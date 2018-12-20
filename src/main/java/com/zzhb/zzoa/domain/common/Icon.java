package com.zzhb.zzoa.domain.common;

public class Icon {

	private Integer id;
	private String name;
	private String icon;
	private String type;
	private Integer sort;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "Icon [id=" + id + ", name=" + name + ", icon=" + icon + ", type=" + type + ", sort=" + sort + "]";
	}
	
	

}
