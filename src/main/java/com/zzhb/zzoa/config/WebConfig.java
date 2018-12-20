package com.zzhb.zzoa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.zzhb.zzoa.interceptor.MvcInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	MvcInterceptor mvcInterceptor;

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(mvcInterceptor).addPathPatterns("/**");
	}
}
