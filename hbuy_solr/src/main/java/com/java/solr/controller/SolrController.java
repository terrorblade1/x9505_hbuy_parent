package com.java.solr.controller;

import com.java.model.ProductSolr;
import com.java.service.ProductSolrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/4/8 21:36
 * 搜索引擎的控制器
 */
@Controller
@RequestMapping("/solr")
public class SolrController {

    //注入业务层对象
    @Autowired
    private ProductSolrService productSolrService;

    /**
     * 将mysql中的数据添加到solr中
     * @return
     */
    /*@RequestMapping("/addDataToSolr")
    public String addDataToSolr(){
        try {
            productSolrService.addDataFromMySqlToSolr();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }*/

    /**
     * 根据条件加载solr引擎中的商品数据
     * @param solrPar  条件
     * @return
     */
    @RequestMapping("/loadProductBySolr")
    public @ResponseBody List<ProductSolr> loadProductBySolr(String solrPar){
        try {
            return productSolrService.findProductBySolr(solrPar);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
