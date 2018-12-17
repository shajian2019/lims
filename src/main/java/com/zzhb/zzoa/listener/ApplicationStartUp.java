package com.zzhb.zzoa.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import freemarker.template.Configuration;

@Component
public class ApplicationStartUp implements InitializingBean {

	private static Logger logger = Logger.getLogger(ApplicationStartUp.class);

	@Autowired
	private Configuration configuration;

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("==============springboot启动================");
		// freemarker 全局配置
		logger.info("==============configuration================" + configuration);
		configuration.setSharedVariable("name", "chenhj");
	}
}
