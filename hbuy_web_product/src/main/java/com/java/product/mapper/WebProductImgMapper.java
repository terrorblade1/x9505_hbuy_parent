package com.java.product.mapper;

import com.java.model.WebProductImgEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 
 * @author yk
 *    WebProductImgMapper层
 * @date 2020-03-09 17:42:43
 */
@Repository
public interface WebProductImgMapper extends BaseMapper<WebProductImgEntity> {

    /**
     * 根据产品id查找多个产品图片数据
     * @param productId
     * @return
     * @throws Exception
     */
    List<WebProductImgMapper> queryWebProductImgByProductId(Integer productId) throws Exception;
}
