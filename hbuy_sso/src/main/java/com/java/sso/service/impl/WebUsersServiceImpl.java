package com.java.sso.service.impl;

import com.java.model.WebUsersEntity;
import com.java.service.WebUsersService;
import com.java.sso.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author yk
 *    WebUsers业务层实现类
 * @date 2020-03-09 17:42:42
 */
@Service
@Transactional
public class WebUsersServiceImpl extends BaseServiceImpl<WebUsersEntity> implements WebUsersService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 重写根据条件查询单个数据(登录)
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> loginUser(WebUsersEntity user) throws Exception {
        Map<String,Object> map = new HashMap<String, Object>();
        //登录前对密码进行MD5加密
        user.setPwd(MD5.md5crypt(user.getPwd()));
        //执行登录(查询登录用户的数据)
        WebUsersEntity loginUser = baseMapper.queryObjectByPramas(user);
        //判断是否登录成功
        if (user != null){  //登录成功
            map.put("loginUser",loginUser);
            //产生令牌token(使用UUID)
            String token = UUID.randomUUID().toString();
            map.put("token",token);
            map.put("code",0);
            ValueOperations vop = redisTemplate.opsForValue();
            //往redis中存放用户数据，设置60分钟内有效
            vop.set("sessionId"+token,loginUser,60, TimeUnit.MINUTES);
        } else {  //登录失败
            map.put("code",200);
        }
        return map;
    }

}
