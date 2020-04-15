package com.java.web.service.impl;

import com.java.model.WebMenuEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java.web.service.WebMenuService;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author yk
 *    WebMenu业务层实现类
 * @date 2020-03-09 17:42:42
 */
@Service
@Transactional
public class WebMenuServiceImpl extends BaseServiceImpl<WebMenuEntity> implements WebMenuService {

    //依赖注入redis的模板
    @Autowired
    private RedisTemplate redisTemplate;

    //重写加载所有菜单的方法
    @Override
    public List<WebMenuEntity> findAll() throws Exception {
        //新建List菜单集合
        List<WebMenuEntity> webMenus = new ArrayList<WebMenuEntity>();
        //得到redis操作菜单List集合的对象
        ListOperations lop = redisTemplate.opsForList();
        //从redis中取菜单集合数据
        webMenus = lop.range("webMenus",0,-1);
        if (webMenus.size() == 0){  //没有数据
            //从mysql中查询数据
            webMenus = baseMapper.queryAll();
            System.out.println("此时数据走的mysql");
            //将查询到的数据装入到redis中
            lop.rightPushAll("webMenus",webMenus);  //先进先出
        } else {
            System.out.println("此时数据走的redis");
        }
        return webMenus;  //返回结果
    }

}
