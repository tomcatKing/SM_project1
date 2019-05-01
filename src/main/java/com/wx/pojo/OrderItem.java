package com.wx.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class OrderItem {
	private Integer order_item_id;
	private String open_id;
	private Long order_no;
	private Integer food_id;
	private String food_name;
	private String food_img;
	private BigDecimal food_price;
	private Integer food_num;
	private BigDecimal total_price;
	private Date create_time;
	private Date update_time;
	public Integer getOrder_item_id() {
		return order_item_id;
	}
	public void setOrder_item_id(Integer order_item_id) {
		this.order_item_id = order_item_id;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
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
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	
	
	
}
