package com.java.admin.controller;

import com.java.admin.utils.MD5;
import com.java.admin.utils.VerifyCodeUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.java.admin.model.AdminUsersEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 
 * @author yk
 *   AdminUsers控制器
 * @date 2020-03-07 20:22:46
 */
@Controller
@RequestMapping("/adminusers")
public class AdminUsersController extends BaseController<AdminUsersEntity>{

	/**
	 * 获取用户登录时的随机验证码
	 * @param session
	 * @param response
	 */
	@RequestMapping("/getVerifyCode")
	public void getVerifyCode(HttpSession session, HttpServletResponse response) throws Exception{
		//1.通过工具类产生随机验证码  如3DcfG
		String verifyCode = VerifyCodeUtils.generateVerifyCode(5);
		//2.将服务器端产生的随机验证码中的英文字母转为小写并放在session容器中   3dcfg
		session.setAttribute("verifyCode",verifyCode.toLowerCase());
		System.out.println(verifyCode);
		//3.将产生的验证码转为图片显示（响应）到页面中   3DcfG
		VerifyCodeUtils.outputImage(250,35,response.getOutputStream(),verifyCode);
	}

	//验证用户输入的验证码
	@RequestMapping("/checkVerifyCode")
	public @ResponseBody String checkVerifyCode(HttpSession session, String verifyCodeInput){
		//1.从session容器中取出之前装入的验证码
		String verifyCode = (String) session.getAttribute("verifyCode");
		//2.此时将用户输入的验证码与session中取出的验证码进行比较
		System.out.println("生成的随机验证码: "+verifyCode);
		System.out.println("输入的验证码: "+verifyCodeInput);
		if (verifyCodeInput.equals(verifyCode)){
			return "success";
		} else {
			return "fail";
		}
	}

	/**
	 * 执行登录
	 * @param user  用户参数(用户名,密码)
	 * @param session  session容器
	 * @return  返回登录结果
	 */
	@RequestMapping("/login")
	public @ResponseBody
	String login(AdminUsersEntity user, HttpSession session){
		//将密码进行MD5加密(单向)
		user.setPwd(MD5.md5crypt(user.getPwd()));
		try {
			//根据用户名和密码查询数据库中的对象
			AdminUsersEntity loginUser = baseService.findObjectByPramas(user);
			//判断是否登录成功
			if (loginUser != null){  //有此用户,登录成功
				//将登录的用户对象装入到session容器中
				session.setAttribute("loginUser",loginUser);
				return "success";
			} else {  //登录失败
				return "fail";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}

	/**
	 * 执行退出
	 * @param session
	 * @return
	 */
	@RequestMapping("/exitUser")
	public @ResponseBody String exitUser(HttpSession session){
		try {
			//移出session容器中登录的用户对象
			session.removeAttribute("loginUser");
			return "success";
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
	}
}
