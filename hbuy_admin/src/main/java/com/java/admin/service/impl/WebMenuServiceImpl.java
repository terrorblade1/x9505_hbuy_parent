package com.java.admin.service.impl;

import com.java.model.WebMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java.admin.service.WebMenuService;

/**
 * 
 * @author yk
 *    WebMenu业务层实现类
 * @date 2020-03-07 20:22:46
 */
@Service
@Transactional
public class WebMenuServiceImpl extends BaseServiceImpl<WebMenuEntity> implements WebMenuService {

    //依赖注入redis的模板对象
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 重写添加的方法
     * @param webMenuEntity
     * @return
     * @throws Exception
     */
    @Override
    public String save(WebMenuEntity webMenuEntity) throws Exception {
        if (baseMapper.insert(webMenuEntity) > 0){  //mysql数据库 添加成功
            //得到操作List集合的对象
            ListOperations lop = redisTemplate.opsForList();
            //往redis中的List加入元素
            lop.rightPush("webMenus",webMenuEntity);
            return "saveSuccess";
        } else {
            return "fail";
        }
    }
}
