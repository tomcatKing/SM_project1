package com.wx.util;

import java.math.BigDecimal;

/**
 *@Title 用于解决数字计算丢失精度的问题
 *@author TomcatBbzzzs
 *@Date 2019/02/09 19:24:25
 */
public class BigDecimalUtil {
	public static final int ADD=1,SUB=2,MUL=3,DIV=4; //加减乘除
	
	//主要的计算方式
	public static BigDecimal mainMath(double num1,double num2,int type) {
		BigDecimal b1=new BigDecimal(Double.toString(num1));
		BigDecimal b2=new BigDecimal(Double.toString(num2));
		if(type==ADD) {
			return b1.add(b2);
		}else if(type==SUB) {
			return b1.subtract(b2);
		}else if(type==MUL){
			return b1.multiply(b2);
		}else{
			return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP); //四舍五入,保留2位小数
		}
	}
	
	//提供的主要的简便的方式
	public static BigDecimal add(double num1,double num2) {
		return mainMath(num1,num2,ADD);
	}
	public static BigDecimal sub(double num1,double num2) {
		return mainMath(num1,num2,SUB);
	}
	public static BigDecimal mul(double num1,double num2) {
		return mainMath(num1,num2,MUL);
	}
	public static BigDecimal div(double num1,double num2) {
		return mainMath(num1,num2,DIV);
	}
	
}