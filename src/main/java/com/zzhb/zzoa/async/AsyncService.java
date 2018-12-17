package com.zzhb.zzoa.async;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsyncService {

	@Async
	public void async() {
		System.out.println("异步处理");
	}

}
