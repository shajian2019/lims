package com.zzhb.zzoa.controller.lcgl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.zzoa.service.LcgyService;

@Controller
@RequestMapping("/lcgl/lcgy")
public class LcgyController {

	@GetMapping("/lcsc")
	public String lcsc() {
		return "lcgl/lcgy/lcsc/lcsc";
	}

	@Autowired
	LcgyService lcgyService;

	@PostMapping("/remove")
	@ResponseBody
	public Integer remove(String processInstanceIds) {
		return lcgyService.removeProcessInstance(processInstanceIds);
	}

}
