package com.zzhb.zzoa.domain;

import java.io.Serializable;

public class Job implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String createtime;
	private String updatetime;

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

	@Override
	public String toString() {
		return "Job [id=" + id + ", name=" + name + ", createtime=" + createtime + ", updatetime=" + updatetime + "]";
	}

}
