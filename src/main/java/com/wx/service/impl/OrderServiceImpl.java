package com.wx.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alipay.api.AlipayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.wx.common.JsonResult;
import com.wx.mapper.CartMapper;
import com.wx.mapper.FoodMapper;
import com.wx.mapper.OrderItemMapper;
import com.wx.mapper.OrderMapper;
import com.wx.mapper.PayInfoMapper;
import com.wx.mapper.ShipMapper;
import com.wx.model.WxSessionModel;
import com.wx.pojo.Cart;
import com.wx.pojo.Food;
import com.wx.pojo.Order;
import com.wx.pojo.OrderItem;
import com.wx.pojo.PayInfo;
import com.wx.pojo.Shipping;
import com.wx.service.IOrderService;
import com.wx.service.IUserService;
import com.wx.util.BigDecimalUtil;
import com.wx.util.Const;
import com.wx.util.DateTimeUtil;
import com.wx.util.DateUtil;
import com.wx.util.FTPUtil;
import com.wx.util.PropertiesUtil;
import com.wx.vo.OrderItemVo;
import com.wx.vo.OrderVo;

import lombok.extern.log4j.Log4j;

@Service
@Log4j
public class OrderServiceImpl implements IOrderService {
	@Autowired
	private IUserService iUserService;
	
	@Autowired
	private PayInfoMapper payInfoMapper;
	
	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private FoodMapper foodMapper;
	
	@Autowired
	private ShipMapper shipMapper;
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Override
	@Transactional
	public JsonResult add(String code, String order_desc,String cart_ids, Integer shipping_id) {
		if(StringUtils.isBlank(code) || shipping_id==null || cart_ids==null || cart_ids.trim()=="") {
			return JsonResult.errorMsg("参数传入异常");
		}
		log.info("===============小程序用户添加订单==========");
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//cart_ids(1,2,3)==>[1,2,3]
		Integer[] c_ids=null;
		if(!cart_ids.contains(",")) {
			c_ids=new Integer[1];
			c_ids[0]=Integer.parseInt(cart_ids);
		}else {
			String[] cartStrs=cart_ids.split(",");
			System.out.println(Arrays.toString(cartStrs));
			c_ids=new Integer[cartStrs.length];
			for (int index=0;index<cartStrs.length;index++) {
				c_ids[index]=Integer.parseInt(cartStrs[index]);
			}
		}
		
		//获取用户的收货地址
		Shipping shipping=shipMapper.getShip(shipping_id);
		if(shipping==null) {
			return JsonResult.errorMsg("用户收货地址不存在");
		}
		
		//查询出用户要购买的购物车
		List<Cart> cartList=cartMapper.getCartByOpenIdAndCartIds(open_id, c_ids);
		
		//通过购物车的信息创建OrderItem
		List<OrderItem> orderItemList=getOrderItemListByCartList(cartList);
		
		//计算总价
		BigDecimal paymail=getOrderTotalPrice(orderItemList);
		
		//生成order
		Order order=assembleOrder(open_id, shipping_id, order_desc, paymail);
	
		if(order==null) {
			return JsonResult.errorMsg("订单生成失败,服务器累了.请稍后试试");
		}
		//更新订单表中的订单号
		for(OrderItem orderItem:orderItemList) {
			orderItem.setOrder_no(order.getOrder_no());
			orderItem.setOpen_id(open_id);
			System.out.println("open_id:"+orderItem.getOpen_id()+",order_no:"+orderItem.getOrder_no()+
					",food_id:"+orderItem.getFood_id()+",food_name:"+orderItem.getFood_name()+",food_img:"+orderItem.getFood_img()+",food_price:"
					+orderItem.getFood_price()+",food_num:"+orderItem.getFood_num()+",total_price:"+orderItem.getTotal_price());
		}
		
		
		//mybatis 批量插入
		orderItemMapper.batchInsert(orderItemList);
		
		//清空购物车
		this.cleanCart(cartList);
		//返回数据给前端
		OrderVo orderVo=this.assembleOrderVo(order, orderItemList);
		
		return JsonResult.ok(orderVo);
	}
	
	//返回OrderVo给前台查看
	private OrderVo assembleOrderVo(Order order,List<OrderItem> orderItemList) {
		OrderVo orderVo=new OrderVo();
		orderVo.setOrder_no(order.getOrder_no());
		
		orderVo.setPayment(order.getPayment());
		orderVo.setPayment_type(Const.PaymentTypeEnum.codeOf(order.getPayment_type()).getValue());
		orderVo.setOrder_desc(order.getOrder_desc());
		
		orderVo.setStatus(order.getStatus());
		orderVo.setStatus_desc(Const.OrderStatusEnum.codeOf(order.getStatus()).getMsg());
		
		orderVo.setShipping_id(order.getShipping_id());
		Shipping shipping=shipMapper.getShip(order.getShipping_id());
		if(shipping!=null) {
			orderVo.setShipping(shipping);
			
		}
		orderVo.setPayment_time(DateTimeUtil.getString(order.getPayment_time()));
		orderVo.setSend_time(DateTimeUtil.getString(order.getSend_time()));
		orderVo.setEnd_time(DateTimeUtil.getString(order.getEnd_time()));
		orderVo.setCreate_time(DateTimeUtil.getString(order.getCreate_time()));
		orderVo.setClose_time(DateTimeUtil.getString(order.getClose_time()));
		
		//将订单明细中的第一张图片作为购物车图标
		OrderItem orderI=orderItemList.get(0);
		
		orderVo.setImageHost(orderI.getFood_img());
		List<OrderItemVo> orderItemVoList=new ArrayList<OrderItemVo>();
		
		for(OrderItem orderItem:orderItemList) {
			orderItemVoList.add(this.assembOrderItemVo(orderItem));
		}
		
		orderVo.setOrderItemList(orderItemVoList);
		return orderVo;
	}
	
	//将订单详情转换为视图模式
	private OrderItemVo assembOrderItemVo(OrderItem orderItem) {
		OrderItemVo orderItemVo=new OrderItemVo();
		orderItemVo.setOrder_no(orderItem.getOrder_no());
		orderItemVo.setFood_id(orderItem.getFood_id());
		orderItemVo.setFood_name(orderItem.getFood_name());
		orderItemVo.setFood_img(orderItem.getFood_img());
		orderItemVo.setFood_price(orderItem.getFood_price());
		orderItemVo.setFood_num(orderItem.getFood_num());
		orderItemVo.setTotal_price(orderItem.getTotal_price());
		orderItemVo.setCreate_time(orderItem.getCreate_time());
		return orderItemVo;
	}

	//计算当前订单详情的总价
	private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList) {
		BigDecimal paymail=new BigDecimal("0");
		for(OrderItem orderItem:orderItemList) {
			paymail = BigDecimalUtil.add(paymail.doubleValue(), orderItem.getTotal_price().doubleValue());
		}
		return paymail;
	}

	//生成Order
	private Order assembleOrder(String open_id,Integer shipping_id,String order_desc,BigDecimal paymail) {
		Order order=new Order();
		long order_no=this.generateOrderNo();
		order.setOrder_no(order_no);
		order.setOpen_id(open_id);
		order.setShipping_id(shipping_id);
		order.setPayment(paymail);
		order.setPayment_type(Const.PaymentTypeEnum.ONLONE_PAY.getCode());
		order.setStatus(Const.OrderStatusEnum.NO_PAY.getCode());
		order.setOrder_desc(order_desc);
		
		int count=orderMapper.insert(order);
		if(count>0) {
			return order;
		}
		return null;
	}
	
	//清空购物车
	private void cleanCart(List<Cart> cartList) {
		for(Cart cart:cartList) {
			cartMapper.deleteCart(cart.getCart_id());
		}
	}
	
	//生成Long类型的orderNo
	private long generateOrderNo() {
		long currenTime=System.currentTimeMillis();
		//解决高并发中OrderNo重复的问题
		return currenTime+new Random().nextInt(2<<16);
	}

	//通过传入购物车列表生成订单详情列表
	private List<OrderItem> getOrderItemListByCartList(List<Cart> cartList){
		List<OrderItem> orderItemList=new ArrayList<OrderItem>();
		for (Cart cart : cartList) {
			String open_id=cart.getOpen_id();
			Integer food_id=cart.getFood_id();
			Food food=foodMapper.getFoodById(food_id);
			Integer food_num=cart.getFood_num();
			String food_name=food.getFood_name();
			String food_img=food.getFood_img();
			BigDecimal food_price=food.getFood_price();
			BigDecimal total_price=BigDecimalUtil.mul(food_price.doubleValue(),food_num);
			
			OrderItem orderItem=new OrderItem();
			orderItem.setOpen_id(open_id);
			orderItem.setFood_id(food_id);
			orderItem.setFood_name(food_name);
			orderItem.setFood_img(food_img);
			orderItem.setFood_price(food_price);
			orderItem.setFood_num(food_num);
			orderItem.setTotal_price(total_price);
			
			orderItemList.add(orderItem);
		}
		return orderItemList;
	}

	@Override
	//取消订单
	public JsonResult cancel(String code, Long order_no) {
		if(StringUtils.isBlank(code) || order_no==null) {
			return JsonResult.errorMsg("参数传入异常");
		}
		log.info("==============取消订单==================");
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//查看用户是否有这个订单
		Order order=orderMapper.selectOrderByOpenIdAndOrderNo(open_id, order_no);
		if(order==null) {
			return JsonResult.errorMsg("用户没有这个订单!!");
		}
		if(order.getStatus()!=Const.OrderStatusEnum.NO_PAY.getCode()) {
			return JsonResult.errorMsg("订单已支付,无法取消");
		}
		Order updateOrder=new Order();
		updateOrder.setOrder_id(order.getOrder_id());
		updateOrder.setStatus(Const.OrderStatusEnum.CANCELED.getCode());
		int rowCount=orderMapper.updateByPrimaryKeySelective(updateOrder);
		if(rowCount>0) {
			return JsonResult.ok("订单取消成功");
		}
		return JsonResult.errorMsg("订单取消失败");
	}

	@Override
	//获取订单的详情页面
	public JsonResult detail(String code, Long order_no) {
		log.info("=============获取订单详情=================");
		if(StringUtils.isBlank(code) || order_no==null) {
			return JsonResult.errorMsg("参数传入异常");
		}
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		//4.获取Order
		Order order=orderMapper.selectOrderByOpenIdAndOrderNo(open_id, order_no);
		if(order==null) {
			return null;
		}
		//获取所有订单详情
		List<OrderItem> orderItemList=orderItemMapper.getOrderItemsByOrderNo(order_no);
		OrderVo orderVo=this.assembleOrderVo(order, orderItemList);
		return JsonResult.ok(orderVo);
	}

	@Override
	//获取指定类型的所有订单{type==="ALL(所有)","NO_PAY(未支付)","PA_ID(已支付)",SHIPPED(已发货)}
	public JsonResult list(String code,Integer type) {
		if(StringUtils.isBlank(code)) {
			return JsonResult.errorMsg("参数传入异常");
		}
		log.info("============小程序用户获取指定类型的订单===============");
		JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
		
		List<Order> orderList=orderMapper.list(open_id,type);
		List<OrderVo> orderVoList=new ArrayList<OrderVo>();
		for (Order order : orderList) {
			List<OrderItem> orderItemList=orderItemMapper.getOrderItemsByOrderNo(order.getOrder_no());
			orderVoList.add(this.assembleOrderVo(order, orderItemList));
		}
		return JsonResult.ok(orderVoList);
	}

	@Override
	public JsonResult pay(Long orderNo,String code,String path) {
		log.info("=========小程序用户付钱了============");
		System.out.println(orderNo+"\\"+code+"\\"+path);
		Map<String,String> resultMap=new HashMap<String,String>();
		JsonResult userResult=iUserService.getUserId(code);
		if(!userResult.isOK()) {
			return userResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)userResult.getData();
		String open_id=wxSessionModel.getOpenid();
		Order order=orderMapper.selectByUserIdAndOrderNo(open_id, orderNo);
		if(order==null) {
			return JsonResult.errorMsg("用户没有这个订单");
		}
		resultMap.put("orderNo", String.valueOf(order.getOrder_no()));
		
		//生成用户订单号
		String outTradeNo =order.getOrder_no().toString();
		
		//订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店消费”
		String subject = Const.ORDER_FLAG;
		
		// 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = order.getPayment().toString();
        
        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动，如果酒水不参与打折，则将对应金额填写至此字段
        // 如果该值未传入,但传入了【订单总金额】,【打折金额】,则该值默认为【订单总金额】-【打折金额】
        String undiscountableAmount = "0.0";
        
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";
        
        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品3件共20.00元"
        String body =new StringBuilder().append("订单").append(outTradeNo).append("购买商品总价为").append(totalAmount).append("元").toString();
		
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";
        
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";
        
        // 业务扩展参数，目前可添加由支付宝分配的系统商编号(通过setSysServiceProviderId方法)，详情请咨询支付宝技术支持
        String providerId = "2088100200300400500";
        ExtendParams extendParams = new ExtendParams();
        extendParams.setSysServiceProviderId(providerId);
        
        // 支付超时，线下扫码交易定义为5分钟
        String timeoutExpress = "5m";
        
        // 商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
        
        List<OrderItem> orderItemList=orderItemMapper.getByOrderNoUserId(orderNo, open_id);
        for(OrderItem orderItem:orderItemList) {
        	  // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
            GoodsDetail goods1 = GoodsDetail.newInstance(
            		orderItem.getFood_id().toString(),
            		orderItem.getFood_name(),
            		BigDecimalUtil.mul(orderItem.getFood_price().doubleValue(),new Double(100).doubleValue()).longValue(),
            		orderItem.getFood_num());
            // 创建好一个商品后添加至商品明细列表
            goodsDetailList.add(goods1);
        }
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount)
                .setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId)
                .setStoreId(storeId).setExtendParams(extendParams)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(PropertiesUtil.getProperty("alipay.callback.url"))//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setGoodsDetailList(goodsDetailList);
       
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的zfbinfo.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");

        /** 使用Configs提供的默认参数
         *  AlipayTradeService可以使用单例或者为静态成员对象，不需要反复new
         */
        AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();

        AlipayF2FPrecreateResult result = tradeService.tradePrecreate(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
            	log.info("支付宝预下单成功: )");
                AlipayTradePrecreateResponse response=result.getResponse();
                dumpResponse(response);
                
                //如果成功,返回二维码给服务器
                File folder=new File(path);
                if(!folder.exists()) {
                	folder.setWritable(true);
                	folder.mkdirs();
                }
                
                //生成结果为.../upload/返回订单号.png
                String qrPath=String.format(path+"/qr-%s.png", response.getOutTradeNo());
                String qrFileName=String.format("qr-%s.png",response.getOutTradeNo());
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, qrPath);
                
                File targetFile=new File(path,qrFileName);
                
                List<File> fileList=new ArrayList<>();
                fileList.add(targetFile);
                FTPUtil.uploadFile(fileList);
                log.info("[TomcatBbzzzs]二维码生成路径:==>"+qrPath);
                String qrUrl=PropertiesUtil.getProperty("ftp.server.http.prefix")+"/"+targetFile.getName();
                resultMap.put("qrUrl", qrUrl);
                return JsonResult.ok(resultMap);

            case FAILED:
            	log.error("支付宝预下单失败!!!");
                return JsonResult.errorMsg("支付宝预下单失败!!");

            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                return JsonResult.errorMsg("系统异常，预下单状态未知!!!");

            default:
            	log.error("不支持的交易状态，交易返回异常!!!");
                return JsonResult.errorMsg("不支持的交易状态，交易返回异常!!!");
        }
	}

	// 简单打印应答
    private void dumpResponse(AlipayResponse response) {
        if (response != null) {
            log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
            if (StringUtils.isNotEmpty(response.getSubCode())) {
                log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                    response.getSubMsg()));
            }
            log.info("body:" + response.getBody());
        }
    }
    
  //支付宝当面付回调
    public JsonResult aliCallback(Map<String,String> params) {
    	log.info("===========支付宝当前付款回调了===========");
    	//获取订单号
    	Long orderNo=Long.parseLong(params.get("out_trade_no"));
    	//获取支付宝交易编号
    	String tradeNo=params.get("trade_no");
    	//获取订单状态
    	String tradeStatus=params.get("trade_status");
    	
    	//查看交易编号是否存在与数据库中
    	Order order=orderMapper.selectOrderByOrderNo(orderNo);
    	if(order==null) {
    		return JsonResult.errorMsg("交易不存在,回调忽略");
    	}
    	//当订单状态>=20时,说明已经支付了,也就不需要回调了
    	if(order.getStatus()>=Const.OrderStatusEnum.PAID.getCode()) {
    		return JsonResult.errorMsg("支付宝重复调用");
    	}
    	//如果交易成功,设置状态为交易成功
    	if(Const.AlipayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)) {
    		//设置订单的交易完成时间
    		order.setPayment_time(DateTimeUtil.getDate(params.get("gmt_payment")));
    		order.setStatus(Const.OrderStatusEnum.PAID.getCode());
    		orderMapper.updateByPrimaryKeySelective(order);
    	}
    	PayInfo payInfo=new PayInfo();
    	payInfo.setOpen_id(order.getOpen_id());
    	payInfo.setOrder_no(order.getOrder_no());
    	payInfo.setPay_platform(Const.PayPlatformEnum.ALIPAY.getCode());
    	payInfo.setPlatform_number(tradeNo);
    	payInfo.setPlatform_status(tradeStatus);
    	
    	payInfoMapper.insertSelective(payInfo);
    	return JsonResult.ok();
    }
    
    //获取用户支付的订单是否存在
    public JsonResult queryOrderPayStatus(String code,Long orderNo) {
    	log.info("============小程序用户查询订单状态==============");
    	JsonResult jsonResult=iUserService.getUserId(code);
		if(!jsonResult.isOK()) {
			return jsonResult;
		}
		WxSessionModel wxSessionModel=(WxSessionModel)jsonResult.getData();
		String open_id=wxSessionModel.getOpenid();
    	
    	Order order=orderMapper.selectByUserIdAndOrderNo(open_id, orderNo);
    	if(order==null) {
    		return JsonResult.errorMsg("用户没有这个订单");
    	}
    	//如果订单状态已经支付
    	if(order.getStatus()>=Const.OrderStatusEnum.PAID.getCode()) {
    		return JsonResult.ok();
    	}
    	return JsonResult.errorMsg("订单未支付");
    }
    
    //支付宝回调修改订单状态
	@Override
	public void updateOrderStatus(Long orderNo, Integer OrderStatus) {
		orderMapper.updateOrderStatus(orderNo, OrderStatus);
	}

	@Override
	public void closeOrder() {
		//1.获取所有未支付订单
		List<Order> orderList=orderMapper.selectOrdersIsNoPay(Const.OrderStatusEnum.NO_PAY.getCode());
		Date now=new Date();
		for(Order orderItem:orderList) {
			if(DateUtil.dateAnddateIsTrue(orderItem.getCreate_time().getTime(), now.getTime())) {
				orderMapper.updateOrderStatus(orderItem.getOrder_no(), Const.OrderStatusEnum.CANCELED.getCode());
			}
		}
		
	}

}
