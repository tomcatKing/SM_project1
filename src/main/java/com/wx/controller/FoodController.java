package com.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wx.common.JsonResult;
import com.wx.service.IFoodService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/food")
@Log4j
public class FoodController {
	
	@Autowired
	private IFoodService iFoodService;
	
	@RequestMapping("/list")
	public JsonResult list(@RequestParam(value="keyword",required=false)String keyword,
			@RequestParam(value="type",required=false)Integer type,
			@RequestParam(value="pageNum",defaultValue="1")int pageNum,
			@RequestParam(value="pageSize",defaultValue="10")int pageSize,
			@RequestParam(value="orderBy",defaultValue="food_id")String orderBy) {
		log.info("小程序来获取美食信息了,传入参数keyword->"+keyword+",type->"+type
				+",pageNum->"+pageNum+",pageSize->"+pageSize+",orderBy->"+orderBy);
		return iFoodService.list(keyword,type,pageNum,pageSize,orderBy);
	}
	
	@RequestMapping("/{food_id}")
	public JsonResult detail(@PathVariable(value="food_id")Integer food_id) {
		log.info("小程序来获取美食详情了,传入参数food_id->"+food_id);
		return iFoodService.food(food_id);
	}
}
