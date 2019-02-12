package com.zzhb.zzoa.controller.grgzt;

import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/history/preview")
	public String historypreview(String businessKey, ModelMap modeMap) {
		modeMap.put("businessKey", businessKey);
		return "grgzt/wfqd/preview";
	}

	@GetMapping("/history/list")
	@ResponseBody
	public JSONObject historyList(String businessKey) {
		return activitiService.historyTask(businessKey);
	}

	// 撤销请假流程
	@PostMapping("/revoke")
	@ResponseBody
	public Integer revoke(String processInstanceId) {
		return activitiService.deleteProcessInstance(processInstanceId, "撤销流程");
	}

	// 流程挂起与激活
	@PostMapping("/pauseAndPlay/{event}")
	@ResponseBody
	public JSONObject pauseAndPlay(@PathVariable("event") String event, String processInstanceId) {
		return activitiService.pauseAndPlay(event, processInstanceId);
	}

}
