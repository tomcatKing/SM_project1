package com.wx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.pojo.Order;

public interface OrderMapper {
	//添加订单
	int insert(Order order);
	
	//查询指定用户的指定订单
	Order selectOrderByOpenIdAndOrderNo(
			@Param("open_id")String open_id,
			@Param("order_no")Long order_no);
	
	//修改指定订单的状态
	int updateByPrimaryKeySelective(Order order);
	
	//通过订单的编号获取订单
	Order selectOrderByOrderNo(Long order_no);
	
	//查询用户的所有订单
	List<Order> list(
			@Param("open_id")String open_id,
			@Param("type")Integer type);
	
	//通过用户id和订单号获取订单信息
	Order selectByUserIdAndOrderNo(@Param("open_id")String open_id,@Param("order_no")Long orderNo);
	
	//修改订单状态
	void updateOrderStatus(Long orderNo, Integer orderStatus);
	
}
