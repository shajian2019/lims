package com.zzhb;

import java.nio.charset.Charset;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableAsync
@EnableCaching
@MapperScan({ "com.zzhb.mapper", "com.zzhb.shiro.mapper" })
@SpringBootApplication
public class AppApplication {

	private static Logger logger = LoggerFactory.getLogger(AppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AppApplication.class, args);
		String encoding = Charset.defaultCharset().name();
		logger.info("======AppApplication===启动 success==encoding=" + encoding);
		logger.debug("======AppApplication===启动 success==encoding=" + encoding);
		
	}
}
