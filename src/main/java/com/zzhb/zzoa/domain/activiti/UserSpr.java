package com.zzhb.zzoa.domain.activiti;

public class UserSpr {

	private String id;
	private String uid;
	private String key;
	private String taskkey;
	private String sprs;

	private String o_id;

	public String getO_id() {
		return o_id;
	}

	public void setO_id(String o_id) {
		this.o_id = o_id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTaskkey() {
		return taskkey;
	}

	public void setTaskkey(String taskkey) {
		this.taskkey = taskkey;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSprs() {
		return sprs;
	}

	public void setSprs(String sprs) {
		this.sprs = sprs;
	}

}
