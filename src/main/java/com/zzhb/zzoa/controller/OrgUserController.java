package com.zzhb.zzoa.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public JSONObject list(@RequestParam("p_id") String p_id) {
		return orgUserService.list(p_id);
	}
	
	@GetMapping("/spr")
	@ResponseBody
	public JSONObject spr(@RequestParam Map<String,String> params) {
		System.out.println(params.toString());
		return orgUserService.sprList(params);
	}
}
