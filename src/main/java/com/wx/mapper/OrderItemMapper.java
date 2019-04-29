package com.wx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.wx.pojo.OrderItem;

public interface OrderItemMapper {
	//添加订单明细
	int insert(OrderItem orderItem);
	
	//批量生成订单明细
	void batchInsert(@Param("orderItemList") List<OrderItem> orderItemList);
	
	//通过订单的order_no获取所有的订单详情信息
	List<OrderItem> getOrderItemsByOrderNo(Long order_no);
	
	//通过订单编号和用户id获取订单详情列表
	List<OrderItem> getByOrderNoUserId(@Param("order_no")Long orderNo, @Param("open_id")String open_id);
}
