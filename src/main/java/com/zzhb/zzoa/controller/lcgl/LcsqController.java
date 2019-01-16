package com.zzhb.zzoa.controller.lcgl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lcgl/lcsq")
public class LcsqController {

	@GetMapping("/fqsq")
	public String fqsq() {
		return "lcgl/lcsq/fqsq/fqsq";
	}
}
