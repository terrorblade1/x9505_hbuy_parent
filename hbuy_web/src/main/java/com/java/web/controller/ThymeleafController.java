package com.java.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Author: 乐科
 * Date: 2020/3/10 16:09
 * thymeleaf模板测试的控制器
 */
@Controller
@RequestMapping("/thymeleaf")
public class ThymeleafController {

    @RequestMapping("/show1")
    public ModelAndView show1(ModelAndView modelAndView){
        //设置跳转的视图
        modelAndView.setViewName("show");
        //设置数据
        modelAndView.addObject("welcomewords","你好啊1");
        return modelAndView;
    }

    @RequestMapping("/show2")
    public String show2(Model model){
        model.addAttribute("welcomewords","你好啊2");
        return "show";
    }

    @RequestMapping("/show3")
    public String show3(Map<String,Object> map){
        map.put("welcomewords","你好啊3");
        return "show";
    }

    @RequestMapping("/show4")
        public String show4(HttpServletRequest request){
        request.setAttribute("welcomewords","你好啊4");
        request.setAttribute("url","https://www.baidu.com");
        return "show";
    }
}
