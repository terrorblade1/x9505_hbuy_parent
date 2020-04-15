package com.java.solr.model;

import org.apache.solr.client.solrj.beans.Field;

/**
 *   学生实体封装类
 */
public class Student {

    //@Field  写value: 指定域名与属性对应, 不写value: 则域名与属性名相同

    //学生编号
    @Field(value = "xh")
    private Integer xh;
    //姓名
    @Field
    private String name;
    //年纪
    @Field
    private Integer age;
    //性别
    @Field
    private String sex;

    public Integer getXh() {
        return xh;
    }

    public void setXh(Integer xh) {
        this.xh = xh;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Student(Integer xh, String name, Integer age, String sex) {
        this.xh = xh;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }

    public Student() {
    }

    @Override
    public String toString() {
        return "Student{" +
                "xh=" + xh +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                '}';
    }
}
