package com.zzhb.controller.grgzt;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.zzhb.domain.Org;
import com.zzhb.domain.Table;
import com.zzhb.domain.User;
import com.zzhb.domain.activiti.UserSpr;
import com.zzhb.mapper.TableMapper;
import com.zzhb.service.ActivitiService;
import com.zzhb.service.FqlcService;
import com.zzhb.utils.SessionUtils;
import com.zzhb.utils.TimeUtil;

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
	
	@Autowired
	TableMapper tableMapper;

	@RequestMapping("/beforeStart/{processDefinitionId}")
	public String beforeStart(@PathVariable("processDefinitionId") String processDefinitionId, ModelMap modelMap) {
		String key = processDefinitionId.split(":")[0];
		User user = SessionUtils.getUser();
		Object renderedStartForm = formService.getRenderedStartForm(processDefinitionId);
		String startFormKey = formService.getStartFormKey(processDefinitionId);
		String businessKey = TimeUtil.getTimeByCustom("yyyyMMddHHmmss") + user.getU_id();
		modelMap.put(key, initData(key, user, businessKey));
		modelMap.put("form", renderedStartForm);
		modelMap.put("formkey", startFormKey);
		return "grgzt/fqlc/" + key;
	}

	@RequestMapping("/start/{key}")
	@ResponseBody
	public JSONObject start(@PathVariable("key") String key, @RequestParam Map<String, String> params) throws Exception {
		logger.info(key + "=" + JSON.toJSONString(params));
		return as.startProcessInstance(key, params);
	}

	private Object initData(String key, User user, String bk) {
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("sqr", user.getNickname());
		data.put("bk", bk);
		return data;
	}

	@GetMapping("/spr")
	public String fqlcSpr(@RequestParam("key") String key, @RequestParam("formkey") String formkey, ModelMap modelMap) {
		modelMap.put("key", key);
		modelMap.put("formkey", formkey);
		return "grgzt/fqlc/spr";
	}

	@PostMapping("/saveSpr")
	@ResponseBody
	public Integer saveSpr(UserSpr userSpr) {
		return fqlcService.saveSpr(userSpr);
	}

	@PostMapping("/getSprs")
	@ResponseBody
	public List<Map<String, String>> getSprs(UserSpr userSpr) {
		return fqlcService.getSprs(userSpr);
	}

	@GetMapping("/getOrgs")
	@ResponseBody
	public List<Org> getOrgs(String u_id) {
		return fqlcService.getOrgs(u_id);
	}

	@GetMapping("/getZtreeChapter")
	@ResponseBody
	public List<Map<String, Object>> getZtreeChapter() {
		return fqlcService.getZtreeChapters();
	}
}
