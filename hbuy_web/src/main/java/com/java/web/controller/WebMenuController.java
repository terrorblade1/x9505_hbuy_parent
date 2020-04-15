package com.java.web.controller;

import com.java.model.WebMenuEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

/**
 * 
 * @author yk
 *   WebMenu控制器
 * @date 2020-03-09 17:42:42
 */
@Controller
@RequestMapping("/webmenu")
public class WebMenuController extends BaseController<WebMenuEntity>{
	
	/**
	 * 页面jsp
	 */
	@RequestMapping("/page")
	public String page(){
		return "webmenu";
	}

    /**
     * 页面html
     */
    @RequestMapping("/html")
    public String html(){
        return "redirect:/html/webmenu.html";
    }
}
