package com.java.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.java.web.model.WebUsersEntity;
 
/**
 * 
 * @author yk
 *   WebUsers控制器
 * @date 2020-03-09 17:42:42
 */
@Controller
@RequestMapping("/webusers")
public class WebUsersController extends BaseController<WebUsersEntity>{
	
	/**
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "webusers";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/webusers.html";
    }
}
