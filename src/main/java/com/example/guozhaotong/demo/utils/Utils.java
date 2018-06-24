package com.example.guozhaotong.demo.utils;

import com.example.guozhaotong.demo.controller.ProcessController;
import com.example.guozhaotong.demo.entity.Person;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class Utils {
    public static void main(String[] args){
        ProcessController processController = new ProcessController();
        processController.timedTask(2018,6,21,21,41,30,"list");
    }
    public static List<Person> fromStrToList(String str){
        Gson gson=new Gson();
        Person[] personArray = gson.fromJson(str, Person[].class);
        List<Person> personList=Arrays.asList(personArray);
        return personList;
    }
}
