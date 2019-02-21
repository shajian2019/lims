package com.zzhb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.domain.User;
import com.zzhb.service.UserService;

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

	@Autowired
	UserService userService;

	@PostMapping("/pass/check")
	@ResponseBody
	public Integer checkOldPass(User user) {
		return userService.checkPass(user);
	}

	@PostMapping("/pass/update")
	@ResponseBody
	public Integer updatePass(User user) {
		return userService.updatePass(user);
	}
}