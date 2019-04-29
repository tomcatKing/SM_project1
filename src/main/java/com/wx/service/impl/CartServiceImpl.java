package com.wx.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.wx.common.JsonResult;
import com.wx.common.RedisOperator;
import com.wx.mapper.CartMapper;
import com.wx.mapper.FoodMapper;
import com.wx.mapper.UserMapper;
import com.wx.model.WxSessionModel;
import com.wx.pojo.Cart;
import com.wx.pojo.Food;
import com.wx.service.ICartService;
import com.wx.service.IUserService;
import com.wx.util.BigDecimalUtil;
import com.wx.vo.CartVo;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class CartServiceImpl implements ICartService {
	
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private RedisOperator redis;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private FoodMapper foodMapper;
	
	@Override
	public JsonResult insertCart(String code, Integer food_id, Integer food_num) {
		log.info("=======开始监控添加购物车行为========");
		if(StringUtils.isBlank(code) || food_id==null) {
			return JsonResult.errorMsg("参数传入异常");
		}
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			log.info("      当前用户不存在        ");
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel) jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//4.判断用户的购物车中是否已经存在这个商品
		Cart cart=cartMapper.getCartByFoodIdAndOpenId(open_id, food_id);
		if(cart!=null) {
			//5.如果存在这个商品,则更新这个商品的数量
			cart.setFood_num(cart.getFood_num()+food_num);
			log.info("触发更新购物车行为");
			cartMapper.updateCartBySelect(cart);
			
		}else {
			log.info("触发添加购物车行为");
			//6.如果不存在这个商品,则创建这条记录到购物车
			cartMapper.insertCart(open_id, food_id, food_num);
		}
		log.info("=======结束监控添加购物车行为========");
		return JsonResult.ok();
	}
	
	@Override
	public JsonResult list(String code,Integer[] cart_ids) {
		log.info("========小程序用户获取指定的购物车=======");
		if(StringUtils.isBlank(code)) {
			return JsonResult.errorMsg("参数传入异常");
		}
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		List<Cart> cartList=cartMapper.getCartByOpenIdAndCartIds(wxSessionModel.getOpenid(),cart_ids);
		List<CartVo> cartVoList=getCartVoList(cartList);
		return JsonResult.ok(cartVoList);
	}
	
	@Override
	public JsonResult list(String code) {
		log.info("========小程序用户获取所有的购物车=======");
		if(StringUtils.isBlank(code)) {
			return JsonResult.errorMsg("参数传入异常");
		}
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//4.根据open_id获取所有的所有购物车记录
		List<Cart> cartList=cartMapper.getCartsByOpenId(open_id);
		List<CartVo> cartVoList=getCartVoList(cartList);
		return JsonResult.ok(cartVoList);
	}
	
	//通过传入的购物车列表获取购物信息详情
	private List<CartVo> getCartVoList(List<Cart> cartList){
		log.info("========小程序用户查看购物车详情=======");
		List<CartVo> cartVoList=new ArrayList<CartVo>();
		BigDecimal cartTotalPrice = new BigDecimal("0");
		//如果传入数组不为null
		if(!CollectionUtils.isEmpty(cartList)) {
			//循环遍历购物车
			for (Cart cart : cartList) {
				CartVo cartVo=new CartVo();
				cartVo.setCart_id(cart.getCart_id());
				Integer food_id=cart.getFood_id();
				cartVo.setFood_id(food_id);
				
				Food food=foodMapper.getFoodById(food_id);
				if(food!=null) {
					cartVo.setFood_img(food.getFood_img());
					cartVo.setFood_name(food.getFood_name());
					cartVo.setFood_num(cart.getFood_num());
					cartVo.setFood_name(food.getFood_name());
					cartVo.setFood_price(food.getFood_price());
				}
				//计算总价
				cartVo.setTotalPrice(BigDecimalUtil.mul(food.getFood_price().doubleValue(), cart.getFood_num()));
				cartVoList.add(cartVo);
			}
		}
		return cartVoList;
	}
	
	//更新购物车中的商品的数量
	@Override
	public JsonResult update(String code, Integer cart_id, Integer food_num) {
		log.info("========小程序用户更新购物车=======");
		if(StringUtils.isBlank(code) || cart_id==null || food_num==null) {
			return JsonResult.errorMsg("传入参数异常");
		}
		
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//4.判断用户是否存在这个订单
		Cart cart=cartMapper.getCartByOpenIdAndCartId(open_id, cart_id);
		if(cart==null) {
			//5.如果购物车没有这个记录
			return JsonResult.errorMsg("用户没有这个记录");
		}else {
			//6.修改购物车中的商品数量
			cart.setFood_num(food_num);
			cartMapper.updateCartBySelect(cart);
			return JsonResult.ok();
		}
	}

	@Override
	public JsonResult delete(String code, Integer[] food_ids) {
		log.info("========小程序用户删除购物车=======");
		if(StringUtils.isBlank(code) || food_ids==null || food_ids.length==0) {
			return JsonResult.errorMsg("传入参数异常");
		}
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		for(Integer food_id:food_ids) {
			//4.判断用户的购物车中是否已经存在这个商品
			Cart cart=cartMapper.getCartByFoodIdAndOpenId(open_id, food_id);
			if(cart!=null) {
				//5.如果存在这个商品,则删除这个订单
				cartMapper.deleteCart(cart.getCart_id());
				
			}
		}
		return JsonResult.ok();
	}
	
	
}
