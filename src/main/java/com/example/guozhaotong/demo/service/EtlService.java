package com.example.guozhaotong.demo.service;

import com.alibaba.fastjson.JSON;
import com.example.guozhaotong.demo.entity.Person;
import com.example.guozhaotong.demo.entity.TimedTask;
import com.example.guozhaotong.demo.repository.PersonRepository;
import com.example.guozhaotong.demo.repository.TimedTaskRepository;
import com.example.guozhaotong.demo.utils.EhCacheUtil;
import com.example.guozhaotong.demo.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class EtlService {
    @Value("${tongtong.etl.port}")
    String etlPort;

    private Logger logger = LoggerFactory.getLogger(EtlService.class);
    //    private Jedis jedis;
    private Utils utils = new Utils();
    private HashMap<Long, ScheduledFuture> timerHashMap = new HashMap<>();


    @Autowired
    PersonRepository personRepository;
    @Autowired
    TimedTaskRepository timedTaskRepository;

    public String hello() {
        return "Hello world!";
    }

    public List<Person> listPerson() {
        String personListString = EhCacheUtil.getInstance().get("ehcache001", "list");
        List<Person> personList = null;
        if (personListString == null) {
            personList = personRepository.findAll();
            logger.info("从数据库中查询列表");
        } else {
            Person[] persons = (Person[]) utils.json2Obj(personListString, Person[].class);
            personList = Arrays.asList(persons);
            logger.info("从缓存数据库ehcache中读取列表");
        }
        EhCacheUtil.getInstance().put("ehcache001", "list", JSON.toJSON(personList));
        return personList;
    }

    public Person addRecord(String name, double chinese, double math, double english) {
        Person person = new Person(name, chinese, math, english);
        person = personRepository.save(person);
        EhCacheUtil.getInstance().remove("ehcache001", "list");
        logger.info("增加人员 —— " + name + " " + chinese + " " + math + " " + english);
        return person;
    }

    public List<Person> sortedByChinese(int page, int size) {
        if (page == 0) {
            return sortedByChinese();
        }
        if (size <= 0) {
            return null;
        }
        List<Person> personList = listPerson();
        utils.sort(personList, "Chinese");
        List<Person> res = new ArrayList<>();
        for (int i = size * page - size; i < size * page && i < personList.size(); i++) {
            res.add(personList.get(i));
        }
        logger.info("按照语文成绩排序 —— 访问第" + page + "页");
        return res;
    }

    public List<Person> sortedByChinese() {
        List<Person> personList = listPerson();
        utils.sort(personList, "Chinese");
        logger.info("按照语文成绩排序");
        return personList;
    }

    public List<Person> sortedByMath() {
        List<Person> personList = listPerson();
        utils.sort(personList, "Math");
        logger.info("按照数学成绩排序");
        return personList;
    }

    public List<Person> sortedByEnglish() {
        List<Person> personList = null;
        //http调用
        String httpResponce = utils.httpGet("http://" + utils.getIp() + ":" + etlPort + "/listPerson");
        if (httpResponce.equals(null)) {
            logger.error("http调用中无response body。");
            return null;
        }
        Person[] persons = (Person[]) utils.json2Obj(httpResponce, Person[].class);
        personList = Arrays.asList(persons);
        utils.sort(personList, "English");
        logger.info("按照英语成绩排序——人员列表通过http调用实现");
        return personList;
    }

    public long timedTask(int year, int month, int day, int hour, int minute, int second, String taskName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        Date time = calendar.getTime();
        Date now = new Date();
        long delayTime = time.getTime() - now.getTime();
        if (delayTime < 0) {
            delayTime = 0;
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                switch (taskName) {
                    case "list":
                        listPerson();
                        break;
                    case "listByChinese":
                        sortedByChinese();
                        break;
                    case "listByMath":
                        sortedByMath();
                        break;
                    case "listByEnglish":
                        sortedByEnglish();
                        break;
                    default:
                        hello();
                }
            }
        };
        TimedTask timedTask = new TimedTask(taskName, JSON.toJSONString(now), JSON.toJSONString(time), 1);
        timedTask = timedTaskRepository.save(timedTask);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> executorHandle = executor.schedule(runnable, delayTime, TimeUnit.MILLISECONDS);
        timerHashMap.put(timedTask.getId(), executorHandle);
        logger.info("定时任务:" + taskName + "设定于" + year + "年" + month + "月" + day + "日" + hour + "时"
                + minute + "分" + second + "秒执行，设置成功！任务id为" + timedTask.getId());
        TimedTask finalTimedTask = timedTask;
        executor.schedule(new Runnable() {
            @Override
            public void run() {
                timerHashMap.remove(finalTimedTask.getId());
            }
        }, delayTime, TimeUnit.MILLISECONDS);
        return timedTask.getId();
    }

    public String deleteTimeTask(long timeTaskId) {
        TimedTask timedTask = null;
        try {
            timedTask = timedTaskRepository.findById(timeTaskId).get();
        } catch (Exception e) {
            String res = "该任务不存在。";
            logger.error("尝试取消任务" + timeTaskId + "，但" + res);
            return res;
        }
        Date dateOfTask = (Date) utils.json2Obj(timedTask.getExecuteTime(), Date.class);
        if (dateOfTask.compareTo(new Date()) < 0) {
            String res = "目前该任务已完成，无法取消执行任务。";
            logger.error("尝试取消任务" + timeTaskId + "，但" + res);
            return res;
        }
        timerHashMap.get(timeTaskId).cancel(true);
        timerHashMap.remove(timeTaskId);
        String res = "任务" + timeTaskId + "取消成功。";
        timedTask.setStatus(0);
        timedTask = timedTaskRepository.save(timedTask);
        logger.info(res);
        return res;
    }

    public List<TimedTask> listAllTimedTask() {
        List<TimedTask> timedTaskList = timedTaskRepository.findAll();
        logger.info("列出所有定时任务列表");
        return timedTaskList;
    }

    public List<TimedTask> listWaitingTimedTask() {
        List<TimedTask> timedTaskList = new ArrayList<>();
        for (long timerId : timerHashMap.keySet()) {
            timedTaskList.add(timedTaskRepository.findById(timerId).get());
        }
        logger.info("列出等待执行的定时任务列表");
        return timedTaskList;
    }

    public List<TimedTask> listInterruptedTimedTask() {
        List<TimedTask> timedTaskList = timedTaskRepository.findByStatus(0);
        logger.info("列出被中断的定时任务列表");
        return timedTaskList;
    }
}
