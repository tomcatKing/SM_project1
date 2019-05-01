package com.wx.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.wx.common.JsonResult;

import lombok.extern.log4j.Log4j;

/**
 *  全局异常处理类
 * @author tomcatBbzzzs
 *
 */
@RestControllerAdvice
@Log4j
public class GlobalException {
	/**
	 * 处理所有不可知异常
	 * @param e
	 * @return
	 */
	@ExceptionHandler({Exception.class})
	public JsonResult globalExcetpionHandler() {
		log.info("未知的异常(Exception)!!");
		return JsonResult.errorMsg("未知的错误!!");
	}
	
	/**
	 * 处理所有业务异常
	 */
	@ExceptionHandler({RuntimeException.class})
	public JsonResult BusinessExceptionHandler() {
		log.info("运行时异常(RuntimeException)");
		return JsonResult.errorMsg("请求出错了!!");
	}
}
