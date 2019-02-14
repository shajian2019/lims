package com.zzhb.zzoa.domain.activiti;

import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.history.HistoricTaskInstance;

import com.zzhb.zzoa.utils.TimeUtil;

public class HistoricTaskInstanceVO {
	private String id;

	private String processInstanceId;

	private String name;

	private String currentName;

	private String startTime;

	private String endTime;

	private String assignee;

	private String owner;

	private Long durationInMillis;

	private String claimTime;

	private String deleteReason;

	private String spyj;

	private boolean sftg;

	private String description;

	private String businessKey;

	private String processDefinitionName;

	private boolean lcsfjs;

	public String getCurrentName() {
		return currentName;
	}

	public void setCurrentName(String currentName) {
		this.currentName = currentName;
	}

	public boolean isLcsfjs() {
		return lcsfjs;
	}

	public void setLcsfjs(boolean lcsfjs) {
		this.lcsfjs = lcsfjs;
	}

	public String getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isSftg() {
		return sftg;
	}

	public void setSftg(boolean sftg) {
		this.sftg = sftg;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getDeleteReason() {
		return deleteReason;
	}

	public void setDeleteReason(String deleteReason) {
		this.deleteReason = deleteReason;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public Long getDurationInMillis() {
		return durationInMillis;
	}

	public void setDurationInMillis(Long durationInMillis) {
		this.durationInMillis = durationInMillis;
	}

	public String getClaimTime() {
		return claimTime;
	}

	public void setClaimTime(String claimTime) {
		this.claimTime = claimTime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSpyj() {
		return spyj;
	}

	public void setSpyj(String spyj) {
		this.spyj = spyj;
	}

	public static List<HistoricTaskInstanceVO> getHistoricTaskInstanceVOs(List<HistoricTaskInstance> hps) {
		List<HistoricTaskInstanceVO> list = new ArrayList<HistoricTaskInstanceVO>();
		for (HistoricTaskInstance hp : hps) {
			HistoricTaskInstanceVO vo = new HistoricTaskInstanceVO();
			vo.setId(hp.getId());
			vo.setProcessInstanceId(hp.getProcessInstanceId());
			vo.setDeleteReason(hp.getDeleteReason());
			vo.setName(hp.getName());
			vo.setStartTime(TimeUtil.getTimeFromDate("yyyy-MM-dd HH:mm:ss", hp.getStartTime()));
			vo.setEndTime(TimeUtil.getTimeFromDate("yyyy-MM-dd HH:mm:ss", hp.getEndTime()));
			vo.setClaimTime(TimeUtil.getTimeFromDate("yyyy-MM-dd HH:mm:ss", hp.getClaimTime()));
			vo.setDurationInMillis(hp.getDurationInMillis());
			vo.setAssignee(hp.getAssignee());
			vo.setOwner(hp.getOwner());

			vo.setDescription(hp.getDescription());
			list.add(vo);
		}
		return list;
	}

	@Override
	public String toString() {
		return "HistoricTaskInstanceVO [id=" + id + ", processInstanceId=" + processInstanceId + ", name=" + name
				+ ", startTime=" + startTime + ", endTime=" + endTime + ", assignee=" + assignee + ", owner=" + owner
				+ ", durationInMillis=" + durationInMillis + ", claimTime=" + claimTime + ", deleteReason="
				+ deleteReason + ", spyj=" + spyj + ", sftg=" + sftg + "]";
	}
}
