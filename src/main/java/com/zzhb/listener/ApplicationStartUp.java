package com.zzhb.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.zzhb.redis.RedisService;
import com.zzhb.service.InitService;

import freemarker.template.Configuration;

@Component
public class ApplicationStartUp implements InitializingBean {

	private static Logger logger = Logger.getLogger(ApplicationStartUp.class);

	@Autowired
	InitService initService;

	@Value(value = "${server.context-path:}")
	private String contextpath;

	@Autowired
	private Configuration configuration;

	@Autowired
	RedisService redisService;

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("==============springboot启动================");
		// freemarker 全局配置
		logger.info("==============configuration================" + configuration);
		configuration.setSharedVariable("ctx", contextpath);
		initService.initParams();

		String flushdb = redisService.flushdb();
		logger.info("==============flushdb====" + flushdb + "============");
	}
}
