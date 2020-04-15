package com.java.solr.utils;

import org.apache.solr.client.solrj.impl.HttpSolrClient;

/**
 *   创建solr的链接对象
 */
public class SolrUtil {

    //定义solr的链接对象
    private static HttpSolrClient solr = null;

    static {
        //1.定义solr的服务器链接路径
        String solrUrl="http://localhost:8888/solr/new_core";
        //2.创建solr的链接对象
        solr = new HttpSolrClient.Builder(solrUrl).
                withConnectionTimeout(10000).
                withSocketTimeout(60000).build();
    }

    public static HttpSolrClient getSolr() {
        return solr;
    }


}
