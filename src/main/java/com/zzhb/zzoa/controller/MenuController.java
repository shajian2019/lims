package com.zzhb.zzoa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.zzoa.domain.common.Menu;
import com.zzhb.zzoa.service.MenuService;

@Controller
@RequestMapping("/menus")
public class MenuController {

	@Autowired
	MenuService menuService;

	@GetMapping("/one/get")
	@ResponseBody
	public List<Menu> getOneMenus(String r_id) {
		return menuService.getOneMenusByRoleId(r_id);
	}

	@GetMapping("/second/get")
	@ResponseBody
	public List<Map<String, Object>> getSecondMenu(String parentid, String r_id) {
		return menuService.getSecondMenu(r_id, parentid);
	}
}
