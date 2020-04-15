package com.java.service;

import com.java.model.WebProductDetailEntity;

/**
 * 
 * @author yk
 *    WebProductDetail业务层接口
 * @date 2020-03-09 17:42:42
 */
public interface WebProductDetailService extends BaseService<WebProductDetailEntity>{

    /**
     * 根据自定义freemarker模板生成商品详情的静态页面
     * @throws Exception
     */
	void makeProductDetail() throws Exception;
}
