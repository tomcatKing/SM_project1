package com.wx.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wx.common.Basic;
import com.wx.common.HttpClientUtil;
import com.wx.common.JsonResult;
import com.wx.common.JsonUtils;
import com.wx.common.RedisOperator;
import com.wx.mapper.UserMapper;
import com.wx.model.WxSessionModel;
import com.wx.service.IUserService;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class UserServiceImpl implements IUserService {
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisOperator redis;
	
	@Override
	public JsonResult insertUser(String open_id, String user_img) {
		if(StringUtils.isBlank(open_id) || StringUtils.isBlank(user_img)) {
			return JsonResult.errorMsg("传入参数错误!!");
		}
		Integer count=userMapper.getUserExists(open_id);
		if(count>0) {
			//用户已经存在数据库
			return JsonResult.errorMsg("此用户已被使用");
		}
		count=userMapper.insertUser(open_id, user_img);
		if(count>0) {
			log.info("来了1个新用户");
			return JsonResult.ok();
		}
		return JsonResult.errorMsg("添加用户信息失败");
	}

	@Override
	public JsonResult getUserId(String code) {
		log.info("获取用户信息");
		WxSessionModel wxSession=getSessionAndOpen(code);
		String open_id=wxSession.getOpenid();
		
		//2.判断redis中是否存在这个open_id的或者是否过期,如果过期,提醒用户,要去重新授权(code:502)
		String wx_result=redis.get(Basic.WX_SESSION_PREFIX+open_id);
		if(wx_result==null) {
			log.info("redis中不存在这个用户信息");
			return JsonResult.errorTokenMsg("用户信息已过期,请重新授权");
		}
		
		//3.判断用户是否存在数据库,如果不存在,就返回一个用户不存在的错误(code:500)
		int rowcount=userMapper.getUserExists(open_id);
		if(!(rowcount>0)) {
			log.info("数据库中没有这个用户的信息");
			return JsonResult.errorTokenMsg("用户信息已过期,请重新授权");
		}
		return JsonResult.ok(wxSession);
	}

	@Override
	public WxSessionModel getSessionAndOpen(String code) {
		//访问微信链接,传入appid和code等参数
		Map<String,String> param=new HashMap<>();
		param.put("appid", Basic.WX_APPID);
		param.put("secret", Basic.WX_SECRET);
		param.put("js_code", code);
		param.put("grant_type", Basic.WX_GRANT_TYPE);
		
		String result=HttpClientUtil.doGet(Basic.WX_URL,param);
		WxSessionModel wxSession=JsonUtils.jsonToPojo(result, WxSessionModel.class);
		return wxSession;
	}

}
