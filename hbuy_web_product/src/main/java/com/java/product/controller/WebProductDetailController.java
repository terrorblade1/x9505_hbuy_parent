package com.java.product.controller;

import com.java.model.WebProductDetailEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author yk
 *   WebProductDetail控制器
 * @date 2020-03-09 17:42:42
 */
@Controller
@RequestMapping("/webproductdetail")
public class WebProductDetailController extends BaseController<WebProductDetailEntity>{

    /**
     * 根据自定义freemarker模板生成商品详情的静态页面
     * @return
     */
    @RequestMapping("/makeProductDetailHtml")
    public @ResponseBody String makeProductDetailHtml(){
        try {
            webProductDetailService.makeProductDetail();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }
}
