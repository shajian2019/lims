package com.zzhb.zzoa.controller.xtgl;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//权限管理
@Controller
@RequestMapping("/xtgl/qxgl")
public class QxglController {

	@GetMapping("/jsgl")
	public String yhgl() {
		return "xtgl/qxgl/jsgl/jsgl";
	}

	@GetMapping("/jsgl/pop")
	public String pop(@RequestParam Map<String, String> params) {
		return "xtgl/qxgl/jsgl/pop";
	}

	@GetMapping("/jsgl/roles/list")
	public void listRoles(Integer page, Integer limit, @RequestParam Map<String, String> params) {

	}
}
