package com.java.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java.admin.model.AdminUsersEntity;
import com.java.admin.service.AdminUsersService;

/**
 * 
 * @author yk
 *    AdminUsers业务层实现类
 * @date 2020-03-07 20:22:46
 */
@Service
@Transactional
public class AdminUsersServiceImpl extends BaseServiceImpl<AdminUsersEntity> implements AdminUsersService {
	
}
