package com.zzhb.zzoa;

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
@MapperScan(value = "com.zzhb.zzoa.mapper")
@SpringBootApplication
public class ZzoaApplication {

	private static Logger logger = LoggerFactory.getLogger(ZzoaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ZzoaApplication.class, args);
		logger.info("ZzoaApplication===start success==");
		logger.debug("ZzoaApplication===start success==");
	}

}
