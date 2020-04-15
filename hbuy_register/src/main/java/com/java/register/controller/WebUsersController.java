package com.java.register.controller;

import com.aliyuncs.exceptions.ClientException;
import com.java.register.utils.HtmlMail;
import com.java.register.utils.MD5;
import com.java.register.utils.SmsUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import com.java.model.WebUsersEntity;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * @author yk
 *   WebUsers控制器
 * @date 2020-03-07 20:22:46
 */
@Controller
@RequestMapping("/webusers")
public class WebUsersController extends BaseController<WebUsersEntity>{

	/**
	 * 重写父类的添加方法
	 * @param entity
	 * @return
	 */
	@Override
	public String saveT(WebUsersEntity entity) {
		//对注册用户密码进行MD5加密
		entity.setPwd(MD5.md5crypt(entity.getPwd()));
		return super.saveT(entity);
	}

	/**
	 * 发送短信验证码
	 * @param phone  手机号
	 * @param code  验证码
	 * @return
	 */
	@RequestMapping("/sendSMS")
	public @ResponseBody String sendSMS(String phone,String code){
		try {
			return SmsUtil.sendSms(phone,code);
		} catch (ClientException e) {
			e.printStackTrace();
			return ERROR;
		}
	}

	/**
	 * 发送邮件
	 * @param email  邮箱地址
	 * @param msg  是否注册成功
	 * @return
	 */
	@RequestMapping("/sendEmail")
	public @ResponseBody String sendEmail(String email,String msg){
		return HtmlMail.sendEmail(email,msg);  //调用工具类HtmlMail中发送邮件的方法
	}
}
