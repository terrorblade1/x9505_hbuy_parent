package com.java.web.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java.web.model.WebOrderEntity;
import com.java.web.service.WebOrderService;

/**
 * 
 * @author yk
 *    WebOrder业务层实现类
 * @date 2020-03-09 17:42:42
 */
@Service
@Transactional
public class WebOrderServiceImpl extends BaseServiceImpl<WebOrderEntity> implements WebOrderService {
	
}
