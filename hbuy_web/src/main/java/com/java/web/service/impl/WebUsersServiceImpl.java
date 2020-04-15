package com.java.web.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java.web.model.WebUsersEntity;
import com.java.web.service.WebUsersService;

/**
 * 
 * @author yk
 *    WebUsers业务层实现类
 * @date 2020-03-09 17:42:42
 */
@Service
@Transactional
public class WebUsersServiceImpl extends BaseServiceImpl<WebUsersEntity> implements WebUsersService {
	
}
