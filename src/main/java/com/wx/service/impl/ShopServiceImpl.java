package com.wx.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wx.common.JsonResult;
import com.wx.common.RedisOperator;
import com.wx.mapper.ShipMapper;
import com.wx.mapper.UserMapper;
import com.wx.model.WxSessionModel;
import com.wx.pojo.Shipping;
import com.wx.service.IShopService;
import com.wx.service.IUserService;

import lombok.extern.log4j.Log4j;
	
@Service
@Log4j
public class ShopServiceImpl implements IShopService{
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private RedisOperator redis;
	
	@Autowired
	private UserMapper userMapper;
	
	@Autowired
	private ShipMapper shipMapper;
	
	@Override
	public JsonResult add(String code, Shipping shipping) {
		if(StringUtils.isBlank(code) || shipping==null) {
			return JsonResult.errorMsg("传入参数异常");
		}
		log.info("=======小程序用户添加收货地址========");
		
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//4.插入数据到数据库
		shipping.setOpen_id(open_id);
		int shipping_id=shipMapper.insertShip(shipping);
		if(shipping_id>0) {
			//插入记录成功
			return JsonResult.ok();
		}else {
			return JsonResult.errorMsg("添加收货地址失败");
		}
		
	}

	@Override
	//获取用户的收货地址列表
	public JsonResult list(String code, int pageNum, int pageSize) {
		if(StringUtils.isBlank(code)) {
			return JsonResult.errorMsg("传入参数异常");
		}
		log.info("=========获取所有的收货地址=========");
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//4.获取用户的所有收货地址列表,并分页
		PageHelper.startPage(pageNum, pageSize);
		List<Shipping> shippingList=shipMapper.getShips(open_id);
		PageInfo pageInfo=new PageInfo(shippingList);
		System.out.println("查询出来的收货地址表:"+shippingList.size());
		return JsonResult.ok(pageInfo);
	}

	@Override
	public JsonResult delete(String code, Integer shippingId) {
		if(StringUtils.isBlank(code) || shippingId==null) {
			return JsonResult.errorMsg("传入参数异常");
		}
		log.info("==========删除收货地址=======");
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//4.判断用户是否存在这个订单
		int count=shipMapper.selectShip(open_id, shippingId);
		if(!(count>0)) {
			return JsonResult.errorMsg("用户没有这个收货地址");
		}
		//5.删除这个订单
		count=shipMapper.deleteShip(open_id, shippingId);
		if(count>0) {
			return JsonResult.ok();
		}
		return JsonResult.errorMsg("删除收货地址失败");
	}

	@Override
	//更新收货地址
	public JsonResult update(String code, Shipping shipping) {
		if(StringUtils.isBlank(code) || shipping==null) {
			return JsonResult.errorMsg("传入参数异常");
		}
		log.info("===========更新收货地址==============");
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//4.判断用户是否存在这个订单
		int count=shipMapper.selectShip(open_id, shipping.getShipping_id());
		if(!(count>0)) {
			return JsonResult.errorMsg("用户没有这个收货地址");
		}
		count=shipMapper.updateShip(shipping);
		if(count>0) {
			return JsonResult.ok();
		}
		return JsonResult.errorMsg("更新收货地址失败");
	}
	
	
	
}
