package com.example.guozhaotong.demo.utils;

import com.example.guozhaotong.demo.entity.Person;
import com.google.gson.Gson;

import java.util.*;

public class Utils {
    public static void main(String[] args) {
        Gson gson = new Gson();
        System.out.println(gson.toJson(new Date()));
    }

    public Timer fromStrToTimer(String str) {
        Gson gson = new Gson();
        return gson.fromJson(str, Timer.class);
    }

    public Object fromStr2Obj(String json, Class c) {
        Gson gson = new Gson();
        return gson.fromJson(json, c);
    }

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
//        return personList;
        return;
    }
}
