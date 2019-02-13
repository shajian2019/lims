package com.zzhb.zzoa.controller.grgzt;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/grgzt/ybsx")
public class YbsxController {

	private static Logger logger = Logger.getLogger(YbsxController.class);

	@RequestMapping("/ybsx")
	public String dbsx() {
		return "grgzt/ybsx/ybsx";
	}
}
