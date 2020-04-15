package com.java.web.consumer.controller;

import com.java.model.WebBannerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/3/19 13:04
 * web模块消费者的控制器
 */
@Controller
@RequestMapping("/WebConsumerController")
public class WebConsumerController {

    //依赖注入Spring的客户端模板
    @Autowired
    private RestTemplate restTemplate;

    //测试
    @RequestMapping("/testConsumerRibbon/{username}")
    public @ResponseBody String testConsumerRibbon(@PathVariable("username") String username){
        return restTemplate.getForObject("http://web-provider/webProviderController/testRibbon/"+username, String.class);
    }

    /**
     * 加载所有轮播图数据
     * @return
     */
    @RequestMapping("/loadAllBanner")
    public @ResponseBody List<WebBannerEntity> loadAllBanner(){

        return restTemplate.getForObject("http://web-provider/webbanner/loadAll",List.class);
    }
}
