package com.zzhb.controller.grgzt;

import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.zzhb.service.ActivitiService;
import com.zzhb.service.DbsxService;
import com.zzhb.service.UserService;

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
	
	@Autowired
	UserService userService;

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
	public JSONObject delegateTask(String taskId, String owerId, String userId) {
		return activitiService.delegateTask(taskId, owerId, userId);
	}

	// 转办任务
	@PostMapping("/transferTask")
	@ResponseBody
	public JSONObject transferTask(String taskId, String owerId, String userId) {
		return activitiService.transferTask(taskId, owerId, userId);
	}

	@Autowired
	DbsxService dbsxService;

	@GetMapping("/viewTask/{taskId}")
	public String viewTask(String bk, @PathVariable("taskId") String taskId,String userId, ModelMap modelMap) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String key = task.getProcessDefinitionId().split(":")[0];
		//判断当前流程的会签节点是否有意见
		/*String processInstanceId=task.getProcessInstanceId();
		List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId, "comment");
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		 String countersignature="";
			for(Comment comment : comments) {
				String str= comment.getFullMessage();
				String time = sdf.format(comment.getTime());
				 Map maps = (Map)JSON.parse(str);  
			        for (Object map : maps.entrySet()){  
			        	String keyStr = map.toString();
			        	//之前的会签意见
			        	if(keyStr.contains("countersignature")) {
			        		String assigneeId = (String) maps.get("assignee");
			        		String assignName= userService.getUserNameByUserId(assigneeId);
			        		countersignature += (String) maps.get("countersignature")+"("+assignName+"  "+time+")"+"\n";
			        	}
			        }  
			}
			if(countersignature != null && countersignature != "") {
				modelMap.put("countersignature", countersignature);
				taskService.addComment(taskId, processInstanceId, JSON.toJSONString(modelMap));
			}*/
		
		//获取当前月-日
		SimpleDateFormat sdf= new SimpleDateFormat("MM-dd");
		Date d = new Date();
		String nowDay=sdf.format(d);
		
		Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
		modelMap.put("form", renderedTaskForm);
		modelMap.put("formkey", task.getFormKey());
		modelMap.put("taskId", taskId);
		modelMap.put("bk", bk);
		//传入当前登录人
		modelMap.put("userId", userId);
		modelMap.put("nowDay", nowDay);
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
