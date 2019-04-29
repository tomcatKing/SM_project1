package com.wx.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class Food {
	private Integer food_id;
	private Integer food_type;
	private String food_name;
	private String food_img;
	private String food_detail;
	private BigDecimal food_price;
	private Integer food_count;
	private Integer food_status;
	private Date create_time;
	private Date update_time;
	public Integer getFood_id() {
		return food_id;
	}
	public void setFood_id(Integer food_id) {
		this.food_id = food_id;
	}
	public Integer getFood_type() {
		return food_type;
	}
	public void setFood_type(Integer food_type) {
		this.food_type = food_type;
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
	public String getFood_detail() {
		return food_detail;
	}
	public void setFood_detail(String food_detail) {
		this.food_detail = food_detail;
	}
	
	public BigDecimal getFood_price() {
		return food_price;
	}
	public void setFood_price(BigDecimal food_price) {
		this.food_price = food_price;
	}
	public Integer getFood_count() {
		return food_count;
	}
	public void setFood_count(Integer food_count) {
		this.food_count = food_count;
	}
	public Integer getFood_status() {
		return food_status;
	}
	public void setFood_status(Integer food_status) {
		this.food_status = food_status;
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
