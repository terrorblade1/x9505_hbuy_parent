package com.java.product.controller;

import com.java.service.DiscussService;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/3/25 11:07
 * 商品详情评论的控制器
 */
@Controller
@RequestMapping("/discuss")
public class DiscussController {

    //评论的业务层对象
    @Autowired
    private DiscussService discussService;

    /**
     * 分页加载商品评论数据
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping("/loadPageDiscuss")
    public @ResponseBody List<Document> loadPageDiscuss(Integer page,Integer limit){
        try {
            return discussService.findPageDiscuss(page,limit);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
