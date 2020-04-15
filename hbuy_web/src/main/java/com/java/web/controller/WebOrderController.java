package com.java.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.java.web.model.WebOrderEntity;
 
/**
 * 
 * @author yk
 *   WebOrder控制器
 * @date 2020-03-09 17:42:42
 */
@Controller
@RequestMapping("/weborder")
public class WebOrderController extends BaseController<WebOrderEntity>{
	
	/**
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "weborder";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/weborder.html";
    }
}
