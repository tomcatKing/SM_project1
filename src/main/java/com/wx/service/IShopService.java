package com.wx.service;

import com.wx.common.JsonResult;
import com.wx.pojo.Shipping;

public interface IShopService {
	JsonResult add(String code,Shipping shipping);
	
	JsonResult list(String code,int pageSize,int pageNum);
	
	JsonResult delete(String code, Integer shippingId);

	JsonResult update(String code, Shipping shipping);
}
