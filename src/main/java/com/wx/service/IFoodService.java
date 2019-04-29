package com.wx.service;

import com.wx.common.JsonResult;

public interface IFoodService {
	//获取指定的食物集合(关键字,类型,第几页,几十数据/页,排序方式)
	public JsonResult list(String keyword,Integer type,int pageNum,int pageSize,String orderBy);
	
	//获取指定食物的详细信息
	public JsonResult food(Integer food_id);
}
