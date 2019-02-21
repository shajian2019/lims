package com.zzhb.domain.activiti;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.history.HistoricProcessInstance;

import com.zzhb.utils.TimeUtil;

public class HistoricProcessInstanceVO {

	private String processInstanceId;
	private String businessKey;
	private String processDefinitionKey;
	private String processDefinitionName;
	private String startTime;
	private String endTime;
	private String deleteReason;
	private String owerId;
	private boolean suspended;
	private boolean sftg;

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

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

	public String getOwerId() {
		return owerId;
	}

	public void setOwerId(String owerId) {
		this.owerId = owerId;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public boolean isSftg() {
		return sftg;
	}

	public void setSftg(boolean sftg) {
		this.sftg = sftg;
	}

	@Override
	public String toString() {
		return "HistoricProcessInstanceVO [processInstanceId=" + processInstanceId + ", businessKey=" + businessKey
				+ ", processDefinitionKey=" + processDefinitionKey + ", processDefinitionName=" + processDefinitionName
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", deleteReason=" + deleteReason + "]";
	}

	public static List<HistoricProcessInstanceVO> getHistoricProcessInstanceVOs(List<HistoricProcessInstance> hps) {
		List<HistoricProcessInstanceVO> list = new ArrayList<HistoricProcessInstanceVO>();
		for (HistoricProcessInstance hp : hps) {
			HistoricProcessInstanceVO vo = new HistoricProcessInstanceVO();
			vo.setProcessInstanceId(hp.getId());
			vo.setDeleteReason(hp.getDeleteReason());
			vo.setBusinessKey(hp.getBusinessKey());
			vo.setProcessDefinitionKey(hp.getProcessDefinitionKey());
			vo.setProcessDefinitionName(hp.getProcessDefinitionName());
			vo.setStartTime(TimeUtil.getTimeFromDate("yyyy-MM-dd HH:mm:ss", hp.getStartTime()));
			vo.setEndTime(TimeUtil.getTimeFromDate("yyyy-MM-dd HH:mm:ss", hp.getEndTime()));
			vo.setOwerId(hp.getStartUserId());
			list.add(vo);
		}
		return list;
	}
}
