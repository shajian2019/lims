package com.zzhb.zzoa.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.zzoa.domain.activiti.UserSpr;
import com.zzhb.zzoa.service.OrgUserService;

@Controller
@RequestMapping("/orgUser")
public class OrgUserController {

	@Autowired
	OrgUserService orgUserService;

	@GetMapping("/list")
	@ResponseBody
	public List<Map<String, Object>> list(String p_id) {
		return orgUserService.list(p_id);
	}

	@GetMapping("/spr")
	@ResponseBody
	public List<Map<String, Object>> spr(UserSpr userSpr) {
		return orgUserService.sprList(userSpr);
	}
}
