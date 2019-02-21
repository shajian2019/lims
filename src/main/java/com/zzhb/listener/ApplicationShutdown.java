package com.zzhb.listener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationShutdown implements ApplicationListener<ContextClosedEvent> {

	private static Logger logger = Logger.getLogger(ApplicationShutdown.class);

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		logger.info("============onApplicationEvent=====");
	}

}
