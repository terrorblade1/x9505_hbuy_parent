package com.java.orders.mapper;

import com.java.model.WebOrderEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author yk
 *    WebOrderMapper层
 * @date 2020-03-09 17:42:42
 */
@Repository
public interface WebOrderMapper extends BaseMapper<WebOrderEntity> {

    //监听mysql订单表 (监听未支付的订单)
    @Select("SELECT * FROM web_order WHERE endDate<=NOW() AND orderStatus=1 AND flag=2")
    List<WebOrderEntity> listenerOrder() throws Exception;
}
