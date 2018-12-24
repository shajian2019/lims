package com.zzhb.zzoa.controller.xtgl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.RoleService;

//权限管理
@Controller
@RequestMapping("/xtgl/qxgl")
public class QxglController {

	@GetMapping("/jsgl")
	public String yhgl() {
		return "xtgl/qxgl/jsgl/jsgl";
	}

	@GetMapping("/jsgl/pop")
	public String pop(@RequestParam Map<String, String> params) {
		return "xtgl/qxgl/jsgl/pop";
	}

	@Autowired
	RoleService roleService;

	@GetMapping("/jsgl/roles/list")
	@ResponseBody
	public JSONObject listRoles(Integer page, Integer limit, @RequestParam Map<String, String> params) {
		return roleService.listRoles(page, limit, params);
	}

	@PostMapping("/jsgl/role/update")
	@ResponseBody
	public Integer updateRole(@RequestParam Map<String, String> params) {
		return roleService.updateRole(params);
	}

}
