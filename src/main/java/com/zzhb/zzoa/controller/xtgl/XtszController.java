package com.zzhb.zzoa.controller.xtgl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.MenuService;

//系统设置
@Controller
@RequestMapping("/xtgl")
public class XtszController {

	@GetMapping("/xtsz/cdgl")
	public String cdgl() {
		return "xtgl/xtsz/cdgl";
	}

	@Autowired
	MenuService menuService;

	@GetMapping("/xtsz/cdgl/getall")
	@ResponseBody
	public JSONObject getCd(@RequestParam Map<String,String> params) {
		return menuService.getAllMenus(params);
	}
}
