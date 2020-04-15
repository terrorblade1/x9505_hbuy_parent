package com.java.service;

import org.bson.Document;

import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/3/25 10:33
 * 商品评论业务层接口
 */
public interface DiscussService {

    /**
     * 评论的分页查询
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    List<Document> findPageDiscuss(Integer page,Integer limit) throws Exception;
}
