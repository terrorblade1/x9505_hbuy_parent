package com.java.service;

import com.java.model.WebOrderEntity;

import java.util.Map;

/**
 * 
 * @author yk
 *    WebOrder业务层接口
 * @date 2020-03-09 17:42:42
 */
public interface WebOrderService extends BaseService<WebOrderEntity>{
	
	//监听mysql订单表 (监听未支付的订单)
    void listenerOrder() throws Exception;

}
