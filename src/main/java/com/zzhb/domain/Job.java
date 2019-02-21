package com.zzhb.domain;

import java.io.Serializable;

public class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String sort;
	private String remark;
	private String status;

	private String createtime;
	private String updatetime;

	private String likename;
	
	public String getLikename() {
		return likename;
	}

	public void setLikename(String likename) {
		this.likename = likename;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	@Override
	public String toString() {
		return "Job [id=" + id + ", name=" + name + ", sort=" + sort + ", remark=" + remark + ", status=" + status
				+ ", createtime=" + createtime + ", updatetime=" + updatetime + "]";
	}

}
