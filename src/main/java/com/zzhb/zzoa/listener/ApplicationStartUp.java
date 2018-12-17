package com.zzhb.zzoa.listener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUp implements InitializingBean {

	private static Logger logger = Logger.getLogger(ApplicationStartUp.class);


	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("==============springboot启动================");
	}
}
