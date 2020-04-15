package com.java.service;

import java.util.Map;

/**
 * Author: 乐科
 * Date: 2020/4/1 20:14
 * 消息队列的业务层接口
 */
public interface RabbitMQService {

    //将秒杀数据装入到消息队列中
    void addRabbitMQToExCFromSeckill(Long secId, Long proId, Float secPrice, Integer uid) throws Exception;

    //将购物车数据装入到消息队列中
    void addRabbitMQToExCFromBuyCar(String proIds, Float zPrice, Integer uid) throws Exception;

}
