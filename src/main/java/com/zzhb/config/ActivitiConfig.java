package com.zzhb.config;

import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ActivitiConfig {

	@Bean
	public ProcessEngineConfigurationConfigurer ProcessEngineConfigurationConfigurer() {
		return new ProcessEngineConfigurationConfigurer() {
			@Override
			public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
				processEngineConfiguration.setActivityFontName("WenQuanYi Zen Hei");
				processEngineConfiguration.setLabelFontName("WenQuanYi Zen Hei");
				processEngineConfiguration.setAnnotationFontName("WenQuanYi Zen Hei");
			}
		};
	}
}
