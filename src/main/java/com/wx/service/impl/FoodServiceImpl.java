package com.wx.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wx.common.JsonResult;
import com.wx.mapper.FoodMapper;
import com.wx.pojo.Food;
import com.wx.service.IFoodService;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class FoodServiceImpl implements IFoodService{
	@Autowired
	private FoodMapper foodMapper;
	
	@Override
	public JsonResult list(String keyword, Integer type, int pageNum, int pageSize, String orderBy) {
		if(StringUtils.isBlank(keyword) && type==null) {
			return getFoodList(pageNum,pageSize,orderBy);
		}
		log.info("===========微信小程序获取指定类型的美食============");
		//如果传入了类型参数(1-甜食,2-瓜果,3-蛋糕,4-饮料)
		if(type!=null) {
			if(StringUtils.isBlank(keyword)) {
				//如果没有这个关键字
				PageHelper.startPage(pageNum,pageSize);
				List<Food> foodList=foodMapper.getFoodListByType(type,orderBy);
				PageInfo pageResult=new PageInfo(foodList);
				pageResult.setList(foodList);
				return JsonResult.ok(pageResult);
			}else {
				//如果存在关键字
				PageHelper.startPage(pageNum,pageSize);
				StringBuilder sb=new StringBuilder();
				//创建模糊搜索关键字
				keyword=sb.append("%").append(keyword).append("%").toString();
				List<Food> foodList=foodMapper.getFoodListByKeyWordAndType(keyword,type,orderBy);
				PageInfo pageResult=new PageInfo(foodList);
				pageResult.setList(foodList);
				return JsonResult.ok(pageResult);
			}
		}else {
			//类型为空的情况下,就只有关键字不为null
			StringBuilder sb=new StringBuilder();
			keyword=sb.append("%").append(keyword).append("%").toString();
			List<Food> foodList=foodMapper.getFoodListByKeyWord(keyword, orderBy);
			PageInfo pageResult=new PageInfo(foodList);
			pageResult.setList(foodList);
			return JsonResult.ok(pageResult);
		}
	}
	
	//获取多有美食,不包含关键子和美食类型
	private JsonResult getFoodList(int pageNum, int pageSize,String orderBy) {
		log.info("===========微信小程序获取指定类型的美食============");
		//从第pageNum页开始,一页pageSize个数据
		PageHelper.startPage(pageNum,pageSize);
		List<Food> foodList=foodMapper.getFoods(orderBy);
		//处理结果
		PageInfo pageResult=new PageInfo(foodList);
		//返回结果
		pageResult.setList(foodList);
		return JsonResult.ok(pageResult);
	}

	@Override
	public JsonResult food(Integer food_id) {
		if(food_id==null) {
			return JsonResult.errorMsg("传入参数异常!!");
		}
		log.info("===========微信小程序获取美食详情============");
		Food food=foodMapper.getFoodById(food_id);
		return JsonResult.ok(food);		
	}
	
	
}
