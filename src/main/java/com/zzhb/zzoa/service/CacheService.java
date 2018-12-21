package com.zzhb.zzoa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zzhb.zzoa.ZzoaApplication;

@Service
public class CacheService {

	private static Logger logger = LoggerFactory.getLogger(ZzoaApplication.class);

	@Autowired
	MenuService menuService;

	public void flushMenus() {
		logger.info("========清楚菜单缓存========");
		menuService.flushOnemenu();
		menuService.flushSecondmenu();
		menuService.flushMenuTree();
	}
}
