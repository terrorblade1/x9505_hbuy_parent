package com.java.solr;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Author: 乐科
 * Date: 2020/3/9 16:52
 * 搜索引擎模块启动类
 */
@SpringBootApplication(scanBasePackages = "com.java.solr.*")  //SpringBoot启动注解
@EnableEurekaClient  //启用注册中心客户端
@MapperScan(basePackages = "com.java.solr.mapper")  //mapper接口的扫描
@EnableScheduling  //开启任务调度
//@ServletComponentScan(basePackages = "com.java.solr.filter")  //扫描到过滤器的包
public class SolrStart {

    public static void main(String[] args) {
        SpringApplication.run(SolrStart.class);
    }
}
