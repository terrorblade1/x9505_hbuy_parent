package com.java.sso;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * Author: 乐科
 * Date: 2020/3/9 16:52
 * 单点登录模块启动类
 */
@SpringBootApplication(scanBasePackages = "com.java.sso.*")  //SpringBoot启动注解
@EnableEurekaClient  //启用注册中心客户端
@MapperScan(basePackages = "com.java.sso.mapper")  //mapper接口的扫描
@ServletComponentScan(basePackages = "com.java.sso.filter")  //扫描到过滤器的包
public class SsoStart {

    public static void main(String[] args) {
        SpringApplication.run(SsoStart.class);
    }
}
