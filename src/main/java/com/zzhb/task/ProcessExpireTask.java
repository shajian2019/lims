package com.zzhb.task;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.mapper.ActivitiMapper;
import com.zzhb.service.ActivitiService;

@Service
public class ProcessExpireTask implements Runnable {

	private static Logger logger = LoggerFactory.getLogger(ProcessExpireTask.class);

	@Autowired
	ActivitiMapper activitiMapper;

	@Autowired
	HistoryService historyService;

	@Autowired
	ActivitiService activitiService;

	@Override
	public void run() {
		Integer count = 0;
		Date nowDate = new Date();
		List<Map<String, Object>> unfinishedProcessInstanceAndHavingZdcxsc = activitiMapper
				.getUnfinishedProcessInstanceAndHavingZdcxsc();
		for (Map<String, Object> map : unfinishedProcessInstanceAndHavingZdcxsc) {
			Date startTime = (Date) map.get("START_TIME_");
			Long zdcxsc = Long.parseLong(map.get("zdcxsc").toString()) * 3600 * 1000;
			Long times = nowDate.getTime() - startTime.getTime();
			if (times >= zdcxsc) {
				activitiService.deleteProcessInstance(map.get("PROC_INST_ID_").toString(), "实例已过期，自动撤销");
				count++;
			}
		}
		logger.info("==================清除过期实例======================" + count);
	}

}
