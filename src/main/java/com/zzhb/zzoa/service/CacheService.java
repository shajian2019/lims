package com.zzhb.zzoa.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {

	private static Logger logger = LoggerFactory.getLogger(CacheService.class);

	@Autowired
	MenuService menuService;

	@Autowired
	DictService dictService;

	public void flushMenus() {
		logger.info("========清楚菜单缓存========");
		menuService.flushOnemenu();
		menuService.flushSecondmenu();
		menuService.flushTreemenus();
		menuService.flushAllMenu();
	}

	public void flushDict() {
		logger.info("========清楚字典缓存========");
		dictService.flushDicts();
	}
}
