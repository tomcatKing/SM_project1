package com.wx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages= {"com.wx.mapper"})
public class Application extends SpringBootServletInitializer{
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override //为了打包SpringBoot项目
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(this.getClass());
	}
	
	
}
