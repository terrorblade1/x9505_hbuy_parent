package com.java.web.provider.service.impl;

import com.java.service.WebProviderService;
import org.springframework.stereotype.Service;

/**
 * Author: 乐科
 * Date: 2020/3/19 12:50
 */
@Service
public class WebProviderServiceImpl implements WebProviderService {

    private Integer num = 0;

    /**
     * 测试ribbon的负载均衡的搭建
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    public String testRibbon(String username) throws Exception {
        num ++;
        System.out.println("这里是 provider 1 执行的请求 ------ 次数: "+num);
        return username+" ------ provider 1";
    }
}
