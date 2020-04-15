package com.java.sso.controller;

import com.java.model.WebUsersEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author yk
 *   WebUsers控制器
 * @date 2020-03-09 17:42:42
 */
@Controller
@RequestMapping("/webusers")
public class WebUsersController extends BaseController<WebUsersEntity>{

	@Autowired
	private RedisTemplate redisTemplate;

	/**
	 * 登录
	 * @param user  用户登录信息
	 * @return  登录结果
	 */
	@RequestMapping("/loginUser")
	public @ResponseBody Map<String,Object> loginUser(WebUsersEntity user, HttpServletResponse response){
		//新建Map集合
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			map = webUsersService.loginUser(user);
			Integer code = (Integer) map.get("code");
			if (code == 0){  //登录成功
				//获取令牌
				String token = (String) map.get("token");
				//新建一个cookie, 并将令牌装入cookie中
				Cookie cookie = new Cookie("token",token);
				//设置cookie的有效时间和路径
				cookie.setMaxAge(20*60);  //有效时间(20min)
				cookie.setPath("/webusers");  //路径
				//通过响应对象将cookie存放在客户端
				response.addCookie(cookie);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * 取出登录模块令牌
	 * @return
	 */
	@RequestMapping("/getToken")
	public @ResponseBody String getToken(HttpServletRequest request){
		//定义令牌
		String token = null;
		//得到客户端请求中的所有cookie数组
		Cookie[] cookies = request.getCookies();
		//判断数组是否为空
		if (cookies != null){
			//定义token的cookie
			Cookie cookieToken = null;
			//通过循环找出名字为token的cookie
			for (Cookie cookie:cookies){
				if ("token".equals(cookie.getName())){
					//将名字为token的cookie赋值给cookieToken
					cookieToken = cookie;
					break;
				}
			}
			//判断名字为token的cookie是否存在
			if (cookieToken != null){
				//将值token取出
				token = cookieToken.getValue();
			}
		}
		return token;
	}

	/**
	 * 根据登录的令牌取到redis中的用户数据
	 * @param token
	 * @return
	 */
	@RequestMapping("/getRedisLoginUser")
	public @ResponseBody WebUsersEntity getRedisLoginUser(String token){
		//获取redis中操作字符串的模板
		ValueOperations vop = redisTemplate.opsForValue();
		//定义登录用户
		WebUsersEntity loginUser = null;
		//根据token取到登录数据
		loginUser = (WebUsersEntity) vop.get("sessionId"+token);
		return loginUser;
	}

	/**
	 * 用户注销
	 * @return  结果
	 */
	@RequestMapping("/exitUser")
	public @ResponseBody Boolean exitUser(HttpServletRequest request, HttpServletResponse response){//删除令牌token
		//判断用户数据是否删除成功的变量
		Boolean delete = false;
		//定义令牌
		String token = null;
		//得到客户端请求中的所有cookie数组
		Cookie[] cookies = request.getCookies();
		//判断数组是否为空
		if (cookies != null){
			//定义token的cookie
			Cookie cookieToken = null;
			//通过循环找出名字为token的cookie
			for (Cookie cookie:cookies){
				if ("token".equals(cookie.getName())){
					//将名字为token的cookie赋值给cookieToken
					cookieToken = cookie;
					break;
				}
			}
			//判断名字为token的cookie是否存在
			if (cookieToken != null){
				//获取token
				token = cookieToken.getValue();
				//删除redis中的用户数据
				delete = redisTemplate.delete("sessionId"+token);  // true & false
				//清除装有token的cookie
				Cookie cookie = new Cookie("token","");
				cookie.setPath("/webusers");//此处与第49行代码保持一致，否则cookie清空会失败
				cookie.setMaxAge(0);  //有效时间设置为0
				response.addCookie(cookie);
			}
		}
		return delete;
	}

}
