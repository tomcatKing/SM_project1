package com.wx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.pojo.Cart;

public interface CartMapper {
	//创建订单
	Integer insertCart(
			@Param("open_id")String open_id,
			@Param("food_id")Integer food_id,
			@Param("food_num")Integer food_num);
	
	
	//判断用户的购物车是否存在指定的记录
	Cart getCartByFoodIdAndOpenId(
			@Param("open_id")String open_id,
			@Param("food_id")Integer food_id);
	
	//更新用户的购物车
	int updateCartBySelect(Cart cart);
	
	//通过用户的open_id获取用户的所有购物车
	List<Cart> getCartsByOpenId(String open_id);
	
	//判断用户是否存在这个购物车
	Cart getCartByOpenIdAndCartId(
			@Param("open_id")String open_id,
			@Param("cart_id")Integer cart_id);
	
	//根据购物车的id删除购物车
	int deleteCart(Integer cart_id);

	//通过传入多个购物车的id和来查询购物车
	List<Cart> getCartByOpenIdAndCartIds(
			@Param("open_id")String open_id, 
			@Param("cart_ids")Integer[] cart_ids);
}
