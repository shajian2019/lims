package com.zzhb.zzoa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.zzoa.domain.activiti.UserSpr;
import com.zzhb.zzoa.service.OrgUserService;

@Controller
@RequestMapping("/orgUser")
public class OrgUserController {

	@Autowired
	OrgUserService orgUserService;

	@GetMapping("/membership")
	public String membership(@RequestParam Map<String, String> params, ModelMap modelMap) {
		modelMap.put("params", params);
		return "grgzt/dbsx/membership";
	}

	// 委托和指派ztree
	@GetMapping("/zpOrwt")
	@ResponseBody
	public List<Map<String, Object>> zpOrwt(String chkDisabled) {
		return orgUserService.zpOrwt(chkDisabled);
	}

	// 授权人ztree
	@GetMapping("/sqr")
	@ResponseBody
	public List<Map<String, Object>> sqr(String p_id) {
		return orgUserService.sqr(p_id);
	}

	// 审批人ztree
	@GetMapping("/spr")
	@ResponseBody
	public List<Map<String, Object>> spr(UserSpr userSpr) {
		return orgUserService.sprList(userSpr);
	}
}
