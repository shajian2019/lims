package com.zzhb.domain;

public class Task {

	private String id;
	private String taskId;
	private String taskName;
	private String cron;
	private String remark;
	private String sfzq;
	private String createtime;
	private String updatetime;
	private String createId;
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSfzq() {
		return sfzq;
	}

	public void setSfzq(String sfzq) {
		this.sfzq = sfzq;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", taskId=" + taskId + ", taskName=" + taskName + ", cron=" + cron + ", remark="
				+ remark + ", sfzq=" + sfzq + ", createtime=" + createtime + ", updatetime=" + updatetime
				+ ", createId=" + createId + ", status=" + status + "]";
	}

}
