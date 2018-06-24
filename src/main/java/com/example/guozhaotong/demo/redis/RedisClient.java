package com.example.guozhaotong.demo.redis;

import redis.clients.jedis.Jedis;



public class RedisClient {

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("runoobkey", "www.runoob.com");
        jedis.expire("runoobkey", 2);
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));
    }

}