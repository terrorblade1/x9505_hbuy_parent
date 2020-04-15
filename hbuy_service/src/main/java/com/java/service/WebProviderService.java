package com.java.service;

/**
 * Author: 乐科
 * Date: 2020/3/19 12:48
 * web模块的提供者业务层接口
 */
public interface WebProviderService {

    /**
     * 测试ribbon的负载均衡的搭建
     * @param username
     * @return
     * @throws Exception
     */
    public String testRibbon(String username) throws Exception;

}
