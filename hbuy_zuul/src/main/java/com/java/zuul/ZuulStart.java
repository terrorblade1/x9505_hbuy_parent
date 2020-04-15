package com.java.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * Author: 乐科
 * Date: 2020/3/9 21:28
 * 网关的启动类
 */
@SpringBootApplication
@EnableEurekaClient  //启动注册中心的客户端
@EnableZuulProxy  //开启网关代理
public class ZuulStart {

    public static void main(String[] args) {
        SpringApplication.run(ZuulStart.class);
    }
}
