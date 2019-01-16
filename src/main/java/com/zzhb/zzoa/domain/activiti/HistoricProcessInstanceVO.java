package com.zzhb.zzoa.domain.activiti;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import com.zzhb.zzoa.utils.TimeUtil;

public class HistoricProcessInstanceVO {

	private String businessKey;
	private String processDefinitionKey;
	private String processDefinitionName;
	private String startTime;
	private String endTime;

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public static List<HistoricProcessInstanceVO> getHistoricProcessInstanceVOs(List<HistoricProcessInstance> hps) {
		List<HistoricProcessInstanceVO> list = new ArrayList<HistoricProcessInstanceVO>();
		for (HistoricProcessInstance hp : hps) {
			HistoricProcessInstanceVO vo = new HistoricProcessInstanceVO();
			vo.setBusinessKey(hp.getBusinessKey());
			vo.setProcessDefinitionKey(hp.getProcessDefinitionKey());
			vo.setProcessDefinitionName(hp.getProcessDefinitionName());
			vo.setStartTime(TimeUtil.getTimeFromDate("yyyy-MM-dd HH:mm:ss", hp.getStartTime()));
			vo.setEndTime(TimeUtil.getTimeFromDate("yyyy-MM-dd HH:mm:ss", hp.getEndTime()));
			list.add(vo);
		}
		return list;
	}
}
