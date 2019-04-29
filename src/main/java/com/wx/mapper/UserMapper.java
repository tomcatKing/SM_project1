package com.wx.mapper;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
	//判断用户是否已经存在数据库中
	int getUserExists(String open_id);
	
	//保存微信用户到数据库中
	int insertUser(@Param("open_id")String open_id,@Param("user_img")String user_img);
}
