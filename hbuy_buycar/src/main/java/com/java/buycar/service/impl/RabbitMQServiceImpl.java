package com.java.buycar.service.impl;

import com.java.service.RabbitMQService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: 乐科
 * Date: 2020/4/1 20:19
 * 消息队列的业务层接口实现类
 */
@Service
@Transactional(readOnly = false)
public class RabbitMQServiceImpl implements RabbitMQService {

    //依赖注入消息队列模板对象
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 将秒杀数据装入到消息队列中
     * @param secId
     * @param proId
     * @param secPrice
     * @param uid
     * @throws Exception
     */
    @Override
    public void addRabbitMQToExCFromSeckill(Long secId, Long proId, Float secPrice, Integer uid) throws Exception {

    }

    /**
     * 将购物车数据装入到消息队列中
     * @param proIds
     * @param zPrice
     * @param uid
     * @throws Exception
     */
    @Override
    public void addRabbitMQToExCFromBuyCar(String proIds, Float zPrice, Integer uid) throws Exception {
        //设置装入的数据
        Map<String,Object> dataMap = new HashMap<String, Object>();
        dataMap.put("proIds",proIds);
        dataMap.put("zPrice",zPrice);
        dataMap.put("uid",uid);
        dataMap.put("flag",1);
        System.out.println("执行了添加到RabbitMQ的方法");
        rabbitTemplate.convertAndSend("ex_x9505_buyCar","hbuy_buyCarKey",dataMap);
    }
}
