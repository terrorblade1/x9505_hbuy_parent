package com.java.register.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java.model.WebUsersEntity;
import com.java.service.WebUsersService;

import java.util.Map;

/**
 * 
 * @author yk
 *    WebUsers业务层实现类
 * @date 2020-03-07 20:22:46
 */
@Service
@Transactional
public class WebUsersServiceImpl extends BaseServiceImpl<WebUsersEntity> implements WebUsersService {
    @Override
    public Map<String, Object> loginUser(WebUsersEntity user) throws Exception {
        return null;
    }
}
