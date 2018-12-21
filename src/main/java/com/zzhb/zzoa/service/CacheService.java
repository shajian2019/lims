package com.zzhb.zzoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
	
	@Autowired
	MenuService menuService;

	public void flushMenus() {
		menuService.flushOnemenu();
		menuService.flushSecondmenu();
		menuService.flushMenuTree();

	}
}
