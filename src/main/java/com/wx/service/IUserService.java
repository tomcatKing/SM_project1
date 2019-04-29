package com.wx.service;

import com.wx.common.JsonResult;
import com.wx.model.WxSessionModel;

public interface IUserService {
	//插入用户信息到数据库
	public JsonResult insertUser(String open_id,String user_img);
	
	//通用模板
	public JsonResult getUserId(String code);
	
	//获取用户的open_id和session_key
	WxSessionModel getSessionAndOpen(String code);
	
}
