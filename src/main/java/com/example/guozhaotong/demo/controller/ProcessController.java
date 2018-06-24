package com.example.guozhaotong.demo.controller;

import com.example.guozhaotong.demo.entity.Person;
import com.example.guozhaotong.demo.repository.PersonRepository;
import com.example.guozhaotong.demo.utils.Utils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;

@RestController
public class ProcessController {

    @Value("${tongtong.redis.host}")
    String redisHost;

    private final Logger logger = Logger.getLogger(this.getClass());

    private Jedis jedis;

    @Autowired
    PersonRepository personRepository;

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String hello() {
        return "Hello world!";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<Person> listPerson() {
        String personListString = jedis.get("list");
        List<Person> personList;
        if (personListString == null){
            personList = personRepository.findAll();
            jedis.set("list", personList.toString());
            jedis.expire("list", 10); //设置过期时间为10秒
            logger.info("从数据库中查询列表");
        } else {
            personList = Utils.fromStrToList(personListString);
            jedis.expire("list", 10);
            logger.info("从Redis中读取列表");
        }
        return personList;
    }

    @RequestMapping(value = "/addrecord", method = RequestMethod.POST)
    public Person addRecord(String name, double chinese, double math, double english) {
        Person person = new Person(name, chinese, math, english);
        person = personRepository.save(person);
        jedis.del("list");
        logger.info("增加人员 —— " + name + " " + chinese + " " + math + " " + english);
        return person;
    }

    @RequestMapping(value = "/sortedbychinesedevidebypage", method = RequestMethod.GET)
    public List<Person> sortedByChinese(int page, int size) {
        List<Person> personList = listPerson();
        personList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getChineseGrade() > o2.getChineseGrade()) {
                    return -1;
                } else if (o1.getChineseGrade() == o2.getChineseGrade()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        List<Person> res = new ArrayList<>();
        for (int i = size * page - size; i < size * page && i < personList.size(); i++) {
            res.add(personList.get(i));
        }
        logger.info("按照语文成绩排序 —— 访问第" + page + "页");
        return res;
    }

    @RequestMapping(value = "/sortedbychinese", method = RequestMethod.GET)
    public List<Person> sortedByChinese() {
        List<Person> personList = listPerson();
        personList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getChineseGrade() > o2.getChineseGrade()) {
                    return -1;
                } else if (o1.getChineseGrade() == o2.getChineseGrade()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        logger.info("按照语文成绩排序");
        return personList;
    }

    @RequestMapping(value = "/sortedbymath", method = RequestMethod.GET)
    public List<Person> sortedByMath() {
        List<Person> personList = listPerson();
        personList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getMathGrade() > o2.getMathGrade()) {
                    return -1;
                } else if (o1.getMathGrade() == o2.getMathGrade()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        logger.info("按照数学成绩排序");
        return personList;
    }

    @RequestMapping(value = "/sortedbyenglish", method = RequestMethod.GET)
    public List<Person> sortedByEnglish() {
        List<Person> personList = listPerson();
        personList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if (o1.getEnglishGrade() > o2.getEnglishGrade()) {
                    return -1;
                } else if (o1.getEnglishGrade() == o2.getEnglishGrade()) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        logger.info("按照英语成绩排序");
        return personList;
    }

    @RequestMapping(value = "/timedtask", method = RequestMethod.POST)
    public String timedTask(int year, int month, int day, int hour, int minute, int second, String taskName){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, minute, second);
        Date time = calendar.getTime();
        Timer timer = new Timer();
        String res = "";
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                switch (taskName){
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
        return res;
    }


    /***
     *会在构造函数之后、init之前执行。
     */
    @PostConstruct
    private void setLoginBody() {
       this.jedis = new Jedis(redisHost);
    }

    @PreDestroy
    private void destroyJedis(){
        this.jedis.close();
    }
}
