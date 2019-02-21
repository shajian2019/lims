package com.zzhb.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

	private static Logger logger = LoggerFactory.getLogger(AsyncService.class);

	@Async
	public void async() {
		logger.debug("==>>异步处理");
	}

}
