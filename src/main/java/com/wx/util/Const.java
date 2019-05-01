package com.wx.util;


public class Const {
	public final static String ORDER_FLAG="tomcatBbzzzs的测试项目";
	
	public interface AlipayCallback{
    	String TRADE_STATUS_WAIT_BUYER_PAY="WAIT_BUYER_PAY";
    	String TRADE_STATUS_TRADE_SUCCESS="TRADE_SUCCESS";
    	
    	String RESPONSE_SUCCESS="success";
    	String RESPONSE_FAILED="failed";
    }
	//订单状态
	public enum OrderStatusEnum{
		CANCELED(0,"已取消"),
    	NO_PAY(10,"未支付"),
    	PAID(20,"已付款"),
    	SHIPPED(40,"已发货"),
    	ORDER_SUCCESS(50,"订单完成"),
    	ORDER_CLOSE(60,"订单关闭");
		
		OrderStatusEnum(int code,String msg) {
			this.code=code;
			this.msg=msg;
		}
		
		private int code;
		private String msg;
		public int getCode() {
			return code;
		}
		public void setCode(int code) {
			this.code = code;
		}
		public String getMsg() {
			return msg;
		}
		public void setMsg(String msg) {
			this.msg = msg;
		}
		//订单状态  orderVo orderStatus
	    public static OrderStatusEnum codeOf(int code) {
	    	for(OrderStatusEnum orderStatusEnum:values()) {
	    		//如果遍历到了这个枚举,就返回
	    		if(orderStatusEnum.getCode()==code) {
	    			return orderStatusEnum;
	    		}
	    	}
	    	throw new RuntimeException("没有找到对应的枚举");
	    }
	}
	public enum PayPlatformEnum{
    	ALIPAY(1,"支付宝");
    	
    	PayPlatformEnum(int code,String value){
    		this.code=code;
    		this.value=value;
    	}
    	private String value;
    	private int code;
		public String getValue() {
			return value;
		}
		public int getCode() {
			return code;
		}	
	    public static PayPlatformEnum codeOf(int code) {
	    	for(PayPlatformEnum paymentTypeEnum:values()) {
	    		//如果遍历到了这个枚举,就返回
	    		if(paymentTypeEnum.getCode()==code) {
	    			return paymentTypeEnum;
	    		}
	    	}
	    	throw new RuntimeException("没有找到对应的枚举");
	    }
    }
	
	//支付类型
	public enum PaymentTypeEnum{
    	ONLONE_PAY(1,"在线支付");
    	PaymentTypeEnum(int code,String value){
    		this.code=code;
    		this.value=value;
    	}
    	private String value;
    	private int code;
		public String getValue() {
			return value;
		}
		public int getCode() {
			return code;
		}	
		
		//订单描述  orderVo.paymentTypeDesc
	    public static PaymentTypeEnum codeOf(int code) {
	    	for(PaymentTypeEnum paymentTypeEnum:values()) {
	    		//如果遍历到了这个枚举,就返回
	    		if(paymentTypeEnum.getCode()==code) {
	    			return paymentTypeEnum;
	    		}
	    	}
	    	throw new RuntimeException("没有找到对应的枚举");
	    }
    }
	
}
