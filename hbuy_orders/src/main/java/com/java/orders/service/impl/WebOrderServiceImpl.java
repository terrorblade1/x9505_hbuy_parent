package com.java.orders.service.impl;

import com.java.model.WebOrderEntity;
import com.java.orders.mapper.WebOrderMapper;
import com.java.service.WebOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author yk
 *    WebOrder业务层实现类
 * @date 2020-03-09 17:42:42
 */
@Service
@Transactional
public class WebOrderServiceImpl extends BaseServiceImpl<WebOrderEntity> implements WebOrderService {

    //注入redis模板
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 监听mysql订单表 (监听未支付的订单)
     * @return
     * @throws Exception
     */
    @Override
    @Scheduled(cron = "0/5 * * * * ? ")  //每隔5秒执行
    public void listenerOrder() throws Exception {
        //查询已经失效的订单 (超时未支付)
        List<WebOrderEntity> orders = webOrderMapper.listenerOrder();
        if (orders != null){
            //通过循环修改订单状态, 然后在redis中修改数据
            for (WebOrderEntity order:orders){
                WebOrderEntity updOrder = new WebOrderEntity();
                updOrder.setId(order.getId());
                updOrder.setOrderstatus("5");  //将订单状态改为已失效
                //修改mysql订单表中的订单状态
                Integer count = webOrderMapper.update(updOrder);
                if (count > 0){
                    System.out.println("订单号: "+order.getOrderno()+", 此订单状态已修改");
                    //操作redis   list+ set-
                    SetOperations sop = redisTemplate.opsForSet();
                    String key = "secKill_" + order.getSecid() + "_" + order.getProids();
                    String value = order.getSecid()+","+order.getUserid()+","+order.getProids();
                    Long removeCount = 0l;
                    //删除redis中的set集合中 秒杀成功但未支付的用户数据
                    if (sop.isMember(key,value)){  //判断是否存在
                        removeCount = sop.remove(key,value);  //删除
                    }
                    //将商品数据加回redis中的list集合中
                    ListOperations lop = redisTemplate.opsForList();
                    Long addCount = lop.leftPush(order.getSecid() + "_" + order.getProids(), order.getSecid() + "," + order.getProids());
                    if (removeCount >0 && addCount >0){
                        System.out.println("订单: " + order.getOrderno() + "已取消");
                    }
                } else {
                    System.out.println("订单号: "+order.getOrderno()+", 订单状态修改失败");
                }
            }

        } else {
            System.out.println("没有超时未支付的订单");
        }

    }
}
