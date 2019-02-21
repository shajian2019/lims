package com.zzhb.controller;

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
import com.alibaba.fastjson.JSONObject;
import com.zzhb.domain.common.Menu;
import com.zzhb.service.MenuService;

@Controller
@RequestMapping("/menus")
public class MenuController {

	@Autowired
	MenuService menuService;

	@GetMapping("")
	@ResponseBody
	public List<Map<String, Object>> menus(String r_id) {
		List<Map<String, Object>> menus = menuService.menus(r_id);
		return menus;
	}

	@GetMapping("/one/get")
	@ResponseBody
	public List<Map<String, Object>> getOneMenus(@RequestParam("r_id") String r_id) {
		List<Map<String, Object>> oneMenusByRoleId = menuService.getOneMenusByRoleId(r_id);
		return oneMenusByRoleId;
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
		// TODO
		return null;
	}

	@GetMapping("/dtree/init")
	@ResponseBody
	public JSONObject initDTree(@RequestParam Map<String, String> params) {
		return menuService.initDTree(params);
	}

	@PostMapping("/update")
	@ResponseBody
	public Integer updateMenu(Menu menu) {
		return menuService.updateMenu(menu);
	}

	@PostMapping("/del")
	@ResponseBody
	public Integer del(@RequestParam Map<String, String> param) {
		return menuService.delMenus(param);
	}
}
