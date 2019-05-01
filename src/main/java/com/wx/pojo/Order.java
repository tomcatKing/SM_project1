package com.wx.pojo;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value=Include.NON_NULL)
public class Order {
	private Integer order_id;
	private Long order_no;
	private String open_id;
	private Integer shipping_id;
	private BigDecimal payment;
	private Integer payment_type;
	private Integer status;
	private String order_desc;
	private Date payment_time;
	private Date send_time;
	private Date end_time;
	private Date close_time;
	private Date update_time;
	private Date create_time;
	public Integer getOrder_id() {
		return order_id;
	}
	public void setOrder_id(Integer order_id) {
		this.order_id = order_id;
	}
	public Long getOrder_no() {
		return order_no;
	}
	public void setOrder_no(Long order_no) {
		this.order_no = order_no;
	}
	public String getOpen_id() {
		return open_id;
	}
	public void setOpen_id(String open_id) {
		this.open_id = open_id;
	}
	public Integer getShipping_id() {
		return shipping_id;
	}
	public void setShipping_id(Integer shipping_id) {
		this.shipping_id = shipping_id;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	public Integer getPayment_type() {
		return payment_type;
	}
	public void setPayment_type(Integer payment_type) {
		this.payment_type = payment_type;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getOrder_desc() {
		return order_desc;
	}
	public void setOrder_desc(String order_desc) {
		this.order_desc = order_desc;
	}
	public Date getPayment_time() {
		return payment_time;
	}
	public void setPayment_time(Date payment_time) {
		this.payment_time = payment_time;
	}
	public Date getSend_time() {
		return send_time;
	}
	public void setSend_time(Date send_time) {
		this.send_time = send_time;
	}
	public Date getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Date end_time) {
		this.end_time = end_time;
	}
	public Date getClose_time() {
		return close_time;
	}
	public void setClose_time(Date close_time) {
		this.close_time = close_time;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public Date getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
	
	
}
