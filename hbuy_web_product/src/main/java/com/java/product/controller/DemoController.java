package com.java.product.controller;

import com.java.model.WebProductDetailEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * Author: 乐科
 * Date: 2020/3/20 10:27
 * 测试的控制器
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @RequestMapping("/toTest01")
    public ModelAndView toTest01(ModelAndView modelAndView){
        modelAndView.setViewName("test01");
        //1. 普通属性数据的传值
        modelAndView.addObject("username","王二麻子");
        modelAndView.addObject("age",21);
        modelAndView.addObject("money",25.90d);
        modelAndView.addObject("date",new Date());
        modelAndView.addObject("char",'害');
        modelAndView.addObject("boolean",false);
        //2. 对象属性的传值
        WebProductDetailEntity productDetail = new WebProductDetailEntity();
        productDetail.setId(9527l);
        productDetail.setTitle("手机");
        productDetail.setSubtitle("iphone");
        productDetail.setAvatorimg("iphone.jpg");
        productDetail.setPrice(6599.0f);
        productDetail.setColor("white");
        productDetail.setNum(9999999);
        productDetail.setHref("https:www.baidu.com");
        productDetail.setUpdatetime(new Date());
        modelAndView.addObject("productDetail",productDetail);
        //List集合传值
        List<String> strs = Arrays.asList("张三","李四","王五","赵六");
        modelAndView.addObject("strs",strs);
        //Map集合的传值
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("username","翠花");
        map.put("date",new Date());
        map.put("char","哦");
        map.put("num",99999);
        map.put("float",99.99f);
        //map.put("productDetail",productDetail);
        //map.put("list",strs);
        modelAndView.addObject("map",map);

        return modelAndView;
    }

}
