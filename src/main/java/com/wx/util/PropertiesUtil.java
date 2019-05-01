package com.wx.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;

import lombok.extern.log4j.Log4j;

/**
 *@Title PropertiesUtil
 *@Author TomcatBbzzzs
 *@Description 用于读取mmall.properties文件内容
 *@Date 2019/2/7 08:53:01
 */
@Log4j
public class PropertiesUtil {
    private static Properties props;
    
    //当类被使用时,执行静态块,静态块>普通块>构造代码块
    static {
        String fileName = "tomcatBbzzzs.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            log.error("配置文件读取异常",e);
        }
    }
    
    //获取对应的key的value
    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    //获取key的value,当找不到这个key对应的value或找不到key时,默认未defaultValue
    public static String getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }
}