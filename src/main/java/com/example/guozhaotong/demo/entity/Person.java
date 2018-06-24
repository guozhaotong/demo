package com.example.guozhaotong.demo.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private double chineseGrade;
    private double mathGrade;
    private double englishGrade;

    public Person(String name, double chineseGrade, double mathGrade, double englishGrade) {
        this.name = name;
        this.chineseGrade = chineseGrade;
        this.mathGrade = mathGrade;
        this.englishGrade = englishGrade;
    }

    public Person() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getChineseGrade() {
        return chineseGrade;
    }

    public void setChineseGrade(double chineseGrade) {
        this.chineseGrade = chineseGrade;
    }

    public double getMathGrade() {
        return mathGrade;
    }

    public void setMathGrade(double mathGrade) {
        this.mathGrade = mathGrade;
    }

    public double getEnglishGrade() {
        return englishGrade;
    }

    public void setEnglishGrade(double englishGrade) {
        this.englishGrade = englishGrade;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chineseGrade=" + chineseGrade +
                ", mathGrade=" + mathGrade +
                ", englishGrade=" + englishGrade +
                '}';
    }

}
