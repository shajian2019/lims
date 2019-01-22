package com.zzhb.zzoa.controller.lcgl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/lcgl/lcgy")
public class LcgyController {

	@GetMapping("/lcsc")
	public String lcsc() {
		return "lcgl/lcgy/lcsc/lcsc";
	}

}
