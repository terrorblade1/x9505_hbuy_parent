package com.java.solr.mapper;

import com.java.model.ProductSolr;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/4/8 20:32
 * 商品详情Mapper代理对象
 */
public interface ProductMapper {

    //查询所有的商品详情数据
    @Select("select id as pid,title,price,avatorImg from web_product_detail")
    List<ProductSolr> queryAll() throws Exception;

}
