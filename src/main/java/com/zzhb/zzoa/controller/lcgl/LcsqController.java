package com.zzhb.zzoa.controller.lcgl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.zzoa.service.FqsqService;

@Controller
@RequestMapping("/lcgl/lcsq")
public class LcsqController {

	@GetMapping("/fqsq")
	public String fqsq() {
		return "lcgl/lcsq/fqsq/fqsq";
	}
	
	@GetMapping("/fqsq/pop")
	public String pop(@RequestParam("p_id")String p_id,ModelMap modelMap) {
		modelMap.put("p_id", p_id);
		return "lcgl/lcsq/fqsq/pop";
	}
	
	@Autowired
	FqsqService fqsqService;
	
	@PostMapping("/fqsq/bindUser")
	@ResponseBody
	public Integer bindUser(@RequestParam("p_id")String p_id,@RequestParam("u_ids")String u_ids) {
		return fqsqService.bindUser(p_id, u_ids);
	}
	
	@PostMapping("/fqsq/unBindUser")
	@ResponseBody
	public Integer unBindUser(@RequestParam("p_id")String p_id,@RequestParam("u_id")String u_id) {
		return fqsqService.unBindUser(p_id, u_id);
	}
}
