package com.zzhb.zzoa.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "props")
public class Props {

	private String loginUrl;

	private Long globalSessionTimeout;
	private int cookemaxage;

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

	public int getCookemaxage() {
		return cookemaxage;
	}

	public void setCookemaxage(int cookemaxage) {
		this.cookemaxage = cookemaxage;
	}

}
