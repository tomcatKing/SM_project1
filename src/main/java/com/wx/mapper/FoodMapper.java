package com.wx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.pojo.Food;

public interface FoodMapper {
	//获取所有美食列表,并按要求排序
	List<Food> getFoods(@Param("orderBy")String orderBy);
	
	//获取指定的美食
	List<Food> getFoodListByKeyWordAndType(@Param("keyword")String keyword,@Param("type")Integer type,@Param("orderBy")String orderBy);
	
	//获取指定分类的美食,并排序
	List<Food> getFoodListByType(@Param("type")Integer type,@Param("orderBy")String orderBy);
	
	//通过关键字获取美食,并排序
	List<Food> getFoodListByKeyWord(@Param("keyword")String keyword,@Param("orderBy")String orderBy);
	
	//通过id获取美食的详细信息
	Food getFoodById(Integer food_id);
	
}
