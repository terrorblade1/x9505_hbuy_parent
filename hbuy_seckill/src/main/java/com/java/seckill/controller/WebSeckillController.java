package com.java.seckill.controller;

import com.java.model.WebSeckillEntity;
import com.java.service.WebSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * Author: 乐科
 * Date: 2020/3/30 22:15
 * 商品秒杀控制器
 */
@Controller
@RequestMapping("/seckill")
public class WebSeckillController {

    @Autowired
    private WebSeckillService webSeckillService;

    /**
     * 测试用户执行秒杀 (操作的是redis数据库)
     * @param uid  用户id
     * @param secId  秒杀id
     * @return
     */
    /*@RequestMapping("/doSeckill")
    public @ResponseBody Map<String,Object> doSeckill(Integer uid, Long secId){
        try {
            return webSeckillService.doSeckill(uid,secId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/

    /**
     * 用户执行秒杀 (操作的是redis数据库)
     * @param token  用户登录令牌
     * @param secId  秒杀id
     * @return
     */
    @RequestMapping("/doSeckill")
    public @ResponseBody Map<String,Object> doSeckill(String token, Long secId){
        try {
            return webSeckillService.doSeckill(token,secId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 加载已经开始秒杀的商品
     * @return
     */
    @RequestMapping("/loadUPSecKill")
    public @ResponseBody List<Map<String,Object>> loadUPSecKill(){
        try {
            return webSeckillService.findUPSecKill();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
