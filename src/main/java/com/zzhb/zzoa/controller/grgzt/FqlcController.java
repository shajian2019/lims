package com.zzhb.zzoa.controller.grgzt;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.domain.activiti.Leave;
import com.zzhb.zzoa.utils.SessionUtils;
import com.zzhb.zzoa.utils.TimeUtil;

@Controller
@RequestMapping("/fqlc")
public class FqlcController {

	@RequestMapping("/fqlc")
	public String fqlc() {
		return "grgzt/fqlc/fqlc";
	}

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	FormService formService;

	@Autowired
	TaskService taskService;

	@Autowired
	IdentityService is;

	@RequestMapping("/start/{key}")
	public String start(@PathVariable("key") String key, ModelMap modelMap) {
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.latestVersion().singleResult();
		User user = SessionUtils.getUser();
		Map<String, String> vars = new HashMap<>();
		String businessKey = user.getU_id()+"-"+TimeUtil.getTimeByCustom("yyyyMMddHHmmss");
		ProcessInstance pi = formService.submitStartFormData(pd.getId(), businessKey, vars);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		Object renderedTaskForm = formService.getRenderedTaskForm(task.getId());
		modelMap.put("form", renderedTaskForm);
		modelMap.put("leave", init(user,businessKey));
		return "grgzt/fqlc/" + key;
	}

	private Leave init(User user,String businessKey) {
		Leave leave = new Leave();
		leave.setBk(businessKey);
		leave.setSqr(user.getNickname());
		Group group = is.createGroupQuery().groupMember(user.getU_id() + "").singleResult();
		leave.setBmmc(group.getName());
		leave.setSqrq(TimeUtil.getTimeByCustom("yyyy-MM-dd HH:mm:ss"));
		return leave;
	}
}
