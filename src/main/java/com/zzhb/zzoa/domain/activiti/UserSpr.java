package com.zzhb.zzoa.domain.activiti;

public class UserSpr {

	private String id;
	private String uid;
	private String key;
	private String formkey;
	private String sprs;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	public String getFormkey() {
		return formkey;
	}
	public void setFormkey(String formkey) {
		this.formkey = formkey;
	}
	public String getSprs() {
		return sprs;
	}
	public void setSprs(String sprs) {
		this.sprs = sprs;
	}
	@Override
	public String toString() {
		return "UserSpr [id=" + id + ", uid=" + uid + ", key=" + key + ", formkey=" + formkey + ", sprs=" + sprs + "]";
	}
	
}
