package com.java.service;

import com.java.model.Good;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Author: 乐科
 * Date: 2020/3/27 9:14
 * 购物车业务层接口
 */
public interface BuyCarService {

    /**
     * 未登录的情况下实现购物车的添加
     * @param goodId  商品id
     * @param num  商品数量
     * @param request  请求对象
     * @param response  响应对象
     * @return
     * @throws Exception
     */
    Map<String,Object> addBuyCar(Integer goodId, Integer num, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 已登录的情况下实现购物车的添加
     * @param goodId  商品id
     * @param num  商品数量
     * @param uid  登录的用户id
     * @param request  请求对象
     * @param response  响应对象
     * @return
     * @throws Exception
     */
    Map<String,Object> addBuyCarAfterLogin(Integer goodId, Integer num, Integer uid, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 用户登录时合并购物车数据
     * @param uid  登录的用户id
     * @param request  请求对象
     * @param response  响应对象
     * @return
     * @throws Exception
     */
    Map<String,Object> appendBuyCarByLogin(Integer uid, HttpServletRequest request, HttpServletResponse response) throws Exception;

    /**
     * 查询购物车数据
     * @param token  登录用户的令牌
     * @param request  请求对象
     * @return
     * @throws Exception
     */
    List<Good> findBuyCar(String token, HttpServletRequest request) throws Exception;

    /**
     * 删除提交后的购物车商品数据
     * @param uid  用户id
     * @param proIds  选中的商品id
     * @throws Exception
     */
    void removeGoodByProIds(Integer uid, String proIds) throws Exception;

}
