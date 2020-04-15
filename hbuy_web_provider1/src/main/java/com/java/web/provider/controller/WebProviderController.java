package com.java.web.provider.controller;

import com.java.service.WebProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Author: 乐科
 * Date: 2020/3/19 12:59
 * web模块的提供者控制器
 */
@Controller
@RequestMapping("/webProviderController")
public class WebProviderController {

    @Autowired
    private WebProviderService webProviderService;

    @RequestMapping("/testRibbon/{username}")
    public @ResponseBody String testRibbon(@PathVariable("username") String username){
        try {
            return webProviderService.testRibbon(username);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
