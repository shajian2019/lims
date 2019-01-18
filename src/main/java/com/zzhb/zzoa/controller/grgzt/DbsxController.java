package com.zzhb.zzoa.controller.grgzt;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.service.ActivitiService;

@Controller
@RequestMapping("/grgzt/dbsx")
public class DbsxController {

	private static Logger logger = Logger.getLogger(DbsxController.class);

	@Autowired
	ActivitiService activitiService;

	@RequestMapping("/dbsx")
	public String dbsx() {
		return "grgzt/dbsx/dbsx";
	}
	
	@RequestMapping("/list")
	@ResponseBody
	public JSONObject dbsxList(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "0x7fffffff") Integer limit, @RequestParam Map<String, Object> params) {
		return activitiService.dbsxList(page, limit, params);
	}

	
	@PostMapping("/calimTask")
	@ResponseBody
	public Integer calimTask(String taskId,String u_id) {
		return activitiService.calimTask(taskId, u_id);
	}
	
	@PostMapping("/completeTask")
	@ResponseBody
	public Integer completeTask(String taskId,String u_id) {
		return activitiService.calimTask(taskId, u_id);
	}
	
}
