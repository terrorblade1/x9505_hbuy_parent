package com.java.solr.service.impl;

import com.java.model.ProductSolr;
import com.java.service.ProductSolrService;
import com.java.solr.mapper.ProductMapper;
import com.java.solr.utils.SolrUtil;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/4/8 20:55
 */
@Service
@Transactional(readOnly = false)
public class ProductSolrServiceImpl implements ProductSolrService {

    //依赖注入Mapper代理对象
    @Autowired
    private ProductMapper productMapper;

    //通过工具类获取solr链接对象
    private HttpSolrClient solr = SolrUtil.getSolr();

    /**
     * 将mysql中的数据添加到solr中
     * @throws Exception
     */
    @Override
    @Scheduled(cron = "0/10 * * * * ? ")  //每隔十秒执行一次
    public void addDataFromMySqlToSolr() throws Exception {
        //先将之前solr中的数据删除
        solr.deleteByQuery("*:*");
        //查询mysql中的商品数据
        List<ProductSolr> productSolrs = productMapper.queryAll();
        //批量添加到solr
        UpdateResponse updateResponse = solr.addBeans(productSolrs);
        //提交
        solr.commit();
        System.out.println(updateResponse.getStatus()==0?"数据成功添加到solr":"数据添加失败");
    }

    /**
     * 根据条件加载solr引擎中的商品数据
     * @param solrPar  条件
     * @return
     * @throws Exception
     */
    @Override
    public List<ProductSolr> findProductBySolr(String solrPar) throws Exception {
        //新建查询条件对象
        SolrQuery solrQuery = new SolrQuery();
        //设置查询的条件
        solrQuery.set("q","title:"+solrPar);
        //设置分页
        solrQuery.setStart(0);
        solrQuery.setRows(5);
        //执行查询
        QueryResponse queryResponse = solr.query(solrQuery);
        System.out.println(queryResponse);
        //将查询结果转为List集合
        List<ProductSolr> productSolrList = queryResponse.getBeans(ProductSolr.class);
        return productSolrList;
    }
}
