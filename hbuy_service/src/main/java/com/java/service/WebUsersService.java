package com.java.service;

import com.java.model.WebUsersEntity;

import java.util.Map;

/**
 * 
 * @author yk
 *    WebUsers业务层接口
 * @date 2020-03-09 17:42:42
 */
public interface WebUsersService extends BaseService<WebUsersEntity>{

    /**
     * 用户登录
     * @param user
     * @return
     * @throws Exception
     */
	Map<String,Object> loginUser(WebUsersEntity user) throws Exception;
}
