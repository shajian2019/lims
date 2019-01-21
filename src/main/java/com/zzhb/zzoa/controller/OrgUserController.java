package com.zzhb.zzoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/orgUser")
public class OrgUserController {


	@GetMapping("/list")
	@ResponseBody
	public void list(@RequestParam("u_id") String u_id,@RequestParam("d_id") String d_id) {
		
	}
}
