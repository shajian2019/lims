package com.zzhb.zzoa.controller.lcgl;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
