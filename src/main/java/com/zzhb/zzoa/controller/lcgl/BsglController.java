package com.zzhb.zzoa.controller.lcgl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.ActivitiService;

//部署管理
@Controller
@RequestMapping("/lcgl/bsgl")
public class BsglController {

	@GetMapping("/bsgl")
	public String bsgl() {
		return "lcgl/bsgl/bsgl";
	}

	@Autowired
	ActivitiService activitiService;

	@GetMapping("/list")
	@ResponseBody
	public JSONObject bsglList() {
		return activitiService.listDeloy(1, 20);
	}
}
