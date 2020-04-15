package com.java.product.service.impl;

import com.java.product.utils.MongoDBUtils;
import com.java.service.DiscussService;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: 乐科
 * Date: 2020/3/25 10:37
 * 商品评论业务层接口实现类
 */
@Service
@Transactional(readOnly = false)
public class DiscussServiceImpl implements DiscussService {

    //MongoDB集合的链接对象
    private MongoCollection<Document> collection = MongoDBUtils.getCollection();

    /**
     * 分页查询商品评论数据
     * @param page
     * @param limit
     * @return
     * @throws Exception
     */
    @Override
    public List<Document> findPageDiscuss(Integer page, Integer limit) throws Exception {
        //执行分页查询
        FindIterable<Document> documents = collection.find().skip((page - 1) * limit).limit(limit);
        //新建评论的List集合(因为SpringMVC框架不能直接将 FindIterable<Document> 转为JSON数据)
        List<Document> list = new ArrayList<Document>();
        //将MongoDB中的文档数据循环, 并装入评论的List集合中
        documents.iterator().forEachRemaining(temp -> list.add(temp));
        return list;
    }
}
