package com.wx.vo;

import com.wx.pojo.Shipping;

public class TempShippingBean {
	private String code;
	private Shipping shipping;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Shipping getShipping() {
		return shipping;
	}
	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
	@Override
	public String toString() {
		return "TempShippingBean [code=" + code + ",shipping="+shipping+"]";
	}
	
}
