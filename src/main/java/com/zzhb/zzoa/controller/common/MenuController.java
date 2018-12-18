package com.zzhb.zzoa.controller.common;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.zzoa.domain.common.Menu;
import com.zzhb.zzoa.service.MenuService;

@Controller
public class MenuController {

	@Autowired
	MenuService menuService;

	@GetMapping("/oneMenu/get")
	@ResponseBody
	public List<Menu> getOneMenus(String rolename) {
		return menuService.getOneMenus(rolename);
	}

	@GetMapping("/secondMenu/get")
	@ResponseBody
	public List<Map<String, Object>> getSecondMenu(String parentid, String rolename) {
		return menuService.getSecondMenu(rolename, parentid);
	}
}
