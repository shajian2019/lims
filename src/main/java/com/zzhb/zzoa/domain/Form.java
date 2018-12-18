package com.zzhb.zzoa.domain;

public class Form {

	private String username;
	private String password;
	private String yzm;

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "Form [username=" + username + ", password=" + password + ", yzm=" + yzm + "]";
	}

}
