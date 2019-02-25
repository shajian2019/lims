package com.zzhb.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import com.zzhb.controller.xtgl.XtszController;
import com.zzhb.domain.Task;
import com.zzhb.domain.common.Param;
import com.zzhb.mapper.ParamMapper;
import com.zzhb.mapper.TaskMapper;
import com.zzhb.task.CacheClearDirTask;
import com.zzhb.task.ProcessExpireTask;

import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;

@Service
public class InitService {

	@Autowired
	ParamMapper paramMapper;

	@Autowired
	TaskMapper taskMapper;

	@Autowired
	private Configuration configuration;

	public void initParams() throws TemplateModelException {
		List<Param> params2 = paramMapper.getParams(null);
		for (Param param : params2) {
			configuration.setSharedVariable(param.getKey(), param.getValue());
		}
	}

	@Autowired
	ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	ProcessExpireTask processExpireTask;

	@Autowired
	CacheClearDirTask cacheClearDirTask;

	public void initTask() {
		Map<String, Object> params = new HashMap<>();
		params.put("sfzq", "1");
		List<Task> tasks = taskMapper.getTasks(params);
		for (Task task2 : tasks) {
			ScheduledFuture<?> schedule = XtszController.MAP.get(task2.getTaskId());
			if (schedule != null) {
				schedule.cancel(true);
			}
			if ("ProcessExpireTask".equals(task2.getTaskId())) {
				schedule = threadPoolTaskScheduler.schedule(processExpireTask, new CronTrigger(task2.getCron()));
			} else if ("CacheClearDirTask".equals(task2.getTaskId())) {
				schedule = threadPoolTaskScheduler.schedule(cacheClearDirTask, new CronTrigger(task2.getCron()));
			}
			XtszController.MAP.put(task2.getTaskId(), schedule);
		}
	}
}
