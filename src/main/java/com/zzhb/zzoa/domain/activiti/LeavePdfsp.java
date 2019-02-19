package com.zzhb.zzoa.domain.activiti;

public class LeavePdfsp {

	private String spdw;
	private String spyj;
	private String spr;

	private String year;
	private String month;
	private String day;

	public String getSpdw() {
		return spdw;
	}

	public void setSpdw(String spdw) {
		this.spdw = spdw;
	}

	public String getSpyj() {
		return spyj;
	}

	public void setSpyj(String spyj) {
		this.spyj = spyj;
	}

	public String getSpr() {
		return spr;
	}

	public void setSpr(String spr) {
		this.spr = spr;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	@Override
	public String toString() {
		return "LeavePdfsp [spdw=" + spdw + ", spyj=" + spyj + ", spr=" + spr + ", year=" + year + ", month=" + month
				+ ", day=" + day + "]";
	}

}
