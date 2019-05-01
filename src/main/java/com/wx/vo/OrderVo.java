package com.wx.vo;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.wx.pojo.Shipping;

import lombok.Data;

@Data
@JsonInclude(value=Include.NON_NULL)
public class OrderVo {
	private Long order_no;
	private BigDecimal payment;
	private String payment_type;
	private String order_desc;
	private Integer status;
	private String status_desc;
	private String payment_time;
	private String send_time;
	private String end_time;
	private String close_time;
	private String create_time;
	//订单明细
	private List<OrderItemVo> orderItemList;
	//购物车图片
	private String imageHost;
	//收货地址id
	private Integer shipping_id;
	//具体收货地址
	private Shipping shipping;
	
	
}
