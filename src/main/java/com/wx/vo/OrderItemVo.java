package com.wx.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class OrderItemVo {
	private Long order_no;
    private Integer food_id;
    private String food_name;
    private String food_img;
    private BigDecimal food_price;
    private Integer food_num;
    private BigDecimal total_price;
    private Date create_time;
	public Long getOrder_no() {
		return order_no;
	}
	public void setOrder_no(Long order_no) {
		this.order_no = order_no;
	}
	public Integer getFood_id() {
		return food_id;
	}
	public void setFood_id(Integer food_id) {
		this.food_id = food_id;
	}
	public String getFood_name() {
		return food_name;
	}
	public void setFood_name(String food_name) {
		this.food_name = food_name;
	}
	public String getFood_img() {
		return food_img;
	}
	public void setFood_img(String food_img) {
		this.food_img = food_img;
	}
	public BigDecimal getFood_price() {
		return food_price;
	}
	public void setFood_price(BigDecimal food_price) {
		this.food_price = food_price;
	}
	public Integer getFood_num() {
		return food_num;
	}
	public void setFood_num(Integer food_num) {
		this.food_num = food_num;
	}
	public BigDecimal getTotal_price() {
		return total_price;
	}
	public void setTotal_price(BigDecimal total_price) {
		this.total_price = total_price;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
    
}
