package com.wx.util;

import redis.clients.jedis.Jedis;

public class JedisUtil {
	public static String set(String key,String value) {
		Jedis jedis=null;
		String result=null;
		try {
			jedis=JedisPoolUtil.getJedis();
			result=jedis.set(key, value);	
		} catch (Exception e) {
			JedisPoolUtil.returnExeResource(jedis);
			return result;
		}
		JedisPoolUtil.returnResource(jedis); //放到正常池中
		return result;
	}
	
	public static String get(String key) {
		Jedis jedis=null;
		String result=null;
		try {
			jedis=JedisPoolUtil.getJedis();
			result=jedis.get(key);	
		} catch (Exception e) {
			JedisPoolUtil.returnExeResource(jedis);
			return result;
		}
		JedisPoolUtil.returnResource(jedis); //放到正常池中
		return result;
	}
	
	/**
	 * 
	 * @param key
	 * @param value
	 * @param exTime以秒为单位
	 * @return
	 */
	public static String setEx(String key,String value,int exTime) {
		Jedis jedis=null;
		String result=null;
		try {
			jedis=JedisPoolUtil.getJedis();
			result=jedis.setex(key, exTime,value);
		} catch (Exception e) {
			JedisPoolUtil.returnExeResource(jedis);
			return result;
		}
		JedisPoolUtil.returnResource(jedis); //放到正常池中
		return result;
	}
	/**
	 * 设置key的有效期,单位是秒
	 * @param key
	 * @param exTime
	 * @return
	 */
	public static Long expire(String key,int exTime) {
		Jedis jedis=null;
		Long result=null;
		try {
			jedis=JedisPoolUtil.getJedis();
			result=jedis.expire(key, exTime);
		} catch (Exception e) {
			JedisPoolUtil.returnExeResource(jedis);
			return result;
		}
		JedisPoolUtil.returnResource(jedis); //放到正常池中
		return result;
	}
	
	public static Long setnx(String key,String value) {
		Jedis jedis=null;
		Long result=null;
		try {
			jedis=JedisPoolUtil.getJedis();
			result=jedis.setnx(key, value);	
		} catch (Exception e) {
			JedisPoolUtil.returnExeResource(jedis);
			return result;
		}
		JedisPoolUtil.returnResource(jedis); //放到正常池中
		return result;
	}
	
	public static Long del(String key) {
		Jedis jedis=null;
		Long result=null;
		try {
			jedis=JedisPoolUtil.getJedis();
			result=jedis.del(key);
		} catch (Exception e) {
			JedisPoolUtil.returnExeResource(jedis);
			return result;
		}
		JedisPoolUtil.returnResource(jedis); //放到正常池中
		return result;
	}
	
	public static String getset(String key,String value) {
		Jedis jedis=null;
		String result=null;
		try {
			jedis=JedisPoolUtil.getJedis();
			result=jedis.getSet(key, value);
		}catch (Exception e) {
			JedisPoolUtil.returnExeResource(jedis);
			return result;
		}
		JedisPoolUtil.returnResource(jedis); //放到正常池中
		return result;
	}
}