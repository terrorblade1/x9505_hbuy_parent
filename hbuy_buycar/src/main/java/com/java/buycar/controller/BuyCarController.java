package com.java.buycar.controller;

import com.java.model.Good;
import com.java.model.WebUsersEntity;
import com.java.service.BuyCarService;
import com.java.service.RabbitMQService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.uid;

/**
 * Author: 乐科
 * Date: 2020/3/27 9:26
 * 购物车控制器
 */
@Controller
@RequestMapping("/buyCar")
public class BuyCarController {

    //依赖注入购物车业务层对象
    @Autowired
    private BuyCarService buyCarService;

    //依赖注入redis模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    //依赖注入消息队列的业务层对象
    @Autowired
    private RabbitMQService rabbitMQService;

    /**
     * 实现购物车的添加
     * @param goodId  商品id
     * @param num  商品数量
     * @param token  登录用户的令牌
     * @param request  请求对象
     * @param response  响应对象
     * @return
     */
    @RequestMapping("/addBuyCar")
    public @ResponseBody Map<String, Object> addBuyCar(Integer goodId, Integer num, String token, HttpServletRequest request, HttpServletResponse response){
        try {
            //判断令牌是否为空
            if (token == null){
                System.out.println("不存在token");
                //令牌为空, 则用户未登录
                return buyCarService.addBuyCar(goodId,num,request,response);
            } else {
                System.out.println("存在token");
                //令牌不为空, 则到redis中查找用户数据
                ValueOperations vop = redisTemplate.opsForValue();
                WebUsersEntity loginUser = (WebUsersEntity) vop.get("sessionId" + token);
                if (loginUser == null){  //未登录时添加购物车
                    System.out.println("uid == null");
                    return buyCarService.addBuyCar(goodId,num,request,response);
                } else {  //已登录时添加购物车
                    Integer uid = loginUser.getId();
                    System.out.println("uid != null, uid = "+uid);
                    return buyCarService.addBuyCarAfterLogin(goodId,num,uid,request,response);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 用户登录后的购物车合并
     * @param uid  登录用户的id
     * @param request  请求对象
     * @param response  响应对象
     * @return
     */
    @RequestMapping("/loginToAppendBuyCar")
    public @ResponseBody Map<String,Object> loginToAppendBuyCar(Integer uid, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> map = null;
        try {
            //执行购物车合并
            map = buyCarService.appendBuyCarByLogin(uid, request, response);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code","error");
        }
        return map;
    }

    /**
     * 加载购物车数据
     * @param token  登录用户的令牌
     * @return
     */
    @RequestMapping("/loadBuyCar")
    public @ResponseBody List<Good> loadBuyCar(String token, HttpServletRequest request){
        try {
            return buyCarService.findBuyCar(token, request);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 提交购物车数据到RabbitMQ中
     * @param proIds
     * @param zPrice
     * @param token
     * @return
     */
    @RequestMapping("/addRabbitMQToExCFromBuyCar")
    public @ResponseBody Map<String,Object> addRabbitMQToExCFromBuyCar(String proIds, Float zPrice, String token){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            //得到登录用户数据
            ValueOperations vop = redisTemplate.opsForValue();
            WebUsersEntity loginUser = (WebUsersEntity) vop.get("sessionId" + token);
            //判断用户是否登录
            if (loginUser != null){
                rabbitMQService.addRabbitMQToExCFromBuyCar(proIds,zPrice,loginUser.getId());
                map.put("code",0);
                map.put("msg","数据装载成功");
            } else {
                map.put("code",404);
                map.put("msg","用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",200);
            map.put("msg","数据装载异常");
        }
        return map;
    }

    /**
     * 删除提交后的购物车数据 (redis中)
     * @param token  用户登录令牌
     * @param proIds  选中的商品id
     * @return
     */
    @RequestMapping("/delGoodByProIds")
    public @ResponseBody Map<String,Object> delGoodByProIds(String token, String proIds){
        Map<String,Object> map = new HashMap<String,Object>();
        try {
            //得到登录用户数据
            ValueOperations vop = redisTemplate.opsForValue();
            WebUsersEntity loginUser = (WebUsersEntity) vop.get("sessionId" + token);
            if (loginUser != null){
                buyCarService.removeGoodByProIds(loginUser.getId(),proIds);
                map.put("code",0);
                map.put("msg","购物车数据删除成功");
            } else {
                map.put("code",404);
                map.put("msg","用户未登录");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",200);
            map.put("msg","购物车数据删除异常");
        }
        return map;
    }

}
