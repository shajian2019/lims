package com.zzhb.domain;

import java.io.Serializable;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer u_id;
	private String username;
	private String password;
	private String nickname;
	private String createtime;
	private String updatetime;
	private String remark;
	private String email;
	private String phone;
	private String status;
	private String recentlogin;
	private String bgdh;
	private String bgxh;
	private String phonexh;
	private Integer r_id;

	public String getBgdh() {
		return bgdh;
	}

	public void setBgdh(String bgdh) {
		this.bgdh = bgdh;
	}

	public String getBgxh() {
		return bgxh;
	}

	public void setBgxh(String bgxh) {
		this.bgxh = bgxh;
	}

	public String getPhonexh() {
		return phonexh;
	}

	public void setPhonexh(String phonexh) {
		this.phonexh = phonexh;
	}

	public Integer getU_id() {
		return u_id;
	}

	public void setU_id(Integer u_id) {
		this.u_id = u_id;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRecentlogin() {
		return recentlogin;
	}

	public void setRecentlogin(String recentlogin) {
		this.recentlogin = recentlogin;
	}

	public Integer getR_id() {
		return r_id;
	}

	public void setR_id(Integer r_id) {
		this.r_id = r_id;
	}

	@Override
	public String toString() {
		return "User [u_id=" + u_id + ", username=" + username + ", password=" + password + ", nickname=" + nickname
				+ ", createtime=" + createtime + ", updatetime=" + updatetime + ", remark=" + remark + ", email="
				+ email + ", phone=" + phone + ", status=" + status + ", recentlogin=" + recentlogin + ", bgdh=" + bgdh
				+ ", bgxh=" + bgxh + ", phonexh=" + phonexh + ", r_id=" + r_id + "]";
	}

}
