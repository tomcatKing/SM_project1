package com.wx.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.wx.common.JsonResult;
import com.wx.service.IOrderService;
import com.wx.util.Const;

import lombok.extern.log4j.Log4j;

@RestController
@RequestMapping("/order")
@Log4j
public class OrderController {
	@Autowired
	private IOrderService iOrderService;
	
	//创建订单,传入code,cart_ids(1,2,3),shipping_id,返回一个orderNo
	@RequestMapping("/add")
	public JsonResult add(
			String code,
			String cart_ids,
			Integer shipping_id,
			@RequestParam(value="order_desc",defaultValue="用户没有留下信息",required=false)String order_desc) {
		log.info("小程序用户添加订单,code->"+code+",cart_ids->"+cart_ids+",shipping_id->"+shipping_id);
		System.out.println(code+"\\"+cart_ids+"\\"+shipping_id+"\\"+order_desc);
		
		return iOrderService.add(code,order_desc,cart_ids,shipping_id);
	}
	
	//取消订单接口
	@RequestMapping("/cancel")
	public JsonResult cancel(
			String code,
			Long order_no) {
		log.info("小程序用户取消订单,取消的order_no是->"+order_no);
		return iOrderService.cancel(code, order_no);
	}
	
	@RequestMapping("/pay")
	//当面付
	public JsonResult pay(HttpServletRequest request,Long orderNo,String code) {
		log.info("小程序用户支付订单,orderNo->"+orderNo+",code->"+code);
		//获取相对路径
		String path=request.getSession().getServletContext().getRealPath("upload");
		return iOrderService.pay(orderNo,code,path);
	}
	
	//查看订单详情
	@RequestMapping("/detail")
	public JsonResult detail(
			String code,
			Long order_no) {
		log.info("小程序用户查看订单详情,code->"+code+",order_no"+order_no);
		return iOrderService.detail(code,order_no);
	}
	
	//查看所有的订单
	@RequestMapping("/list")
	public JsonResult list(String code) {
		log.info("小程序用户查看所有的顶单,code->"+code);
		return iOrderService.list(code,null);
	}
	
	//查看所有未支付订单
	@RequestMapping("/nopaylist")
	public JsonResult noPay(String code) {
		log.info("小程序用户查询所有未支付订单");
		return iOrderService.list(code,Const.OrderStatusEnum.NO_PAY.getCode());
	}
	
	//查看所有待发货的订单
	@RequestMapping("/ispaylist")
	public JsonResult isPay(String code) {
		log.info("小程序用户查询所有未支付订单");
		return iOrderService.list(code, Const.OrderStatusEnum.PAID.getCode());
	}
	
	//查看所有待收货的订单
	@RequestMapping("/isshiplist")
	public JsonResult isSuccess(String code) {
		log.info("小程序用户查询所有未支付订单");
		return iOrderService.list(code,Const.OrderStatusEnum.SHIPPED.getCode());
	}
	
	//支付宝回调
	@RequestMapping("/alipay_callback")
	public Object alipayCallback(HttpServletRequest request) {
		log.info("支付宝回调了");
		Map<String,String> params=new HashMap<>();
		
		//支付宝支付成功后会通过request返回给服务器
		Map requestParams=request.getParameterMap();
		for(Iterator iterator=requestParams.keySet().iterator();iterator.hasNext();) {
			String name=(String)iterator.next();
			String[] values=(String[])requestParams.get(name);
			String valStr="";
			for(int i=0;i<values.length;i++) {
				valStr=(i==values.length-1)?valStr+values[i]:valStr+values[i]+",";
				//如果不是最后一个元素,则用逗号,最后一个不添加逗号
			}
			params.put(name, valStr);
		}
		log.info("支付宝回调,sign->"+params.get("sign")+",trade_status->"+params.get("trade_status")+",参数->"+params.toString());
		//验证这个回调是否为支付宝的,并且避免重复通知
		
		//支付宝官方提示:必须移除sign_type和sign,默认移除了sign,手动移除sign_type
		params.remove("sign_type"); //charset默认为"utf-8"
		try {
			boolean alipayRSACheckedV2=AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
			if(!alipayRSACheckedV2) {
				log.info("支付宝验签失败!!");
				return JsonResult.errorMsg("非法请求,验证不通过!!");
			}
			log.info("支付宝验签成功,开始执行操作!!");
			//更新订单状态
			Long orderNo=Long.valueOf(params.get("out_trade_no"));
			iOrderService.updateOrderStatus(orderNo, Const.OrderStatusEnum.PAID.getCode());
			
		} catch (AlipayApiException e) {
			log.error("支付宝验证回调异常",e);
		}	
		
		JsonResult jsonResult=iOrderService.aliCallback(params);
		if(jsonResult.isOK()) {
			log.info("订单支付完成");
			return Const.AlipayCallback.RESPONSE_SUCCESS;
		}
		return Const.AlipayCallback.RESPONSE_FAILED;
	}
	
	@RequestMapping("/queryOrderPayStatus")
	//获取订单支付状态
	public JsonResult queryOrderPayStatus(String code,Long order_no) {
		log.info("小程序用户查询订单状态,order_no->"+order_no);
		JsonResult serverResponse= iOrderService.queryOrderPayStatus(code, order_no);
		if(serverResponse.isOK()) {
			return JsonResult.ok();
		}
		return JsonResult.errorMsg("订单未支付!!");
	}
	
}
