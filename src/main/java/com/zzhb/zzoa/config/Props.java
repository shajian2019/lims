package com.zzhb.zzoa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "props")
public class Props {

	private String demo;

	private String loginUrl;

	private Long globalSessionTimeout;
	private Long sessionValidationInterval;
	private int cookemaxage;

	public String getDemo() {
		return demo;
	}

	public void setDemo(String demo) {
		this.demo = demo;
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

	public Long getSessionValidationInterval() {
		return sessionValidationInterval;
	}

	public void setSessionValidationInterval(Long sessionValidationInterval) {
		this.sessionValidationInterval = sessionValidationInterval;
	}

	public int getCookemaxage() {
		return cookemaxage;
	}

	public void setCookemaxage(int cookemaxage) {
		this.cookemaxage = cookemaxage;
	}

}
