package com.zzhb.zzoa.controller.xtgl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//权限管理
@Controller
@RequestMapping("/xtgl/qxgl")
public class qxglController {

	@GetMapping("/jsgl")
	public String yhgl() {
		return "xtgl/qxgl/jsgl/jsgl";
	}
}
