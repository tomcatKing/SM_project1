package com.wx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wx.common.JsonResult;
import com.wx.pojo.Shipping;
import com.wx.service.IShopService;
import com.wx.vo.TempShippingBean;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/shipping")
@Log4j
public class ShippingController {
	@Autowired
	private IShopService iShopService;
	
	//添加收货地址
	@RequestMapping(value="/add",method=RequestMethod.POST)
	public JsonResult add(@RequestBody TempShippingBean tempShippingBean) {
		log.info("小程序用户添加收货地址,tempShippingBean->"+tempShippingBean);
		System.out.println(tempShippingBean);
		return iShopService.add(tempShippingBean.getCode(), tempShippingBean.getShipping());
	}
	
	//删除收货地址
	@RequestMapping("/delete")
	public JsonResult del(
			String code,
			Integer shippingId) {
		log.info("小程序用户删除收货地址,shippingId->"+shippingId);
		return iShopService.delete(code,shippingId);
	}
	
	//更新收货地址
	@RequestMapping("/update")
	public JsonResult update(
			String code,
			Shipping shipping) {
		log.info("小程序用户更新收货地址,shipping->"+shipping);
		return iShopService.update(code,shipping);
	}
	
	//获取用户的收货地址
	@RequestMapping("/list")
	public JsonResult list(
			String code,
			@RequestParam(value="pageNum",defaultValue="1",required=false)int pageNum,
			@RequestParam(value="pageSize",defaultValue="10",required=false)int pageSize) {
		log.info("小程序用户查看收货地址");
		return iShopService.list(code,pageNum,pageSize);
	}
}
