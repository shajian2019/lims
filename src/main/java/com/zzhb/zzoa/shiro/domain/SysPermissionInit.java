package com.zzhb.zzoa.shiro.domain;

public class SysPermissionInit {

	private int id;
	private String url;
	private String qxlj;
	private int sort;
	private String sfsx;
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

	public String getSfsx() {
		return sfsx;
	}

	public void setSfsx(String sfsx) {
		this.sfsx = sfsx;
	}

	public String getQxlj() {
		return qxlj;
	}

	public void setQxlj(String qxlj) {
		this.qxlj = qxlj;
	}

	@Override
	public String toString() {
		return "SysPermissionInit [id=" + id + ", url=" + url + ", qxlj=" + qxlj + ", sort=" + sort + ", sfsx=" + sfsx
				+ "]";
	}

}
