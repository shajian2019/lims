package com.zzhb.zzoa.domain.activiti;

public class UserSpr {

	private String uid;
	private String key;
	private String name;
	private String sprs;
	private String rev;
	private String createtime;
	private String updatetime;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSprs() {
		return sprs;
	}

	public void setSprs(String sprs) {
		this.sprs = sprs;
	}

	public String getRev() {
		return rev;
	}

	public void setRev(String rev) {
		this.rev = rev;
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
		return "UserSpr [uid=" + uid + ", key=" + key + ", name=" + name + ", sprs=" + sprs + ", rev=" + rev
				+ ", createtime=" + createtime + ", updatetime=" + updatetime + "]";
	}

}
