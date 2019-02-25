package com.zzhb.service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.controller.xtgl.XtszController;
import com.zzhb.domain.Task;
import com.zzhb.mapper.TaskMapper;
import com.zzhb.task.CacheClearDirTask;
import com.zzhb.task.ProcessExpireTask;
import com.zzhb.utils.LayUiUtil;

@Service
public class TaskScheduleService {

	@Autowired
	TaskMapper taskMapper;

	@Autowired
	ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	ProcessExpireTask processExpireTask;

	@Autowired
	CacheClearDirTask cacheClearDirTask;

	public JSONObject list(Integer page, Integer limit, Map<String, Object> params) {
		Map<String, ScheduledFuture<?>> map = XtszController.MAP;
		PageHelper.startPage(page, limit);
		List<Task> tasks = taskMapper.getTasks(params);
		PageInfo<Task> pageInfo = new PageInfo<>(tasks);
		tasks = pageInfo.getList();
		for (Task task : tasks) {
			String key = task.getTaskId();
			if (map.containsKey(key)) {
				task.setStatus("1");
			} else {
				task.setStatus("0");
			}
		}
		return LayUiUtil.pagination(pageInfo);
	}

	public Integer startTask(String taskId, String cron) {
		Map<String, ScheduledFuture<?>> map = XtszController.MAP;
		ScheduledFuture<?> schedule = map.get(taskId);
		if (schedule != null) {
			schedule.cancel(true);
		}
		if ("ProcessExpireTask".equals(taskId)) {
			schedule = threadPoolTaskScheduler.schedule(processExpireTask, new CronTrigger(cron));
		} else if ("CacheClearDirTask".equals(taskId)) {
			schedule = threadPoolTaskScheduler.schedule(cacheClearDirTask, new CronTrigger(cron));
		}
		map.put(taskId, schedule);
		return 1;
	}

	@Transactional
	public Integer stopTask(String taskId) {
		Map<String, ScheduledFuture<?>> map = XtszController.MAP;
		ScheduledFuture<?> scheduledFuture = map.get(taskId);
		if (scheduledFuture != null) {
			scheduledFuture.cancel(true);
			map.remove(taskId);
		}
		return 1;
	}

	@Transactional
	public Integer sfzq(Task task) {
		return taskMapper.updateTask(task);
	}
	
	@Autowired
	XtszController xtszController;

	@Transactional
	public Integer add(Task task, String flag, String oldTaskId) {
		Integer addTask = 0;
		if ("add".equals(flag)) {
			
			addTask = taskMapper.addTask(task);
		} else {
			taskMapper.updateTask(task);
			
		}
		return 1;
	}
}
