package com.wx.pojo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
@JsonInclude(value=Include.NON_NULL)
public class Shipping {
	private Integer shipping_id;
	private String open_id;
	private String receiver_name;
	private String receiver_phone;
	private String receiver_province;
	private String receiver_city;
	private String receiver_district;
	private String receiver_address;
	private Date create_time;
	private Date update_time;
	public Integer getShipping_id() {
		return shipping_id;
	}
	public void setShipping_id(Integer shipping_id) {
		this.shipping_id = shipping_id;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public String getReceiver_name() {
		return receiver_name;
	}
	public void setReceiver_name(String receiver_name) {
		this.receiver_name = receiver_name;
	}
	public String getReceiver_phone() {
		return receiver_phone;
	}
	public void setReceiver_phone(String receiver_phone) {
		this.receiver_phone = receiver_phone;
	}
	public String getReceiver_province() {
		return receiver_province;
	}
	public void setReceiver_province(String receiver_province) {
		this.receiver_province = receiver_province;
	}
	public String getReceiver_city() {
		return receiver_city;
	}
	public void setReceiver_city(String receiver_city) {
		this.receiver_city = receiver_city;
	}
	public String getReceiver_district() {
		return receiver_district;
	}
	public void setReceiver_district(String receiver_district) {
		this.receiver_district = receiver_district;
	}
	public String getReceiver_address() {
		return receiver_address;
	}
	public void setReceiver_address(String receiver_address) {
		this.receiver_address = receiver_address;
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
	@Override
	public String toString() {
		return "Shipping [shipping_id=" + shipping_id + ", open_id=" + open_id + ", receiver_name=" + receiver_name
				+ ", receiver_phone=" + receiver_phone + ", receiver_province=" + receiver_province + ", receiver_city="
				+ receiver_city + ", receiver_district=" + receiver_district + ", receiver_address=" + receiver_address
				+ ", create_time=" + create_time + ", update_time=" + update_time + "]";
	}
	
}
