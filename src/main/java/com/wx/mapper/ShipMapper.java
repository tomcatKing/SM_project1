package com.wx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.pojo.Shipping;

public interface ShipMapper {
	//插入收货地址
	int insertShip(Shipping shipping);
	
	//获取指定的收货地址
	List<Shipping> getShips(String open_id);
	
	int selectShips(String open_id);
	
	//判断用户是否存在这个收货地址
	int selectShip(
			@Param("open_id")String open_id,
			@Param("shipping_id")Integer shipping_id);
	
	//获取指定的收货地址
	Shipping getShip(Integer shipping_id);
	
	//删除用户的指定收货地址
	int deleteShip(
			@Param("open_id")String open_id,
			@Param("shipping_id")Integer shipping_id);
	
	//更新收货地址
	int updateShip(
			Shipping shipping
			);
}	
