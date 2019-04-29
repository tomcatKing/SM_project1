package com.wx.service;

import com.wx.common.JsonResult;

public interface ICartService {
	//传入用户验证码,美食id,美食数量
	JsonResult insertCart(String code,Integer food_id,Integer food_num);
	
	//根据传入的用户的验证码,获取用户的购物车
	JsonResult list(String code);
	
	//根据传入的用户的验证码,与指定的购物车id来获取指定的用户
	JsonResult list(String code,Integer[] cart_ids);
	
	//更新购物车中的指定商品的数量
	JsonResult update(String code,Integer cart_id,Integer food_num);
	
	//删除购物车中的多个商品
	JsonResult delete(String code,Integer[] food_ids);
	
}
