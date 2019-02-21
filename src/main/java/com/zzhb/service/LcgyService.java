package com.zzhb.service;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LcgyService {

	@Autowired
	RuntimeService rs;

	@Autowired
	HistoryService hs;

	@Autowired
	TaskService ts;

	@Transactional
	public Integer removeProcessInstance(String processInstanceIds) {
		List<String> list = Arrays.asList(processInstanceIds.split(","));
		for (String processInstanceId : list) {
			Task ruTask = ts.createTaskQuery().processInstanceId(processInstanceId).singleResult();
			if (ruTask != null) {
				rs.deleteProcessInstance(processInstanceId, "流程干预刪除");
			}
			hs.deleteHistoricProcessInstance(processInstanceId);
		}
		return 1;
	}
}
