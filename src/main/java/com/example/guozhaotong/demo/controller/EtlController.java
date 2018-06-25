package com.example.guozhaotong.demo.controller;

import com.example.guozhaotong.demo.entity.Person;
import com.example.guozhaotong.demo.entity.TimedTask;
import com.example.guozhaotong.demo.repository.PersonRepository;
import com.example.guozhaotong.demo.repository.TimedTaskRepository;
import com.example.guozhaotong.demo.utils.Utils;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.Generated;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.*;

@RestController
public class EtlController {

    @Value("${tongtong.redis.host}")//需写代码来加载
            String redisHost;

    @Value("${tongtong.etl.port}")
    String etlPort;

    private final Logger logger = Logger.getLogger(this.getClass());
    private Jedis jedis;
    private Utils utils = new Utils();
    private HashMap<Long, Timer> timerHashMap = new HashMap<>();

    @Autowired
    PersonRepository personRepository;
    @Autowired
    TimedTaskRepository timedTaskRepository;

    /**
     * This is a test api
     *
     * @return
     */
    @GetMapping("/hi")
    public String hello() {
        return "Hello world!";
    }

    @GetMapping("/listPerson")
    public List<Person> listPerson() {
        String personListString = jedis.get("list");
        List<Person> personList;
        if (personListString == null) {
            personList = personRepository.findAll();
            jedis.set("list", personList.toString());
            jedis.expire("list", 10); //设置过期时间为10秒
            logger.info("从数据库中查询列表");
        } else {
            Person[] persons = (Person[]) utils.fromStr2Obj(personListString, Person[].class);
            personList = Arrays.asList(persons);
            jedis.expire("list", 10);
            logger.info("从Redis中读取列表");
        }
        return personList;
    }

    @PostMapping("/addrecord")
    public Person addRecord(String name, double chinese, double math, double english) {
        Person person = new Person(name, chinese, math, english);
        person = personRepository.save(person);
        jedis.del("list");
        logger.info("增加人员 —— " + name + " " + chinese + " " + math + " " + english);
        return person;
    }

    @GetMapping("/sortedbychinesedevidebypage")
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

    @GetMapping("/sortedbychinese")
    public List<Person> sortedByChinese() {
        List<Person> personList = listPerson();
        utils.sort(personList, "Chinese");
        logger.info("按照语文成绩排序");
        return personList;
    }

    @Generated("/sortedbymath")
    public List<Person> sortedByMath() {
        List<Person> personList = listPerson();
        utils.sort(personList, "Math");
        logger.info("按照数学成绩排序");
        return personList;
    }

    @GetMapping("/sortedbyenglish")
    public List<Person> sortedByEnglish() {
        List<Person> personList = null;
        //http调用
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://" + utils.getIp() + ":" + etlPort + "/listPerson")
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.body() == null) {
                return null;
            }
            Person[] persons = (Person[]) utils.fromStr2Obj(response.body().string(), Person[].class);
            personList = Arrays.asList(persons);
        } catch (IOException e) {
            e.printStackTrace();
        }
        utils.sort(personList, "English");
        logger.info("按照英语成绩排序——人员列表通过http调用实现");
        return personList;
    }

    @PostMapping("/timedtask")
    public long timedTask(int year, int month, int day, int hour, int minute, int second, String taskName) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, day, hour, minute, second);
        Date time = calendar.getTime();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
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
        }, time);
        Gson gson = new Gson();
        TimedTask timedTask = new TimedTask(taskName, gson.toJson(new Date()), gson.toJson(time), 1);
        timedTask = timedTaskRepository.save(timedTask);
        timerHashMap.put(timedTask.getId(), timer);
        logger.info("定时任务:" + taskName + "设定于" + year + "年" + month + "月" + day + "日" + hour + "时"
                + minute + "分" + second + "秒执行，设置成功！任务id为" + timedTask.getId());
        Timer deleteHashMapEle = new Timer();
        TimedTask finalTimedTask = timedTask;
        deleteHashMapEle.schedule(new TimerTask() {
            @Override
            public void run() {
                timerHashMap.remove(finalTimedTask.getId());
            }
        }, time);
        return timedTask.getId();
    }

    @PostMapping("/deleteTimeTask")
    public String deleteTimeTask(long timeTaskId) {
        Timer timer = timerHashMap.get(timeTaskId);
        if (timer == null) {
            String res = "该任务不存在。";
            logger.error("尝试取消任务" + timeTaskId + "，但" + res);
            return res;
        }
        TimedTask timedTask = timedTaskRepository.findById(timeTaskId).get();
        Date dateOfTask = (Date) utils.fromStr2Obj(timedTask.getExecuteTime(), Date.class);
        if (dateOfTask.compareTo(new Date()) < 0) {
            String res = "目前该任务已完成，无法取消执行任务。";
            logger.error("尝试取消任务" + timeTaskId + "，但" + res);
            return res;
        }
        timer.cancel();
        timerHashMap.remove(timeTaskId);
        String res = "任务" + timeTaskId + "取消成功。";
        timedTask.setStatus(0);
        timedTask = timedTaskRepository.save(timedTask);
        logger.info(res);
        return res;
    }

    @GetMapping("/listAllTimedTask")
    public List<TimedTask> listAllTimedTask() {
        List<TimedTask> timedTaskList = timedTaskRepository.findAll();
        logger.info("列出所有定时任务列表");
        return timedTaskList;
    }

    @GetMapping("/listWaitingTimedTask")
    public List<TimedTask> listWaitingTimedTask() {
        List<TimedTask> timedTaskList = new ArrayList<>();
        for (long timerId : timerHashMap.keySet()) {
            timedTaskList.add(timedTaskRepository.findById(timerId).get());
        }
        logger.info("列出等待执行的定时任务列表");
        return timedTaskList;
    }

    @GetMapping("/listInterruptedTimedTask")
    public List<TimedTask> listInterruptedTimedTask() {
        List<TimedTask> timedTaskList = timedTaskRepository.findByStatus(0);
        logger.info("列出被中断的定时任务列表");
        return timedTaskList;
    }

    /**
     * 会在构造函数之后、init之前执行。
     */
    @PostConstruct
    private void setLoginBody() {
        this.jedis = new Jedis(redisHost);
    }

    @PreDestroy
    private void destroyJedis() {
        this.jedis.close();
    }
}
