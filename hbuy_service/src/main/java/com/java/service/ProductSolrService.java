package com.java.service;

import com.java.model.ProductSolr;

import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/4/8 20:52
 * 商品详情solr业务层接口
 */
public interface ProductSolrService {

    //将mysql中的商品数据添加到solr中
    void addDataFromMySqlToSolr() throws Exception;

    //根据条件加载solr引擎中的商品数据
    List<ProductSolr> findProductBySolr(String solrPar) throws Exception;
}
