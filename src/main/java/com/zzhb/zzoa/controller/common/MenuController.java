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

	@GetMapping("/oneMenus/get")
	@ResponseBody
	public List<Menu> getOneMenus(String r_id) {
		return menuService.getOneMenusByRoleId(r_id);
	}

	@GetMapping("/secondMenu/get")
	@ResponseBody
	public List<Map<String, Object>> getSecondMenu(String parentid, String r_id) {
		return menuService.getSecondMenu(r_id, parentid);
	}
}
