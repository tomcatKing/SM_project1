package com.wx.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
	private static final String format="yyyy-MM-dd HH:mm:ss";
	private static final SimpleDateFormat sdf=new SimpleDateFormat(format);
	
	//将Date类型转为String类型
	public static String getString(Date date) {
		if(date==null) {
			return "";
		}
		return sdf.format(date);
	}
	
	//简单字符串转时间
	public static Date getDate(String datetime) {
		if(datetime!="") {
			try {
				return sdf.parse(datetime);
			} catch (ParseException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}
	
}
