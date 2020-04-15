package com.java.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author yk
 *    WebUsers实体类
 * @date 2020-03-09 17:42:42
 */
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class WebUsersEntity implements Serializable{  //使此对象序列化, 便于用redis存储数据

	  private static final long serialVersionUID = 1L;
	
      //主键
	  private Integer id;
      //
	  private String uname;
      //用户名
	  private String username;
      //密码
	  private String pwd;
      //手机号
	  private String phone;
      //账号最新变动时间
      @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss" ,timezone = "GMT+8")
	  private Date updatetime;
      //用户头像
	  private String userheader;
	  //用户邮箱
	  private String email;

	  /**
	   * 设置：主键
	   */
	  public void setId(Integer id) {
		  this.id = id;
	  }
	  /**
	   * 获取：主键
	   */
	  public Integer getId() {
	   	  return id;
	  }
	  /**
	   * 设置：
	   */
	  public void setUname(String uname) {
		  this.uname = uname;
	  }
	  /**
	   * 获取：
	   */
	  public String getUname() {
	   	  return uname;
	  }
	  /**
	   * 设置：用户名
	   */
	  public void setUsername(String username) {
		  this.username = username;
	  }
	  /**
	   * 获取：用户名
	   */
	  public String getUsername() {
	   	  return username;
	  }
	  /**
	   * 设置：密码
	   */
	  public void setPwd(String pwd) {
		  this.pwd = pwd;
	  }
	  /**
	   * 获取：密码
	   */
	  public String getPwd() {
	   	  return pwd;
	  }
	  /**
	   * 设置：手机号
	   */
	  public void setPhone(String phone) {
		  this.phone = phone;
	  }
	  /**
	   * 获取：手机号
	   */
	  public String getPhone() {
	   	  return phone;
	  }
	  /**
	   * 设置：账号最新变动时间
	   */
	  public void setUpdatetime(Date updatetime) {
		  this.updatetime = updatetime;
	  }
	  /**
	   * 获取：账号最新变动时间
	   */
	  public Date getUpdatetime() {
	   	  return updatetime;
	  }
	  /**
	   * 设置：用户头像
	   */
	  public void setUserheader(String userheader) {
		  this.userheader = userheader;
	  }
	  /**
	   * 获取：用户头像
	   */
	  public String getUserheader() {
	   	  return userheader;
	  }

	  /**
	   * 设置：用户邮箱
	   */
	  public void setEmail(String email) {
		  this.email = email;
	  }

	  /**
	   * 获取：用户邮箱
	   */
	  public String getEmail() {
		  return email;
	  }


	@Override
	  public String toString() {
		  return  ReflectionToStringBuilder.toString(this);
	  }

}
