package com.java.seckill.service.impl;

import com.java.model.WebSeckillEntity;
import com.java.model.WebUsersEntity;
import com.java.seckill.mapper.WebSeckillMapper;
import com.java.service.RabbitMQService;
import com.java.service.WebSeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: 乐科
 * Date: 2020/3/30 20:17
 * WebSeckill业务层接口实现类
 */
@Service
@Transactional(readOnly = false)
public class WebSeckillServiceImpl implements WebSeckillService {

    //注入redis模板
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private WebSeckillMapper webSeckillMapper;

    //注入消息队列的业务层对象
    @Autowired
    private RabbitMQService rabbitMQService;

    /**
     * 将商品秒杀数据放入redis中
     * @return
     * @throws Exception
     */
    @Override
    @Scheduled(cron = "0/5 * * * * ? ")  //间隔5秒执行此方法
    public Map<String, Object> addSecKillToRedis() throws Exception {
        //新建返回的Map
        Map<String,Object> map = new HashMap<String, Object>();
        //将符合秒杀的商品从mysql中查出来
        List<WebSeckillEntity> seckillEntityList = webSeckillMapper.selectSecKillByTimes();
        //判断符合秒杀的数据是否存在
        if (seckillEntityList != null){  //符合秒杀的数据存在
            //把存在的商品数据装入到redis中
            for (WebSeckillEntity seckill:seckillEntityList){
                String key = seckill.getId() + "_" +seckill.getProductid();
                //判断redis中是否存在此秒杀商品, 若存在则不装入, 不存在则装入 (只装一次)
                if (!redisTemplate.hasKey(key)){  //不存在此秒杀商品
                    map.put("code",200);
                    map.put("msg","此次装入秒杀商品"+key);
                    System.out.println("此次装入秒杀商品"+key);
                    //获取redis操作List的模板
                    ListOperations lop = redisTemplate.opsForList();
                    for (int i = 0; i < seckill.getNum(); i++) {
                        // 装入到redis中!
                        lop.leftPush(key, seckill.getId() + "," +seckill.getProductid());
                    }
                } else {  //存在此秒杀商品
                    map.put("code",500);
                    map.put("msg","之前已装入秒杀商品"+key);
                    System.out.println("之前已装入秒杀商品"+key);
                }
            }
        } else {  //符合秒杀的数据不存在
            map.put("code",404);
            map.put("msg","没有符合秒杀的数据");
            System.out.println("没有符合秒杀的数据");
        }
        return map;
    }


    /**
     * 将秒杀的商品状态改为开始秒杀 (准备 0 -----> 开始 1)
     * 修改秒杀的商品的状态(准备-->开始)   假如   13：59：55 <= 14：00：00 <= 14：00：05
     * 系统时间14：00：05  服务器最多有5次机会去修改状态updateUPSecKillStatus
     * @throws Exception
     */
    @Override
    @Scheduled(cron = "0/2 * * * * ? ")  //每隔2秒执行
    public String updateUPSecKillStatus() throws Exception {
        //执行状态修改
        if (webSeckillMapper.updateUPSecKillStatus() > 0){
            System.out.println("------状态修改成功, (准备0 ---> 开始1) ------");
            return "success";
        } else {
            System.out.println("------状态修改失败, (准备0 ---> 开始1) ------");
            return "fail";
        }
    }

    /**
     * 将秒杀的商品状态改为结束秒杀 (开始 1 -----> 结束 2)
     * 修改秒杀的商品的状态(准备-->开始)   假如   13：59：55 <= 14：00：00 <= 14：00：05
     * 系统时间14：00：05  服务器最多有5次机会去修改状态updateUPSecKillStatus
     * @throws Exception
     */
    @Override
    @Scheduled(cron = "0/2 * * * * ? ")  //每隔2秒执行
    public String updateEndSecKillStatus() throws Exception {
        //执行状态修改
        if (webSeckillMapper.updateEndSecKillStatus() > 0){
            System.out.println("------状态修改成功, (开始1 ---> 结束2) ------");
            return "success";
        } else {
            System.out.println("------状态修改失败, (开始1 ---> 结束2) ------");
            return "fail";
        }
    }

    /**
     * 用户执行秒杀 (操作的是redis数据库)
     * @param token  用户登录令牌
     * @param secId  秒杀id
     * @return
     */
    @Override
    public Map<String, Object> doSeckill(String token, Long secId) throws Exception {
        //新建操作的结果集合
        Map<String,Object> map = new HashMap<String, Object>();
        //根据token在redis中查找用户登录信息
        ValueOperations vop = redisTemplate.opsForValue();
        WebUsersEntity loginUser = (WebUsersEntity) vop.get("sessionId" + token);
        //判断用户是否登录
        if (loginUser != null){  //已登录
            //根据秒杀id查询该秒杀数据
            WebSeckillEntity seckill = webSeckillMapper.selectObjectById(secId);
            //判断秒杀数据是否存在
            if (seckill != null){
                //秒杀数据存在, 根据秒杀数据的状态来判断是否开始秒杀
                if ("0".equals(seckill.getStatus())){
                    map.put("code",300);
                    map.put("msg","商品还未开始秒杀");
                }
                if ("2".equals(seckill.getStatus())){
                    map.put("code",302);
                    map.put("msg","秒杀活动已结束");
                }
                if ("1".equals(seckill.getStatus())){  //可以秒杀
                    //得到操作List的模板对象
                    ListOperations lop = redisTemplate.opsForList();
                    //得到操作此商品秒杀的List集合的key
                    String listKey = secId + "_" +seckill.getProductid();
                    //根据key来删除List集合中的一个元素
                    //删除成功, 返回值不为null; 删除失败, 返回值为null, 也说明此List中已没有元素, 无法进行秒杀
                    Object obj = lop.leftPop(listKey);
                    if (obj != null){  //还有商品
                        //判断此用户是否重复秒杀
                        //得到操作Set的模板对象
                        SetOperations sop = redisTemplate.opsForSet();
                        //构建set的key值
                        String setkey = "secKill_"+secId+"_"+seckill.getProductid();
                        //构建set元素中的value值
                        String setvalue = secId+","+loginUser.getId()+","+seckill.getProductid();
                        //判断set中是否存在此元素
                        if (!sop.isMember(setkey,setvalue)){  //不存在, 可以秒杀
                            //执行秒杀
                            sop.add(setkey,setvalue);  //将数据存到redis中
                            map.put("code",200);
                            map.put("msg","恭喜你,秒杀成功");
                            // 在秒杀成功的同时, 将数据装入到RabbitMQ中 !
                            //要装入的数据: secId, productId, cost, uid, flag
                            rabbitMQService.addRabbitMQToExCFromSeckill(secId,seckill.getProductid(), seckill.getSeckillprice(), loginUser.getId());
                        } else {  //存在, 不能重复秒杀
                            map.put("code",600);
                            map.put("msg","不能重复秒杀");
                            lop.leftPush(listKey,obj);  //把商品重新加入到List集合中
                        }
                    } else {  //没有商品
                        map.put("code",500);
                        map.put("msg","商品已被秒杀完");
                    }
                }
            } else {
                map.put("code","404");
                map.put("msg","没有查询到此秒杀商品");
            }
        } else {  //未登录
            map.put("code","400");
            map.put("msg","用户未登录, 不能秒杀");
        }
        return map;
    }

    /**
     * 加载已经开始秒杀的商品
     * @return
     * @throws Exception
     */
    @Override
    public List<Map<String, Object>> findUPSecKill() throws Exception {
        return webSeckillMapper.selectUPSecKill();
    }
}
