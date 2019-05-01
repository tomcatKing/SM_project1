package com.wx.service;

import java.util.Map;

import com.wx.common.JsonResult;

public interface IOrderService {
	JsonResult add(String code,String order_desc,String cart_ids,Integer shipping_id);

	JsonResult cancel(String code, Long order_no);

	JsonResult detail(String code, Long order_no);

	JsonResult list(String code,Integer type);

	JsonResult pay(Long orderNo,String code,String path);
	
	//支付宝当面付回调
	JsonResult aliCallback(Map<String,String> params);
	
	//支付宝回调后修改订单状态
	void updateOrderStatus(Long orderNo,Integer OrderStatus);
	
	//查询用户的订单是否存在
	JsonResult queryOrderPayStatus(String code,Long orderNo);
	
	//关闭订单操作
	void closeOrder();
}
