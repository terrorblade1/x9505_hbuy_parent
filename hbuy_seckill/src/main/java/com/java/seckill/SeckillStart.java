package com.java.seckill;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Author: 乐科
 * Date: 2020/3/9 16:52
 * 商品秒杀模块启动类
 */
@SpringBootApplication(scanBasePackages = "com.java.seckill.*")  //SpringBoot启动注解
@EnableEurekaClient  //启用注册中心客户端
@MapperScan(basePackages = "com.java.seckill.mapper")  //mapper接口的扫描
//@ServletComponentScan(basePackages = "com.java.seckill.filter")  //扫描到过滤器的包
@EnableScheduling  //开启任务调度
public class SeckillStart {

    public static void main(String[] args) {
        SpringApplication.run(SeckillStart.class);
    }

}
