package com.zzhb.domain.activiti;

public class ProcessDefinitionType {

	private String type;

	private String name;

	private Integer sort;

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ProcessDefinitionType [type=" + type + ", name=" + name + ", sort=" + sort + "]";
	}

}
