package com.java.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java.admin.model.AdminMenusEntity;
import com.java.admin.service.AdminMenusService;

/**
 * 
 * @author yk
 *    AdminMenus业务层实现类
 * @date 2020-03-07 20:22:46
 */
@Service
@Transactional
public class AdminMenusServiceImpl extends BaseServiceImpl<AdminMenusEntity> implements AdminMenusService {
	
}
