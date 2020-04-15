package com.java.product.test;

import com.java.model.WebProductDetailEntity;
import com.java.model.WebProductImgEntity;
import com.java.service.WebProductDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/3/20 21:28
 * 商品详情业务层测试类
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class WebProductDetailServiceTest {

    @Autowired
    private WebProductDetailService webProductDetailService;

    //测试查询所有
    @Test
    public void test01(){
        try {
            //执行获取所有商品详情数据
            List<WebProductDetailEntity> productDetailEntities = webProductDetailService.findAll();
            for (WebProductDetailEntity productDetail: productDetailEntities){
                System.out.println(productDetail);
                List<WebProductImgEntity> productImgs = productDetail.getProductImgs();
                for (WebProductImgEntity img:productImgs){
                    System.out.println(img);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
