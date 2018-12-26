package com.zzhb.zzoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/grzx")
public class GrzxController {

	@GetMapping("/grzx")
	public String grzx() {
		return "grzx/grzx";
	}
	
	@GetMapping("/xgmm")
	public String xgmm() {
		return "grzx/xgmm";
	}
}
