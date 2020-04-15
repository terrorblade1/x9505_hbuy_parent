package com.java.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.java.web.model.WebProductDetailEntity;
 
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
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "webproductdetail";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/webproductdetail.html";
    }
}
