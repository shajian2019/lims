package com.zzhb.zzoa.controller.xtgl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.Role;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.service.RoleService;

//权限管理
@Controller
@RequestMapping("/xtgl/qxgl")
public class QxglController {

	@GetMapping("/jsgl")
	public String jsgl() {
		return "xtgl/qxgl/jsgl/jsgl";
	}

	@Autowired
	RoleMapper roleMapper;

	@GetMapping("/jsgl/pop")
	public ModelAndView pop(@RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView();
		String flag = params.get("flag");
		mv.addObject("flag", flag);
		if ("edit".equals(flag)) {
			String r_id = params.get("r_id");
			Role role = roleMapper.getRolByRid(r_id);
			mv.addObject("role", role);
		}
		mv.setViewName("xtgl/qxgl/jsgl/pop");
		return mv;
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

	@PostMapping("/jsgl/role/add")
	@ResponseBody
	public Integer addRole(@RequestParam Map<String, String> params) {
		return roleService.addRole(params);
	}

	@PostMapping("/jsgl/role/del")
	@ResponseBody
	public Integer delRole(@RequestParam Map<String, Object> params) {
		return roleService.delRole(params);
	}

	@GetMapping("/jsgl/role/bind")
	public ModelAndView bind(@RequestParam Map<String, String> params) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("param", params);
		mv.setViewName("xtgl/qxgl/jsgl/bind");
		return mv;
	}

	@PostMapping("/jsgl/role/bindUser")
	@ResponseBody
	public Integer bindUser(@RequestParam Map<String, Object> params) {
		return roleService.bindUser(params);
	}

}
