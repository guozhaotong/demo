package com.example.guozhaotong.demo.utils;

import com.alibaba.fastjson.JSON;
import com.example.guozhaotong.demo.entity.Person;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.net.URL;

public class EhCacheUtil {
    //ehcache.xml 保存在src/main/resources/
    private static final String path = "/ehcache.xml";

    private URL url;

    private CacheManager manager;

    private static EhCacheUtil ehCache;

    private EhCacheUtil(String path) {
        url = getClass().getResource(path);
        manager = CacheManager.create(url);
    }

    public static EhCacheUtil getInstance() {
        if (ehCache == null) {
            ehCache = new EhCacheUtil(path);
        }
        return ehCache;
    }

    public void put(String cacheName, String key, Object value) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    public String get(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue().toString();
    }

    public Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }

    public void remove(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        cache.remove(key);
    }

    public static void main(String[] args){
        //string测试
        EhCacheUtil.getInstance().put("ehcache001","userEch","test001");
        String val = (String) EhCacheUtil.getInstance().get("ehcache001", "userEch");
        System.out.println(val);

        //object测试
        Person person = new Person();
        person.setId(1l);
        person.setName("jack");
        person.setChineseGrade(100);
        person.setMathGrade(99);
        person.setEnglishGrade(98);
        EhCacheUtil.getInstance().put("ehcache001","userJack",JSON.toJSON(person));

        String person1 = EhCacheUtil.getInstance().get("ehcache001", "userJack").toString();
        System.out.println(person1);
    }
}
