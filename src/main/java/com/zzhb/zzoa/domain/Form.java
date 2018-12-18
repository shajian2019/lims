package com.zzhb.zzoa.domain;

public class Form {

	private String userName;
	private String password;
	private String rememberMe;
	private String validCode;

	public String getValidCode() {
		return validCode;
	}

	public void setValidCode(String validCode) {
		this.validCode = validCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRememberMe() {
		return rememberMe;
	}

	public void setRememberMe(String rememberMe) {
		this.rememberMe = rememberMe;
	}

	@Override
	public String toString() {
		return "Form [username=" + userName + ", password=" + password + ", rememberMe=" + rememberMe + ", validCode="
				+ validCode + "]";
	}

}
