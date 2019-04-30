package com.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wx.common.JsonResult;
import com.wx.service.ICartService;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/cart")
@Log4j
public class CartController {
	@Autowired
	private ICartService iCartService;
	
	//添加商品到购物车
	@RequestMapping("/add")
	public JsonResult add(
			@RequestParam(name="code")String code,
			@RequestParam(name="food_id")Integer food_id,
			@RequestParam(name="food_num",required=false,defaultValue="1")Integer food_num) {
		log.info("小程序用户添加购物车,food_id->"+food_id+",food_num->"+food_num);
		System.out.println(food_id+","+code+","+food_num);
		return iCartService.insertCart(code, food_id, food_num);
	}
	
	//获取用户的购物车
	@RequestMapping("/list")
	public JsonResult list(
			@RequestParam(name="code")String code) {
		log.info("小程序用户查看购物车");
		return iCartService.list(code);
	}
	
	//获取指定的用户的购物车
	//1,2,3
	@RequestMapping("/bylist")
	public JsonResult list(
			String code,String cart_ids) {
		log.info("小程序用户查看指定的购物车");
		if(!cart_ids.contains(",")) {
			Integer[] foods= {Integer.parseInt(cart_ids.trim())};
			return iCartService.list(code, foods);
		}else {
			String[] f_ids=cart_ids.split(",");
			Integer[] foods=new Integer[f_ids.length];
			for(int i=0;i<f_ids.length;i++) {
				foods[i]=Integer.parseInt(f_ids[i].trim());
			}
			return iCartService.list(code, foods);
		}
	}
	
	//更新购物车
	@RequestMapping("/update")
	public JsonResult update(
			String code,
			Integer cart_id,
			Integer food_num) {
		log.info("小程序用户更新购物车,cart_id->"+cart_id+",food_num->"+food_num);
		return iCartService.update(code,cart_id,food_num);
	}
	
	
	//删除用户的多个购物车
	/**
	 * @param foodId 这里与前端约定,以','号分割要删除的产品的id.比如"1,2,3"
	 * @return
	 */
	@RequestMapping("/delete")
	public JsonResult delCart(
			String code,
			String food_ids) {
		
		log.info("小程序用户删除购物车,food_ids->"+food_ids);
		//1,2,3...如果用户只传入了1个1
		if(!food_ids.contains(",")) {
			Integer[] foods= {Integer.parseInt(food_ids.trim())};
			return iCartService.delete(code, foods);
		}else {
			String[] f_ids=food_ids.split(",");
			Integer[] foods=new Integer[f_ids.length];
			for(int i=0;i<f_ids.length;i++) {
				foods[i]=Integer.parseInt(f_ids[i].trim());
			}
			return iCartService.delete(code, foods);
		}
		
	}
	
}
