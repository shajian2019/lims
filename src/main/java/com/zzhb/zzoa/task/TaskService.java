package com.zzhb.zzoa.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

	@Scheduled(cron = "0/30 * * * * ?")
	public void task() {
//		System.out.println("定时任务开始执行");
	}
}
