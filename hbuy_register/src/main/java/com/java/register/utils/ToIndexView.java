package com.java.register.utils;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 *   设置访问首页index.jsp
 *   @Configuration 实例化此类，项目在启动过程中读取的配置
 */
@Configuration
public class ToIndexView extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers( ViewControllerRegistry registry ) {
        System.out.println("执行了配置访问首页的工具类");
        //      /为访问的路径    /static/html/index.html为文件的实际路径
        registry.addViewController( "/" ).setViewName( "html/register" );
        registry.setOrder( Ordered.HIGHEST_PRECEDENCE );
        super.addViewControllers( registry );
    }
}
