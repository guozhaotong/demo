package com.example.guozhaotong.demo.utils;

import com.alibaba.fastjson.JSON;
import com.example.guozhaotong.demo.entity.Person;
import okhttp3.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Comparator;
import java.util.List;

public class Utils {
    public static void main(String[] args) {
        Utils utils = new Utils();
//        String name, double chinese, double math, double english
        System.out.println(utils.httpPost("http://localhost:8080/addrecord",
                "name", "Jack",
                "chinese", "99",
                "math","98",
                "english", "97"));
    }

    /**
     * 根据不同课程对人员进行排序
     *
     * @param personList： 排序前的人员列表
     * @param course      ： 排序课程
     */
    public void sort(List<Person> personList, String course) {
        if (personList == null || personList.size() == 1) {
            return;
        }
        final double[] value1 = new double[1];
        final double[] value2 = new double[1];
        personList.sort(new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                switch (course) {
                    case "Chinese":
                        value1[0] = o1.getChineseGrade();
                        value2[0] = o2.getChineseGrade();
                        break;
                    case "Math":
                        value1[0] = o1.getMathGrade();
                        value2[0] = o2.getMathGrade();
                        break;
                    case "English":
                        value1[0] = o1.getEnglishGrade();
                        value2[0] = o2.getEnglishGrade();
                        break;
                }
                if (value1[0] > value2[0]) {
                    return -1;
                } else if (value1[0] == value2[0]) {
                    return 0;
                } else {
                    return 1;
                }
            }
        });
        return;
    }

    /**
     * 获取本机IP
     *
     * @return
     */
    public String getIp() {
        String ip = "";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return ip;
    }

    /**
     * http get调用的util函数。
     * @param url： http请求的url
     * @return 返回response的body内容。
     */
    public String httpGet(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        String responseContent = "";
        try {
            response = client.newCall(request).execute();
            if (response == null || response.body() == null || response.body().string() == null) {
                return null;
            }
            responseContent = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseContent;
    }

    /**
     * http post调用的util函数
     * @param url   请求URL
     * @param param post请求的参数，以参数名、参数值、参数名、参数值、。。。交替出现，都是String格式，没有的话填写null
     * @return
     */
    public String httpPost(String url, String... param) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        if (param != null) {
            for (int i = 0; i < param.length; i += 2) {
                formBody.add(param[i], param[i + 1]);//传递键值对参数
            }
        }
        Request request = new Request.Builder()//创建Request对象。
                .url(url)
                .post(formBody.build())//传递请求体
                .build();
        String responseContent = null;
        try {
            responseContent = client.newCall(request).execute().body().string();//回调方法的使用与get异步请求相同，此时略。
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseContent;
    }

    /**
     * 从json字符串转换为java对象
     *
     * @param json ： json字符串
     * @param c    ： 目标对象所属类的class
     * @return
     */
    public Object json2Obj(String json, Class c) {
        Object o = null;
        try {
            o = JSON.parseObject(json, c);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }

}
