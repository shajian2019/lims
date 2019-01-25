package com.zzhb.zzoa.controller.grgzt;

import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.Org;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.domain.activiti.Leave;
import com.zzhb.zzoa.service.ActivitiService;
import com.zzhb.zzoa.service.FqlcService;
import com.zzhb.zzoa.utils.SessionUtils;
import com.zzhb.zzoa.utils.TimeUtil;

@Controller
@RequestMapping("/grgzt/fqlc")
public class FqlcController {

	private static Logger logger = Logger.getLogger(FqlcController.class);

	@Autowired
	FqlcService fqlcService;

	@RequestMapping("/fqlc")
	public String fqlc() {
		return "grgzt/fqlc/fqlc";
	}

	@GetMapping("/list")
	@ResponseBody
	public JSONObject fqlcList(@RequestParam(defaultValue = "1") Integer page,
			@RequestParam(defaultValue = "0x7fffffff") Integer limit, @RequestParam Map<String, String> params) {
		return fqlcService.fqlcList(page, limit, params);
	}

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	FormService formService;

	@Autowired
	TaskService taskService;

	@Autowired
	IdentityService is;

	@Autowired
	ActivitiService as;

	@RequestMapping("/beforeStart/{processDefinitionId}")
	public String beforeStart(@PathVariable("processDefinitionId") String processDefinitionId, ModelMap modelMap) {
		String key = processDefinitionId.split(":")[0];
		User user = SessionUtils.getUser();
		Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
		String startFormKey = formService.getStartFormKey(processDefinitionId);
		String businessKey = TimeUtil.getTimeByCustom("yyyyMMddHHmmss") + user.getU_id();
		modelMap.put(key, initLeave(user, businessKey));
		modelMap.put("form", renderedStartForm);
		modelMap.put("formkey", startFormKey);
		return "grgzt/fqlc/" + key;
	}

	@RequestMapping("/start/{key}")
	@ResponseBody
	public JSONObject start(@PathVariable("key") String key, @RequestParam Map<String, String> params) {
		logger.info(key + "=" + JSON.toJSONString(params));
		return as.startProcessInstance(key, params);
	}

	private Leave initLeave(User user, String bk) {
		Leave leave = new Leave();
		leave.setSqr(user.getNickname());
		Org org = SessionUtils.getOrg();
		leave.setBk(bk);
		leave.setBmmc(org.getName());
		leave.setSqrq(TimeUtil.getTimeByCustom("yyyy-MM-dd HH:mm:ss"));
		return leave;
	}

	@GetMapping("/spr")
	public String fqlcSpr(@RequestParam("key") String key, @RequestParam("formkey") String formkey, ModelMap modelMap) {
		modelMap.put("key", key);
		modelMap.put("formkey", formkey);
		return "grgzt/fqlc/spr";
	}

	@PostMapping("/saveSpr")
	@ResponseBody
	public Integer saveSpr(@RequestParam Map<String, String> params) {
		return fqlcService.saveSpr(params);
	}

	@PostMapping("/getSprs")
	@ResponseBody
	public List<Map<String, String>> getSprs(@RequestParam Map<String, String> params) {
		return fqlcService.getSprs(params);
	}
}
