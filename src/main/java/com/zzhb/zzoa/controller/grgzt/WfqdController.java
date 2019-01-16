package com.zzhb.zzoa.controller.grgzt;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/grgzt/wfqd")
public class WfqdController {

	private static Logger logger = Logger.getLogger(WfqdController.class);

	@RequestMapping("/wfqd")
	public String wfqd() {
		return "grgzt/wfqd/wfqd";
	}
	
	public void getProcess() {
		
	}

	
}
