package com.zzhb.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

	/**
	 * 定时任务监测过期流程实例
	 */
	@Scheduled(cron = "0/30 * * * * ?")
	public void revoke() {

	}
}
