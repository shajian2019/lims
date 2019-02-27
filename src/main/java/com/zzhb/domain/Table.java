package com.zzhb.domain;

public class Table {

	public String id;
	public String procdefkey;
	public String cols;
	public String prefix;
	public String createtime;
	public String updatetime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProcdefkey() {
		return procdefkey;
	}

	public void setProcdefkey(String procdefkey) {
		this.procdefkey = procdefkey;
	}

	public String getCols() {
		return cols;
	}

	public void setCols(String cols) {
		this.cols = cols;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
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

}
