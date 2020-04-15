package com.java.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Author: 乐科
 * Date: 2020/3/8 16:01
 */
@Controller
@RequestMapping("/model")
public class ModelController {

    /**
     * 去到后台管理平台首页
     * @return
     */
    @RequestMapping("/toIndex")
    public String toIndex(){
        return "index";
    }

    /**
     * 去到后台用户管理页面
     * @return
     */
    @RequestMapping("/toAdminUsers")
    public String toAdminUsers(){
        return "adminusers";
    }

    /**
     * 去到后台菜单管理页面
     * @return
     */
    @RequestMapping("/toAdminMenus")
    public String toAdminMenus(){
        return "adminmenus";
    }

    /**
     * 去到前台菜单导航管理页面
     * @return
     */
    @RequestMapping("/toWebMenu")
    public String toWebMenu(){
        return "webmenu";
    }

}
