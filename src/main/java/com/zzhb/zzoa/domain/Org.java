package com.zzhb.zzoa.domain;

import java.io.Serializable;

public class Org implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String createtime;
	private String updatetime;
	private String status;
	private String remark;
	private String level;
	private String sort;
	private String parentid;

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

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "Org [id=" + id + ", name=" + name + ", createtime=" + createtime + ", updatetime=" + updatetime
				+ ", status=" + status + ", remark=" + remark + ", level=" + level + ", sort=" + sort + ", parentid="
				+ parentid + "]";
	}

}
