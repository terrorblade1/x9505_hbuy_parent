package com.java.web.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.java.web.model.WebBannerEntity;
import com.java.web.service.WebBannerService;

/**
 * 
 * @author yk
 *    WebBanner业务层实现类
 * @date 2020-03-09 17:42:42
 */
@Service
@Transactional
public class WebBannerServiceImpl extends BaseServiceImpl<WebBannerEntity> implements WebBannerService {
	
}
