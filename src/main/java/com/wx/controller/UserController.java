package com.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wx.common.Basic;
import com.wx.common.JsonResult;
import com.wx.common.RedisOperator;
import com.wx.model.WxSessionModel;
import com.wx.service.IUserService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/user")
@Log4j
public class UserController {
	@Autowired
	private RedisOperator redis;
	
	@Autowired
	private IUserService userService; 
	
	@PostMapping("/login")
	public JsonResult login(String code,String img) {
		log.info("小程序授权登录了!!code-->"+code+",img-->"+img);
		//获取用户的session_key和open_id
		WxSessionModel wxSession=userService.getSessionAndOpen(code);
		
		//检查open_id是否已经存在数据库,如果存在,就不保存,否则保存微信用户到数据库
		JsonResult jsonResult=userService.insertUser(wxSession.getOpenid(), img);
		
		redis.set(Basic.WX_SESSION_PREFIX+wxSession.getOpenid(), wxSession.getSession_key(),1000 * 60 * 60);
		return jsonResult;
	}
	
	
}
