package com.zzhb.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.config.Props;
import com.zzhb.domain.User;
import com.zzhb.domain.activiti.HistoricProcessInstanceVO;
import com.zzhb.domain.activiti.HistoricTaskInstanceVO;
import com.zzhb.domain.activiti.Leave;
import com.zzhb.domain.activiti.ProcessDefinitionExt;
import com.zzhb.domain.activiti.ProcessDefinitionType;
import com.zzhb.mapper.ActivitiMapper;
import com.zzhb.mapper.LeaveMapper;
import com.zzhb.mapper.UserMapper;
import com.zzhb.mapper.UserSprMapper;
import com.zzhb.utils.CustomProcessDiagramGenerator;
import com.zzhb.utils.FileUtil;
import com.zzhb.utils.LayUiUtil;
import com.zzhb.utils.SessionUtils;
import com.zzhb.utils.TimeUtil;
import com.zzhb.utils.ZipUtils;

@Service
public class ActivitiService {

	private static Logger logger = LoggerFactory.getLogger(ActivitiService.class);

	@Autowired
	Props props;

	@Autowired
	ActivitiMapper activitiMapper;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	FormService formService;

	@Autowired
	LeaveMapper leaveMapper;

	@Autowired
	ProcessEngine pes;

	@Autowired
	UserMapper userMapper;

	@Transactional
	public Integer deploy(Map<String, String> params, MultipartFile file) throws IOException {
		String resourceName = file.getOriginalFilename();
		Deployment deploy = repositoryService.createDeployment()
				.addZipInputStream(new ZipInputStream(file.getInputStream())).name(resourceName).deploy();
		List<ProcessDefinition> pdfs = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
				.list();
		List<ProcessDefinitionExt> pdes = new ArrayList<ProcessDefinitionExt>();
		Map<String, String> params2 = new HashMap<>();
		for (ProcessDefinition pdf : pdfs) {
			ProcessDefinitionExt pde = new ProcessDefinitionExt();
			pde.setId(pdf.getId());
			pde.setDeployment_id(deploy.getId());
			pde.setCreateuser(params.get("createuser"));
			pde.setDescription(params.get("description"));
			pde.setDgrm_resource_name(pdf.getDiagramResourceName());
			pde.setKey(pdf.getKey());
			pde.setName(pdf.getName());
			pde.setResource_name(pdf.getResourceName());
			pde.setVersion(pdf.getVersion());
			pde.setProtype(params.get("protype"));
			pdes.add(pde);
			params2.put("key", pdf.getKey());
			params2.put("version", (pdf.getVersion() - 1) + "");
			ProcessDefinitionExt preVersionProcessDefinitionExt = activitiMapper
					.getPreVersionProcessDefinitionExt(params2);
			if (preVersionProcessDefinitionExt != null) {
				params2.put("oldpid", preVersionProcessDefinitionExt.getId());
				params2.put("newpid", pdf.getId());
				userMapper.updateUserProcdef(params2);
			}
		}
		Integer addProcessDefinitionExt = activitiMapper.addProcessDefinitionExt(pdes);
		return addProcessDefinitionExt;
	}

	public JSONObject lcdyList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<ProcessDefinitionExt> processDefinitionExts = activitiMapper.getProcessDefinitionExts(params);
		PageInfo<ProcessDefinitionExt> pageInfo = new PageInfo<>(processDefinitionExts);
		return LayUiUtil.pagination(pageInfo);
	}

	public JSONObject lcflList() {
		List<ProcessDefinitionType> processDefinitionTypes = activitiMapper.getProcessDefinitionTypes();
		JSONObject result = new JSONObject();
		JSONArray data = new JSONArray();
		JSONArray children = new JSONArray();
		for (ProcessDefinitionType pt : processDefinitionTypes) {
			JSONObject groupJ = new JSONObject();
			groupJ.put("id", pt.getType());
			groupJ.put("title", pt.getName());
			groupJ.put("parentId", "0");
			groupJ.put("children", children);
			data.add(groupJ);
		}
		result.put("data", data);
		JSONObject status = new JSONObject();
		status.put("code", 200);
		status.put("message", "操作成功");
		result.put("status", status);
		return result;
	}

	public JSONObject lcflListByUid() {
		List<ProcessDefinitionType> processDefinitionTypes = activitiMapper.getProcessDefinitionTypes();
		JSONObject result = new JSONObject();
		JSONArray data = new JSONArray();
		for (ProcessDefinitionType pt : processDefinitionTypes) {
			JSONObject groupJ = new JSONObject();
			groupJ.put("id", pt.getType());
			groupJ.put("title", pt.getName());
			groupJ.put("parentId", "0");
			List<ProcessDefinitionExt> processDefinitionTypesByProType = activitiMapper
					.getProcessDefinitionExtByProType(pt.getType());
			JSONArray childrenj = new JSONArray();
			for (ProcessDefinitionExt pde : processDefinitionTypesByProType) {
				JSONObject cJ = new JSONObject();
				cJ.put("id", pde.getKey());
				cJ.put("title", pde.getName());
				cJ.put("parentId", pt.getType());
				childrenj.add(cJ);
			}
			groupJ.put("children", childrenj);
			data.add(groupJ);
		}
		result.put("data", data);
		JSONObject status = new JSONObject();
		status.put("code", 200);
		status.put("message", "操作成功");
		result.put("status", status);
		return result;
	}

	@Transactional
	public Integer lcflAdd(String name) {
		ProcessDefinitionType pt = new ProcessDefinitionType();
		pt.setName(name);
		String type = UUID.randomUUID().toString();
		pt.setType(type);
		return activitiMapper.addProcessDefinitionType(pt);
	}

	@Transactional
	public Integer lcflEdit(ProcessDefinitionType pt) {
		return activitiMapper.addProcessDefinitionType(pt);
	}

	@Autowired
	UserSprMapper userSprMapper;

	@Transactional
	public long lcdyDel(Map<String, String> params) {
		boolean sfjl = "0".equals(params.get("sfjl")) ? false : true;
		if (!sfjl) {
			long count = hs.createHistoricProcessInstanceQuery().deploymentId(params.get("deployment_id")).unfinished()
					.count();
			if (count > 0) {
				return 0 - count;
			}
		}
		repositoryService.deleteDeployment(params.get("deployment_id"), sfjl);
		Map<String, String> params2 = new HashMap<>();
		params2.put("key", params.get("key"));
		params2.put("version", (Integer.parseInt(params.get("version")) - 1) + "");
		ProcessDefinitionExt preVersionProcessDefinitionExt = activitiMapper.getPreVersionProcessDefinitionExt(params2);
		if (preVersionProcessDefinitionExt != null) {
			params2.put("newpid", preVersionProcessDefinitionExt.getId());
			params2.put("oldpid", params.get("p_id"));
			userMapper.updateUserProcdef(params2);
		}
		return activitiMapper.delProcessDefinitionExt(params);
	}

	public String download(Map<String, String> params) {
		String fileName = "";
		String resource_name = params.get("resource_name");
		String deploymentId = params.get("deployment_id");
		String fileDir = resource_name.split("\\.")[0];
		String dir = props.getTempPath() + "/" + fileDir;
		if (new File(dir).mkdirs()) {
			List<String> deployResourceNameByDepId = activitiMapper.getDeployResourceNameByDepId(deploymentId);
			for (String resourceName : deployResourceNameByDepId) {
				InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
				FileUtil.saveFileFromInputStream(resourceAsStream, dir, resourceName);
			}
			try {
				ZipUtils.toZip(dir, new FileOutputStream(dir + ".zip"), false);
				fileName = fileDir + ".zip";
				FileUtil.deleteDirectory(dir);
			} catch (FileNotFoundException | RuntimeException e) {
				e.printStackTrace();
			}
		}
		return fileName;
	}

	public String preview(Map<String, String> params) {
		String processDefinitionId = params.get("id");
		String dgrm_resource_name = params.get("dgrm_resource_name");
		String dir = props.getTempPath();
		if (!new File(dir).exists()) {
			new File(dir).mkdir();
		}
		InputStream processDiagram = repositoryService.getProcessDiagram(processDefinitionId);
		FileUtil.saveFileFromInputStream(processDiagram, dir, dgrm_resource_name);
		return dgrm_resource_name;
	}

	public String previewByBk(Map<String, String> params) {
		String processInstanceBusinessKey = params.get("businessKey");
		String dgrm_resource_name = processInstanceBusinessKey + ".png";

		HistoricProcessInstance historicProcessInstance = hs.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(processInstanceBusinessKey).singleResult();
		String processDefinitionId = historicProcessInstance.getProcessDefinitionId();
		String processInstanceId = historicProcessInstance.getId();

		List<String> executedActivityIdList = new ArrayList<>();
		List<HistoricActivityInstance> highLightedActivities = hs.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).orderByHistoricActivityInstanceId().asc().list();

		for (HistoricActivityInstance activityInstance : highLightedActivities) {
			executedActivityIdList.add(activityInstance.getActivityId());
		}

		List<HistoricProcessInstance> historicFinishedProcessInstances = hs.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).finished().list();

		ProcessDiagramGenerator processDiagramGenerator = null;

		// 如果还没完成，流程图高亮颜色为绿色，如果已经完成为红色
		if (!CollectionUtils.isEmpty(historicFinishedProcessInstances)) {
			// 如果不为空，说明已经完成
			processDiagramGenerator = pes.getProcessEngineConfiguration().getProcessDiagramGenerator();
		} else {
			processDiagramGenerator = new CustomProcessDiagramGenerator();
		}

		BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);
		// 高亮流程已发生流转的线id集合
		List<String> highLightedFlows = getHighLightedFlows(bpmnModel, highLightedActivities);

		String fontName = "宋体";

		// 使用默认配置获得流程图表生成器，并生成追踪图片字符流
		InputStream generateDiagram = processDiagramGenerator.generateDiagram(bpmnModel, "png", executedActivityIdList,
				highLightedFlows, fontName, fontName, fontName, null, 1.0);

		String dir = props.getTempPath();
		if (!new File(dir).exists()) {
			new File(dir).mkdir();
		}
		FileUtil.saveFileFromInputStream(generateDiagram, dir, dgrm_resource_name);
		return dgrm_resource_name;
	}

	public JSONObject historyTask(String businessKey) {
		List<HistoricTaskInstance> list = hs.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey)
				.finished().orderByTaskCreateTime().asc().list();
		List<HistoricTaskInstanceVO> historicTaskInstanceVOs = HistoricTaskInstanceVO.getHistoricTaskInstanceVOs(list);
		for (HistoricTaskInstanceVO vo : historicTaskInstanceVOs) {
			String u_id = null;
			if (vo.getOwner() != null) {
				u_id = vo.getOwner();
			}
			if (vo.getAssignee() != null) {
				u_id = vo.getAssignee();
			}
			if (u_id != null) {
				User user = userMapper.getUserById(u_id);
				vo.setAssignee(user.getNickname());
			}
			String spyj = "";
			boolean sftg = true;
			List<Comment> taskComments = taskService.getTaskComments(vo.getId(), "comment");
			for (Comment comment : taskComments) {
				JSONObject commentJ = JSON.parseObject(comment.getFullMessage());
				sftg = commentJ.getBoolean("agree");
				Set<String> keySet = commentJ.keySet();
				for (String string : keySet) {
					if (string.endsWith("spyj")) {
						spyj = commentJ.getString(string);
						break;
					}
				}
			}
			vo.setSftg(sftg);
			vo.setSpyj(spyj);
		}
		Task ruTask = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).singleResult();
		if (ruTask != null) {
			HistoricTaskInstanceVO vo = new HistoricTaskInstanceVO();
			vo.setId(ruTask.getId());
			vo.setDeleteReason(null);
			vo.setName(ruTask.getName());
			vo.setStartTime(TimeUtil.getTimeFromDate("yyyy-MM-dd HH:mm:ss", ruTask.getCreateTime()));
			if (ruTask.getAssignee() != null) {
				User user = userMapper.getUserById(ruTask.getAssignee());
				vo.setAssignee(user.getNickname());
			}
			historicTaskInstanceVOs.add(vo);
		}

		return LayUiUtil.pagination(historicTaskInstanceVOs.size(), historicTaskInstanceVOs);
	}

	@Autowired
	TaskService taskService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	IdentityService is;

	@Autowired
	HistoryService hs;

	@Transactional
	public Integer deleteProcessInstance(String processInstanceId, String deleteReason) {
		runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
		return 1;
	}

	@Transactional
	public Integer pauseAndPlay(String event, String processInstanceId) {
		if ("play".equals(event)) {
			runtimeService.activateProcessInstanceById(processInstanceId);
		} else {
			runtimeService.suspendProcessInstanceById(processInstanceId);
		}
		return 1;
	}

	@Transactional
	public JSONObject startProcessInstance(String key, Map<String, String> params) {
		User user = SessionUtils.getUser();
		JSONObject result = new JSONObject();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.latestVersion().singleResult();
		is.setAuthenticatedUserId(user.getU_id() + "");

		ProcessInstance pi = formService.submitStartFormData(pd.getId(), params.get("bk"), params);

		userSprMapper.updateSprs(params);

		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();

		// 添加附件
		List<String> readFilePath = FileUtil.readFilePath(props.getTempPath(), "&" + params.get("bk") + "&");
		for (String fileName : readFilePath) {
			String filePath = props.getTempPath() + File.separator + fileName;
			String attachmentType = fileName.split("&" + params.get("bk") + "&")[0];
			String attachmentName = fileName.split("&" + params.get("bk") + "&")[1];
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(new File(filePath));
				taskService.createAttachment(attachmentType, task.getId(), pi.getId(), attachmentName,
						fileName.split("&")[1], fileInputStream);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} finally {
				if (fileInputStream != null) {
					try {
						fileInputStream.close();
						FileUtil.delete(filePath);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		params.put("proid", pi.getId());
		Integer saveBusiness = saveBusiness(key, params);

		result.put("code", saveBusiness);
		result.put("msg", task.getName());
		result.put("bk", params.get("bk"));
		return result;
	}

	@Transactional
	public JSONObject submitTaskFormData(String taskId, Map<String, String> params) {
		User user = SessionUtils.getUser();
		JSONObject result = new JSONObject();
		String bk = params.get("bk");

		// 保存审批备注表
		Task ruTask = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = ruTask.getProcessInstanceId();
		is.setAuthenticatedUserId(user.getU_id() + "");
		taskService.addComment(taskId, processInstanceId, JSON.toJSONString(params));

		// 完成当前任务
		formService.submitTaskFormData(taskId, params);

		// 更新历史UserTask表
		params.put("assignee", ruTask.getAssignee());
		params.put("taskId", taskId);
		activitiMapper.updateHiTaskInst(params);

		Task task = taskService.createTaskQuery().processInstanceBusinessKey(bk).singleResult();
		if (task != null) {
			// 更新审批人缓存表
			userSprMapper.updateSprs(params);
			result.put("code", 1);
			result.put("msg", task.getName());
		} else {
			result.put("code", 0);
		}
		result.put("taskId", taskId);
		return result;
	}

	public Integer saveBusiness(String key, Map<String, String> params) {
		Integer add = null;
		if ("leave".equals(key)) {
			Leave leave = JSON.parseObject(JSON.toJSONString(params), Leave.class);
			add = leaveMapper.addLeave(leave);
		}
		return add;
	}

	public JSONObject getHistoricProcessInstances(Integer page, Integer limit, Map<String, String> params) {
		HistoricProcessInstanceQuery hpiq = hs.createHistoricProcessInstanceQuery();
		String u_id = params.get("u_id");

		String businessKey = params.get("businessKey");
		String dateS = params.get("dateS");
		String dateE = params.get("dateE");
		String keys = params.get("keys");
		String finish = params.get("finish");
		if (finish != null && !"".equals(finish)) {
			if ("0".equals(finish)) {
				hpiq.unfinished();
			} else {
				hpiq.finished();
			}
		}
		if (u_id != null && !"".equals(u_id)) {
			hpiq.startedBy(u_id);
		}
		if (businessKey != null && !"".equals(businessKey)) {
			hpiq.processInstanceBusinessKey(businessKey);
		}
		if (keys != null && !"".equals(keys)) {
			hpiq.processDefinitionKeyIn(Arrays.asList(keys.split(",")));
		}
		if (dateS != null && !"".equals(dateS)) {
			hpiq.startedAfter(TimeUtil.getDateByCustom("yyyy-MM-dd HH:mm:ss", dateS + " 00:00:00"));
		}
		if (dateE != null && !"".equals(dateE)) {
			hpiq.startedBefore(TimeUtil.getDateByCustom("yyyy-MM-dd HH:mm:ss", dateE + " 23:59:59"));
		}
		long count = hpiq.count();
		hpiq = hpiq.orderByProcessInstanceStartTime().desc();
		List<HistoricProcessInstance> hps = hpiq.listPage((page - 1) * limit, page * limit);
		List<HistoricProcessInstanceVO> historicProcessInstanceVOs = HistoricProcessInstanceVO
				.getHistoricProcessInstanceVOs(hps);
		for (HistoricProcessInstanceVO hio : historicProcessInstanceVOs) {
			hio.setSuspended(false);
			Task ruTask = taskService.createTaskQuery().processInstanceId(hio.getProcessInstanceId()).suspended()
					.singleResult();
			if (ruTask != null) {
				hio.setSuspended(true);
			}
			if (!"".endsWith(hio.getEndTime()) && hio.getDeleteReason() == null) {
				List<Comment> comments = taskService.getProcessInstanceComments(hio.getProcessInstanceId(), "comment");
				hio.setSftg(JSON.parseObject(comments.get(0).getFullMessage()).getBoolean("agree"));
			}
			if (params.get("u_id") == null && hio.getOwerId() != null) {
				User user = userMapper.getUserById(hio.getOwerId());
				hio.setOwerId(user.getNickname());
			}
		}
		return LayUiUtil.pagination(count, historicProcessInstanceVOs);
	}

	private List<String> getHighLightedFlows(BpmnModel bpmnModel,
			List<HistoricActivityInstance> historicActivityInstances) {
		// 高亮流程已发生流转的线id集合
		List<String> highLightedFlowIds = new ArrayList<>();
		// 全部活动节点
		List<FlowNode> historicActivityNodes = new ArrayList<>();
		// 已完成的历史活动节点
		List<HistoricActivityInstance> finishedActivityInstances = new ArrayList<>();

		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess()
					.getFlowElement(historicActivityInstance.getActivityId(), true);
			historicActivityNodes.add(flowNode);
			if (historicActivityInstance.getEndTime() != null) {
				finishedActivityInstances.add(historicActivityInstance);
			}
		}

		FlowNode currentFlowNode = null;
		FlowNode targetFlowNode = null;
		// 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
		for (HistoricActivityInstance currentActivityInstance : finishedActivityInstances) {
			// 获得当前活动对应的节点信息及outgoingFlows信息
			currentFlowNode = (FlowNode) bpmnModel.getMainProcess()
					.getFlowElement(currentActivityInstance.getActivityId(), true);
			List<SequenceFlow> sequenceFlows = currentFlowNode.getOutgoingFlows();

			/**
			 * 遍历outgoingFlows并找到已已流转的 满足如下条件认为已已流转：
			 * 1.当前节点是并行网关或兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
			 * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转
			 */
			if ("parallelGateway".equals(currentActivityInstance.getActivityType())
					|| "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
				// 遍历历史活动节点，找到匹配流程目标节点的
				for (SequenceFlow sequenceFlow : sequenceFlows) {
					targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(),
							true);
					if (historicActivityNodes.contains(targetFlowNode)) {
						highLightedFlowIds.add(targetFlowNode.getId());
					}
				}
			} else {
				List<Map<String, Object>> tempMapList = new ArrayList<>();
				for (SequenceFlow sequenceFlow : sequenceFlows) {
					for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
						if (historicActivityInstance.getActivityId().equals(sequenceFlow.getTargetRef())) {
							Map<String, Object> map = new HashMap<>();
							map.put("highLightedFlowId", sequenceFlow.getId());
							map.put("highLightedFlowStartTime", historicActivityInstance.getStartTime().getTime());
							tempMapList.add(map);
						}
					}
				}

				if (!CollectionUtils.isEmpty(tempMapList)) {
					// 遍历匹配的集合，取得开始时间最早的一个
					long earliestStamp = 0L;
					String highLightedFlowId = null;
					for (Map<String, Object> map : tempMapList) {
						long highLightedFlowStartTime = Long.valueOf(map.get("highLightedFlowStartTime").toString());
						if (earliestStamp == 0 || earliestStamp >= highLightedFlowStartTime) {
							highLightedFlowId = map.get("highLightedFlowId").toString();
							earliestStamp = highLightedFlowStartTime;
						}
					}
					highLightedFlowIds.add(highLightedFlowId);
				}

			}

		}
		return highLightedFlowIds;
	}

	public JSONObject dbsxList(Integer page, Integer limit, Map<String, Object> params) {
		Object keys = params.get("keys");
		if (keys != null && !"".equals(keys)) {
			params.put("keys", Arrays.asList(keys.toString().split(",")));
		}
		PageHelper.startPage(page, limit);
		List<Map<String, Object>> list = activitiMapper.getTodoTaskAndToClaimTask(params);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(list);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer claimTask(String taskId, String u_id) {
		if (u_id != null) {
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
			if (task.getAssignee() == null) {
				taskService.claim(taskId, u_id);
				return 1;
			} else {
				return 2;
			}
		} else {
			List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
			if (identityLinksForTask.size() > 1) {
				taskService.setAssignee(taskId, null);
				return 3;
			} else {
				return 0;
			}
		}
	}

	@Transactional
	public Integer delegateTask(String taskId, String u_id) {
		return 1;
	}

}
