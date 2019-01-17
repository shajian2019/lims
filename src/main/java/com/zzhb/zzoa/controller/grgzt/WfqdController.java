package com.zzhb.zzoa.controller.grgzt;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.ActivitiService;

@Controller
@RequestMapping("/grgzt/wfqd")
public class WfqdController {

	private static Logger logger = Logger.getLogger(WfqdController.class);

	@Autowired
	ActivitiService activitiService;

	@RequestMapping("/wfqd")
	public String wfqd() {
		return "grgzt/wfqd/wfqd";
	}

	@GetMapping("/list")
	@ResponseBody
	public JSONObject list(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "0x7fffffff") Integer limit, @RequestParam Map<String, String> params) {
		return activitiService.getHistoricProcessInstances(page, limit, params);
	}

	@PostMapping("/preview")
	@ResponseBody
	public String preview(@RequestParam Map<String, String> params) {
		return activitiService.previewByBk(params);
	}
}
