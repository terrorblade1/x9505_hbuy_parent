package com.java.service;

import com.java.model.WebSeckillEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author yk
 *    WebSeckill业务层接口
 * @date 2020-03-09 17:42:43
 */
public interface WebSeckillService {

    //将商品秒杀数据放入redis中
    Map<String,Object> addSecKillToRedis() throws Exception;

    //将秒杀的商品状态改为开始秒杀
    String updateUPSecKillStatus() throws Exception;

    //将秒杀的商品状态改为结束秒杀
    String updateEndSecKillStatus() throws Exception;

    //执行秒杀
    Map<String,Object> doSeckill(String token, Long secId) throws Exception;

    //加载已经开始秒杀的商品
    List<Map<String,Object>> findUPSecKill() throws Exception;

}
