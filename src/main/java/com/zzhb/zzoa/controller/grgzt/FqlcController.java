package com.zzhb.zzoa.controller.grgzt;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.domain.activiti.Leave;
import com.zzhb.zzoa.service.ActivitiService;
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

	@Autowired
	ActivitiService as;

	@RequestMapping("/start/{key}")
	public String start(@PathVariable("key") String key, ModelMap modelMap) {
		User user = SessionUtils.getUser();
		String businessKey = user.getU_id() + "-" + TimeUtil.getTimeByCustom("yyyyMMddHHmmss");
		Task task = as.startProcessInstance(user, key, businessKey);
		Object renderedTaskForm = formService.getRenderedTaskForm(task.getId());
		modelMap.put("form", renderedTaskForm);
		modelMap.put("leave", init(user, businessKey));
		return "grgzt/fqlc/" + key;
	}

	@PostMapping("/task")
	@ResponseBody
	public Integer task(Leave leave) {
		return as.task(leave);
	}

	private Leave init(User user, String businessKey) {
		Leave leave = new Leave();
		leave.setBk(businessKey);
		leave.setSqr(user.getNickname());
		Group group = is.createGroupQuery().groupMember(user.getU_id() + "").singleResult();
		leave.setBmmc(group.getName());
		leave.setSqrq(TimeUtil.getTimeByCustom("yyyy-MM-dd HH:mm:ss"));
		return leave;
	}
}
