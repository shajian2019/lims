package com.zzhb.zzoa.domain.activiti;

import java.util.ArrayList;
import java.util.List;

public class LeavePdf {

	private String sqrname;
	private String orgname;
	private String startyear;
	private String startmonth;
	private String startday;
	private String endyear;
	private String endmonth;
	private String endday;
	private String qjlb;
	private String qjsy;

	private List<LeavePdfsp> sprs = new ArrayList<>();
	
	private List<LeavePdfsp> bas = new ArrayList<>();

	public String getSqrname() {
		return sqrname;
	}

	public void setSqrname(String sqrname) {
		this.sqrname = sqrname;
	}

	public String getOrgname() {
		return orgname;
	}

	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}

	public String getStartyear() {
		return startyear;
	}

	public void setStartyear(String startyear) {
		this.startyear = startyear;
	}

	public String getStartmonth() {
		return startmonth;
	}

	public void setStartmonth(String startmonth) {
		this.startmonth = startmonth;
	}

	public String getStartday() {
		return startday;
	}

	public void setStartday(String startday) {
		this.startday = startday;
	}

	public String getEndyear() {
		return endyear;
	}

	public void setEndyear(String endyear) {
		this.endyear = endyear;
	}

	public String getEndmonth() {
		return endmonth;
	}

	public void setEndmonth(String endmonth) {
		this.endmonth = endmonth;
	}

	public String getEndday() {
		return endday;
	}

	public void setEndday(String endday) {
		this.endday = endday;
	}

	public String getQjlb() {
		return qjlb;
	}

	public void setQjlb(String qjlb) {
		this.qjlb = qjlb;
	}

	public String getQjsy() {
		return qjsy;
	}

	public void setQjsy(String qjsy) {
		this.qjsy = qjsy;
	}

	public List<LeavePdfsp> getSprs() {
		return sprs;
	}

	public void setSprs(List<LeavePdfsp> sprs) {
		this.sprs = sprs;
	}

	public List<LeavePdfsp> getBas() {
		return bas;
	}

	public void setBas(List<LeavePdfsp> bas) {
		this.bas = bas;
	}

	@Override
	public String toString() {
		return "LeavePdf [sqrname=" + sqrname + ", orgname=" + orgname + ", startyear=" + startyear + ", startmonth="
				+ startmonth + ", startday=" + startday + ", endyear=" + endyear + ", endmonth=" + endmonth
				+ ", endday=" + endday + ", qjlb=" + qjlb + ", qjsy=" + qjsy + ", sprs=" + sprs + ", bas=" + bas + "]";
	}

}
