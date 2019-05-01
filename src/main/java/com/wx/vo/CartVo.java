package com.wx.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class CartVo {
	private Integer cart_id;
    private Integer food_id;
    private Integer food_num;
    private String food_name;
    private String food_img;
    private BigDecimal food_price;
    private BigDecimal TotalPrice;
	public Integer getCart_id() {
		return cart_id;
	}
	public void setCart_id(Integer cart_id) {
		this.cart_id = cart_id;
	}
	public Integer getFood_id() {
		return food_id;
	}
	public void setFood_id(Integer food_id) {
		this.food_id = food_id;
	}
	public Integer getFood_num() {
		return food_num;
	}
	public void setFood_num(Integer food_num) {
		this.food_num = food_num;
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
	public BigDecimal getTotalPrice() {
		return TotalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		TotalPrice = totalPrice;
	}
    
    
}
