package com.zzhb.domain.activiti;

public class Leave {

	private String proid;
	private String bk;
	private String u_id;
	private String sqr;
	private String bmmc;
	private String sqrq;
	private String qjlx;
	private String ksrq;
	private String jsrq;
	private String qjly;
	private String spjg;

	public String getSpjg() {
		return spjg;
	}

	public void setSpjg(String spjg) {
		this.spjg = spjg;
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	public String getProid() {
		return proid;
	}

	public void setProid(String proid) {
		this.proid = proid;
	}

	public String getBk() {
		return bk;
	}

	public void setBk(String bk) {
		this.bk = bk;
	}

	public String getSqr() {
		return sqr;
	}

	public void setSqr(String sqr) {
		this.sqr = sqr;
	}

	public String getBmmc() {
		return bmmc;
	}

	public void setBmmc(String bmmc) {
		this.bmmc = bmmc;
	}

	public String getSqrq() {
		return sqrq;
	}

	public void setSqrq(String sqrq) {
		this.sqrq = sqrq;
	}

	public String getQjlx() {
		return qjlx;
	}

	public void setQjlx(String qjlx) {
		this.qjlx = qjlx;
	}

	public String getKsrq() {
		return ksrq;
	}

	public void setKsrq(String ksrq) {
		this.ksrq = ksrq;
	}

	public String getJsrq() {
		return jsrq;
	}

	public void setJsrq(String jsrq) {
		this.jsrq = jsrq;
	}

	public String getQjly() {
		return qjly;
	}

	public void setQjly(String qjly) {
		this.qjly = qjly;
	}

	@Override
	public String toString() {
		return "Leave [proid=" + proid + ", bk=" + bk + ", u_id=" + u_id + ", sqr=" + sqr + ", bmmc=" + bmmc + ", sqrq="
				+ sqrq + ", qjlx=" + qjlx + ", ksrq=" + ksrq + ", jsrq=" + jsrq + ", qjly=" + qjly + "]";
	}

}
