package com.zzhb.zzoa.controller.grgzt;

import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
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
import com.zzhb.zzoa.service.ActivitiService;
import com.zzhb.zzoa.service.DbsxService;

@Controller
@RequestMapping("/grgzt/dbsx")
public class DbsxController {

	private static Logger logger = Logger.getLogger(DbsxController.class);

	@Autowired
	ActivitiService activitiService;

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

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

	// 领取或放弃领取任务
	@PostMapping("/calimTask")
	@ResponseBody
	public Integer claimTask(String taskId, String u_id) {
		return activitiService.claimTask(taskId, u_id);
	}

	// 委托任务
	@PostMapping("/delegateTask")
	@ResponseBody
	public JSONObject delegateTask(String taskId, String userId) {
		return activitiService.delegateTask(taskId, userId);
	}

	@Autowired
	DbsxService dbsxService;

	@GetMapping("/viewTask/{taskId}")
	public String viewTask(String bk, @PathVariable("taskId") String taskId, ModelMap modelMap) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String key = task.getProcessDefinitionId().split(":")[0];
		Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
		modelMap.put("form", renderedTaskForm);
		modelMap.put("formkey", task.getFormKey());
		modelMap.put("taskId", taskId);
		modelMap.put("bk", bk);
		return "grgzt/fqlc/" + key;
	}

	@RequestMapping("/complete/{taskId}")
	@ResponseBody
	public JSONObject complete(@PathVariable("taskId") String taskId, @RequestParam Map<String, String> params) {
		logger.info(taskId + "=" + JSON.toJSONString(params));
		JSONObject submitTaskFormData = activitiService.submitTaskFormData(taskId, params);
		return submitTaskFormData;
	}
}
