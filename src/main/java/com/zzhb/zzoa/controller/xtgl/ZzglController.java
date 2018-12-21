package com.zzhb.zzoa.controller.xtgl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

//组织管理
@Controller
@RequestMapping("/zzgl")
public class ZzglController {

	@GetMapping("/yhgl")
	public String yhgl() {
		return "xtgl/zzgl/yhgl/yhgl";
	}
}
