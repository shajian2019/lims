package com.zzhb.zzoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.OrgUserService;

@Controller
@RequestMapping("/orgUser")
public class OrgUserController {
	
	@Autowired
	OrgUserService orgUserService;
	
	@GetMapping("/list")
	@ResponseBody
	public JSONObject main() {
		return orgUserService.getOrgUsers();
	}
}
