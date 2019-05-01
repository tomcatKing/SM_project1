package com.wx.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.wx.common.RedisOperator;
import com.wx.service.IOrderService;
import com.wx.util.JedisUtil;

import lombok.extern.log4j.Log4j;

@Component
@Log4j
public class OrderTask {
	@Autowired
	private IOrderService iOrderService;
	
	
	//关闭订单的分布式锁的名称
	private final String CLOSE_ORDER_TASK_LOCK="close_lock";
	
	public String toDate(Date date) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	
	@Scheduled(cron="0 0 */1 * * ?")
	public void OrderPlayV2() {
		log.info("关单操作启动了");
		long lockTimeout=5000; //设置为5秒,实际开发最好设置5秒
		
		//设置分布式任务锁,如果这个锁已经存在就会返回0
		Long setnxResult=JedisUtil.
			setnx(CLOSE_ORDER_TASK_LOCK, 
					String.valueOf(System.currentTimeMillis()+lockTimeout));
		
		//如果不为空并且返回1,表示成功
		if(setnxResult!=null && setnxResult.intValue()==1) {
			//获得锁后的操作
			closeOrder(CLOSE_ORDER_TASK_LOCK);
		}else{
			//此时没有获取到锁,但是还是要继续操作
			
			String lockValue=JedisUtil.get(CLOSE_ORDER_TASK_LOCK);
			//如果这个锁存在,并且当前时间大于这个锁(创建时间+存活时间)
			if(lockValue!=null && System.currentTimeMillis()>Long.parseLong(lockValue)) {
				String oldValue=JedisUtil.getset(CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis()+lockTimeout));
				//再次用当前时间戳getset
				//返回给定的key的旧值-->旧值判断,是否可以获取锁
				//当key不存在时,返回nil	-->获取锁
				//如果这个返回的值不存在 或者这个返回的值是当前我们设置的值,那么这个操作也就拿到锁了
				if(oldValue==null || (oldValue!=null && lockValue.equals(oldValue))) {
					//获取到锁的操作
					closeOrder(CLOSE_ORDER_TASK_LOCK);
				}else {
					System.out.println("tomcat1没有获取分布式锁");
				}
			}else {
				System.out.println("这个锁还在使用");
			}
		}
		
		log.info(toDate(new Date())+"tomcat1定时关单执行结束");
	}
	
	private void closeOrder(String lockName) {
		JedisUtil.expire(lockName, 50); //有效期为50秒,防止死锁
		//获取锁
		Long keycode=Long.parseLong(JedisUtil.get(lockName));
		//关单操作
		iOrderService.closeOrder();
		//执行操作后删除锁
		JedisUtil.del(lockName);
		System.out.println("tomcat1释放分布式锁");
	}
	
}