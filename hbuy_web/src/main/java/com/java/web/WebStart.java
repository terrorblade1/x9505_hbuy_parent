package com.java.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Author: 乐科
 * Date: 2020/3/9 16:52
 * 前台首页模块启动类
 */
@SpringBootApplication(scanBasePackages = "com.java.web.*")  //SpringBoot启动注解
@EnableEurekaClient  //启用注册中心客户端
@MapperScan(basePackages = "com.java.web.mapper")  //mapper接口的扫描
@ServletComponentScan(basePackages = "com.java.web.filter")  //扫描到过滤器的包
public class WebStart {

    public static void main(String[] args) {
        SpringApplication.run(WebStart.class);
    }
}
