package com.java.register.utils;

import org.apache.commons.mail.HtmlEmail;
import java.util.Date;

public class HtmlMail {

	public static String sendEmail(String emailAddress,String msg){
		//新建可以读取html标签内容的邮件对象
		HtmlEmail email = new HtmlEmail();
		//新浪邮件服务器（用于发、收邮件的）POP3/POP是用来取邮件的
		email.setHostName("smtp.sina.com");
		//新浪邮件邮箱的登陆账号密码
		email.setAuthentication("yue_19960925", "yueke2511..");
		//把邮件的字符编码设置为UTF-8
		email.setCharset("UTF-8");
		try{
			//接收方的邮箱地址
			email.addTo(emailAddress);
			//必须和Authentication使用的用户相同，否则失败（邮箱名字）
			email.setFrom("yue_19960925@sina.com", "yue_19960925", "utf-8");// 设置发件人信息
			//发送的邮件标题
			email.setSubject("注册结果");
			//发送邮件时间
			email.setSentDate(new Date());
			//发送的邮件内容
			email.setHtmlMsg("易买网用户注册提示：<font size='20px' color='red'>"+msg+"</font>");
			//发送
			email.send();
			return "emailOk";
		}catch (Exception e){
			e.printStackTrace();
			return "emailFail";
		}
	}

}
