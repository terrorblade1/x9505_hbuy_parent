package com.java.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Author: 乐科
 * Date: 2020/3/8 14:20
 * 注册中心1的启动类
 */
@SpringBootApplication  //SpringBoot的启动注解
@EnableEurekaServer  //启动注册中心的服务
public class EurekaStart1 {

    public static void main(String[] args) {
        SpringApplication.run(EurekaStart1.class);
    }
}
