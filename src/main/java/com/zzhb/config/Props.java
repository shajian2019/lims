package com.zzhb.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "props")
public class Props {

	private String loginUrl;

	private String cookiename;

	private Long globalSessionTimeout;

	private String tempPath;
	
	private List<String> flushkeys = new ArrayList<>();
	
	public List<String> getFlushkeys() {
		return flushkeys;
	}

	public void setFlushkeys(List<String> flushkeys) {
		this.flushkeys = flushkeys;
	}

	public String getCookiename() {
		return cookiename;
	}

	public void setCookiename(String cookiename) {
		this.cookiename = cookiename;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public Long getGlobalSessionTimeout() {
		return globalSessionTimeout;
	}

	public void setGlobalSessionTimeout(Long globalSessionTimeout) {
		this.globalSessionTimeout = globalSessionTimeout;
	}

	public String getTempPath() {
		return tempPath;
	}

	public void setTempPath(String tempPath) {
		this.tempPath = tempPath;
	}

}