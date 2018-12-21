package com.zzhb.zzoa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.zzhb.zzoa.domain.common.Menu;
import com.zzhb.zzoa.service.MenuService;

@Controller
@RequestMapping("/menus")
public class MenuController {

	@Autowired
	MenuService menuService;

	@GetMapping("/one/get")
	@ResponseBody
	public List<Menu> getOneMenus(@RequestParam("r_id") String r_id) {
		return menuService.getOneMenusByRoleId(r_id);
	}

	@GetMapping("/second/get")
	@ResponseBody
	public List<Map<String, Object>> getSecondMenu(@RequestParam("parentid") String parentid,
			@RequestParam("r_id") String r_id) {
		return menuService.getSecondMenu(r_id + ">" + parentid, r_id, parentid);
	}

	@GetMapping("/tree/init")
	@ResponseBody
	public JSONArray initMenuTree(@RequestParam(defaultValue = "0") String level, String r_id,
			@RequestParam Map<String, String> params) {
		return menuService.initMenuTree(level, r_id, params);
	}

	@PostMapping("/update")
	@ResponseBody
	public Integer updateMenu(Menu menu) {
		System.out.println(menu);
		return menuService.updateMenu(menu);
	}

	@PostMapping("/del")
	@ResponseBody
	public Integer del(@RequestParam Map<String, String> param) {
		return menuService.delMenus(param);
	}
}
