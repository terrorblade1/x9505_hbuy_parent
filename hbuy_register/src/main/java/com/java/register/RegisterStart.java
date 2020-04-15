package com.java.register;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Author: 乐科
 * Date: 2020/3/9 16:52
 * 前台注册模块启动类
 */
@SpringBootApplication(scanBasePackages = "com.java.register.*")  //SpringBoot启动注解
@EnableEurekaClient  //启用注册中心客户端
@MapperScan(basePackages = "com.java.register.mapper")  //mapper接口的扫描
//@ServletComponentScan(basePackages = "com.java.register.filter")  //扫描到过滤器的包
public class RegisterStart {

    public static void main(String[] args) {
        SpringApplication.run(RegisterStart.class);
    }
}
