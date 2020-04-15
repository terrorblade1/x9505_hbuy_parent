package com.java.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Author: 乐科
 * Date: 2020/3/20 10:21
 */
@SpringBootApplication(scanBasePackages = "com.java.product.*")
@EnableEurekaClient  //开启注册中心的客户端
@MapperScan("com.java.product.mapper")
@EnableScheduling  //开启任务调度
@ServletComponentScan(basePackages = "com.java.product.filter")  //扫描过滤器
public class ProductStart {

    public static void main(String[] args) {
        SpringApplication.run(ProductStart.class);
    }
}
