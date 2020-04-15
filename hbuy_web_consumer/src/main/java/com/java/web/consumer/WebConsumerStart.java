package com.java.web.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 *   web模块的消费者启动器
 *     在启动消费者之前，至少要启动一个提供者
 */
@SpringBootApplication(scanBasePackages = "com.java.web.consumer.*")
@EnableEurekaClient  //注册中心客户端注册
@EnableDiscoveryClient  //开启消费者
@ServletComponentScan(basePackages = "com.java.web.consumer.filter")  //扫描过滤器
public class WebConsumerStart {
    //启动的方法
    public static void main(String[] args) {
        SpringApplication.run(WebConsumerStart.class);
    }

    @Bean  //实例化  接收请求处理响应的模版
    @LoadBalanced  //启动项目时自动加载，创建RestTemplate对象放在SpringIOC容器中
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
