package com.zzhb.zzoa.controller.grgzt;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.ActivitiService;

@Controller
@RequestMapping("/grgzt/ybsx")
public class YbsxController {

	private static Logger logger = Logger.getLogger(YbsxController.class);

	@Autowired
	ActivitiService activitiService;

	@RequestMapping("/ybsx")
	public String dbsx() {
		return "grgzt/ybsx/ybsx";
	}

	@ResponseBody
	public JSONObject list(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "0x7fffffff") Integer limit, @RequestParam Map<String, String> params) {
		return activitiService.getHistoricTaskInstance(page, limit, params);
	}

}
