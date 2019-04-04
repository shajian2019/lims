package com.zzhb.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
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
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.async.AsyncService;
import com.zzhb.config.Props;
import com.zzhb.domain.Table;
import com.zzhb.domain.User;
import com.zzhb.domain.activiti.HistoricProcessInstanceVO;
import com.zzhb.domain.activiti.HistoricTaskInstanceVO;
import com.zzhb.domain.activiti.ProcessDefinitionExt;
import com.zzhb.domain.activiti.ProcessDefinitionType;
import com.zzhb.mapper.ActivitiMapper;
import com.zzhb.mapper.TableMapper;
import com.zzhb.mapper.UserMapper;
import com.zzhb.mapper.UserSprMapper;
import com.zzhb.utils.CustomProcessDiagramGenerator;
import com.zzhb.utils.FileUtil;
import com.zzhb.utils.LayUiUtil;
import com.zzhb.utils.PdfUtil;
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
	ProcessEngine pes;

	@Autowired
	UserMapper userMapper;

	@Autowired
	AsyncService asyncService;
	
	@Autowired
	UserService userService;

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
			pde.setZdcxsc(params.get("num"));
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
			groupJ.put("sort", pt.getSort());
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
	public Integer lcflAdd(ProcessDefinitionType pt) {
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
		String version = (Integer.parseInt(params.get("version")) - 1) + "";
		if ("0".equals(version)) {
			userMapper.delUserProcdef(params.get("p_id"));
		} else {
			Map<String, String> params2 = new HashMap<>();
			params2.put("key", params.get("key"));
			params2.put("version", version);
			ProcessDefinitionExt preVersionProcessDefinitionExt = activitiMapper
					.getPreVersionProcessDefinitionExt(params2);
			params2.put("newpid", preVersionProcessDefinitionExt.getId());
			params2.put("oldpid", params.get("p_id"));
			userMapper.updateUserProcdef(params2);
		}
		return activitiMapper.delProcessDefinitionExt(params);
	}

	@Transactional
	public Integer lcdyUpdate(Map<String, String> params) {
		return activitiMapper.updateProcessDefinitionExtById(params);
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
		// 查询已办理任务
		List<HistoricTaskInstance> list = hs.createHistoricTaskInstanceQuery().processInstanceBusinessKey(businessKey)
				.finished().orderByTaskCreateTime().asc().list();
		List<HistoricTaskInstanceVO> historicTaskInstanceVOs = HistoricTaskInstanceVO.getHistoricTaskInstanceVOs(list);
		for (HistoricTaskInstanceVO vo : historicTaskInstanceVOs) {
			if (vo.getAssignee() != null) {
				User user = userMapper.getUserById(vo.getAssignee());
				// 设置办理人
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

		// 查询进行中的任务
		List<Task> ruTasks = taskService.createTaskQuery().processInstanceBusinessKey(businessKey).list();
		if (ruTasks != null) {
			for (Task ruTask : ruTasks) {
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
		}

		return LayUiUtil.pagination(historicTaskInstanceVOs.size(), historicTaskInstanceVOs);
	}

	@Autowired
	TaskService taskService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	IdentityService identityService;

	@Autowired
	HistoryService hs;

	@Transactional
	public Integer deleteProcessInstance(String processInstanceId, String deleteReason, String key) {

		runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
		Table table = tableMapper.getTableByProcdefkey(key);
		String sql = "UPDATE " + table.getPrefix().trim() + table.getProcdefkey().trim() + " SET spjg = '2'"
				+ " WHERE proid = '" + processInstanceId + "'";
		return jdbcTemplate.update(sql);
	}

	@Transactional
	public JSONObject pauseAndPlay(String event, String processInstanceId) {
		JSONObject result = new JSONObject();
		ProcessInstance pi = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId)
				.singleResult();
		if (pi != null) {
			if ("play".equals(event)) {
				runtimeService.activateProcessInstanceById(processInstanceId);
			} else {
				runtimeService.suspendProcessInstanceById(processInstanceId);
			}
			result.put("code", 1);
			result.put("msg", "操作成功");
		} else {
			result.put("code", 2);
			result.put("msg", "流程已审批");
		}
		return result;
	}

	/**
	 * 
	 * @param key
	 *            流程KEY
	 * @param params
	 *            业务数据
	 * @return
	 */
	@Transactional
	public JSONObject startProcessInstance(String key, Map<String, String> params) {
		JSONObject result = new JSONObject();

		User user = SessionUtils.getUser();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.latestVersion().singleResult();
		identityService.setAuthenticatedUserId(user.getU_id() + "");

		ProcessInstance pi = formService.submitStartFormData(pd.getId(), params.get("bk"), params);

		// 更新审批人缓存表
		userSprMapper.updateSprs(params);

		// 为下一个任务节点添加附件
		List<String> readFilePath = FileUtil.readFilePath(props.getTempPath(), "&" + params.get("bk") + "&");
		for (String fileName : readFilePath) {
			String filePath = props.getTempPath() + File.separator + fileName;
			String attachmentType = fileName.split("&" + params.get("bk") + "&")[0];
			String attachmentName = fileName.split("&" + params.get("bk") + "&")[1];
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(new File(filePath));
				taskService.createAttachment(attachmentType, null, pi.getId(), attachmentName, fileName.split("&")[1],
						fileInputStream);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("===附件文件未找到==" + e.getMessage());
			} finally {
				try {
					fileInputStream.close();
					FileUtil.delete(filePath);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(pi.getId()).list();
		// 更新历史表
		for (Task task : tasks) {
			params.put("assignee", task.getAssignee());
			params.put("taskId", task.getId());
			activitiMapper.updateHiTaskInst(params);
		}

		// 保存业务数据
		params.put("proid", pi.getId());
		Integer saveBusiness = saveBusiness(key, params);

		result.put("code", saveBusiness);
		result.put("msg", tasks.get(0).getName());
		result.put("bk", params.get("bk"));
		return result;
	}

	// @Transactional
	public JSONObject submitTaskFormData(String taskId, Map<String, String> params) {
		User user = SessionUtils.getUser();
		JSONObject result = new JSONObject();
		String bk = params.get("bk").toString();//2019032914395010233

		Task ruTask = taskService.createTaskQuery().taskId(taskId).singleResult();//227838
		// 判断流程是否被挂起
		if (!ruTask.isSuspended()) {

			if (ruTask.getClaimTime() != null) {
				DelegationState delegationState = ruTask.getDelegationState();
				if (delegationState != null && delegationState.toString().equals("PENDING")) {
					// 委托
					taskService.resolveTask(taskId);
					User userById = userMapper.getUserById(ruTask.getAssignee());
					params.put("description", "领办-委托" + "【" + userById.getNickname() + "】");
					params.put("assignee", ruTask.getAssignee());
					params.put("owner", ruTask.getOwner());
				} else {
					if (ruTask.getDescription() != null) {
						// 领办-指派
						params.put("description", "领办-指派" + "【" + ruTask.getDescription() + "】");
						params.put("assignee", ruTask.getAssignee());
						params.put("owner", ruTask.getAssignee());
					} else {
						params.put("description", "领办");
						params.put("assignee", ruTask.getAssignee());
						params.put("owner", ruTask.getAssignee());
					}
				}
			} else {
				DelegationState delegationState = ruTask.getDelegationState();
				if (delegationState != null && delegationState.toString().equals("PENDING")) {
					// 委托
					taskService.resolveTask(taskId);
					User userById = userMapper.getUserById(ruTask.getAssignee());
					params.put("description", "委托" + "【" + userById.getNickname() + "】");
					params.put("assignee", ruTask.getAssignee());
					params.put("owner", ruTask.getOwner());
				} else {
					// 指派 或指定
					if (ruTask.getDescription() != null) {
						// 指派
						params.put("description", "指派" + "【" + ruTask.getDescription() + "】");
						params.put("assignee", ruTask.getAssignee());
						params.put("owner", ruTask.getAssignee());
					} else {
						params.put("description", "指定");
						params.put("assignee", ruTask.getAssignee());
						params.put("owner", ruTask.getAssignee());
					}
				}
			}
			String processInstanceId = ruTask.getProcessInstanceId();
			identityService.setAuthenticatedUserId(user.getU_id() + "");
			//判断当前流程的会签节点是否有意见
			List<Comment> comments = taskService.getProcessInstanceComments(processInstanceId, "comment");
			SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
			 String countersignature="";
			for(Comment comment : comments) {
				String str= comment.getFullMessage();
				String time = sdf.format(comment.getTime());
				 Map maps = (Map)JSON.parse(str);  
			        for (Object map : maps.entrySet()){  
			        	String key = map.toString();
			        	//之前的会签意见
			        	if(key.contains("hq_spyj")) {
			        		String assigneeId = (String) maps.get("assignee");
			        		String assignName= userService.getUserNameByUserId(assigneeId);
			        		countersignature += (String) maps.get("hq_spyj")+"("+assignName+"  "+time+")"+"\n";
			        	}
			        }  
			}
	        if(countersignature != null && countersignature != "") {
	        	String message1=JSON.toJSONString(params);
	        	Map mapNew = JSON.parseObject(message1,Map.class);
	        	//新的会签意见
	        	String countersignature1=(String) mapNew.get("hq_spyj")+"("+user.getNickname()+"  "+sdf.format(new Date())+")"+"\n";
	        	countersignature1=countersignature+countersignature1;
	        	params.put("hq_spyj", countersignature1);
	        	taskService.addComment(taskId, processInstanceId, JSON.toJSONString(params));
	        }else {
	        	// 保存审批备注表
	        	taskService.addComment(taskId, processInstanceId, JSON.toJSONString(params));
	        }
	        
			// 完成当前任务
			formService.submitTaskFormData(taskId, params);

			// 更新已办任务的历史表
			params.put("taskId", taskId);
			activitiMapper.updateHiTaskInst(params);

			String taskDefinitionKey1 = ruTask.getTaskDefinitionKey();
			List<Task> tasks = taskService.createTaskQuery().processInstanceBusinessKey(bk).list();
			if (tasks.size() == 1) {
				// 更新审批人缓存表
				if (params.get("usersprs") != null && params.get("id") != null) {
					userSprMapper.updateSprs(params);
				}
				Task task = tasks.get(0);

				// 更新待办任务的历史表
				params.clear();
				params.put("assignee", task.getAssignee());
				params.put("taskId", task.getId());
				activitiMapper.updateHiTaskInst(params);

				String taskDefinitionKey2 = task.getTaskDefinitionKey();
				if (taskDefinitionKey1.equals(taskDefinitionKey2)) {
					// 会签用户任务
					result.put("code", 0);
					result.put("msg", "完成审批！");
				} else {
					result.put("code", 1);
					result.put("msg", task.getName());
				}
			} else if (tasks.size() == 0) {
				String agree = params.get("agree");
				if (agree != null) {
					agree = agree.equals("true") ? "1" : "0";
					String processDefinitionId = ruTask.getProcessDefinitionId();
					Table table = tableMapper.getTableByProcdefkey(processDefinitionId.split(":")[0]);
					String sql = "UPDATE " + table.getPrefix().trim() + table.getProcdefkey().trim() + " SET spjg = '"
							+ agree + "'" + " where bk = '" + bk + "'";
					logger.debug("========更新业务表=========" + sql);
					jdbcTemplate.update(sql);
				}
				asyncService.sendMessage(bk);
				result.put("code", 0);
				result.put("msg", "完成审批！");
			} else {
				result.put("code", 0);
				result.put("msg", "完成审批！");
			}
		} else {
			result.put("code", -1);
			result.put("msg", "流程已挂起！");
		}
		result.put("taskId", taskId);
		return result;
	}

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	TableMapper tableMapper;

	public Integer saveBusiness(String key, Map<String, String> params) {
		Table table = tableMapper.getTableByProcdefkey(key);
		if (table != null) {
			Set<String> set = new HashSet<>();
			String cols = table.getCols().trim();
			List<String> asList = Arrays.asList(cols.split(","));
			for (String string : asList) {
				set.add(string.trim());
			}
			cols = "";
			String values = "";
			for (String string : set) {
				cols += string + ",";
				String value = params.get(string) == null ? "" : params.get(string);
				values += "'" + value + "'" + ",";
			}
			cols = " (" + cols.substring(0, cols.length() - 1) + ") ";
			values = " (" + values.substring(0, values.length() - 1) + ") ";

			String sql = "INSERT INTO " + table.getPrefix().trim() + table.getProcdefkey().trim() + cols + "VALUES"
					+ values;
			logger.debug("======saveBusiness=======" + sql);
			return jdbcTemplate.update(sql);
		} else {
			return 0;
		}
	}

	// 我发起的流程查询
	public JSONObject getHistoricProcessInstances(Integer page, Integer limit, Map<String, String> params) {
		HistoricProcessInstanceQuery hpiq = hs.createHistoricProcessInstanceQuery();
		String u_id = params.get("u_id");

		String businessKey = params.get("businessKey");
		String dateS = params.get("dateS");
		String dateE = params.get("dateE");
		String keys = params.get("keys");
		String finish = params.get("finish");

		if (finish != null && !"".equals(finish)) {
			if ("1".equals(finish)) {
				hpiq.finished();
			} else {
				hpiq.unfinished();
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

			int size = taskService.getProcessInstanceAttachments(hio.getProcessInstanceId()).size();
			hio.setAttachments(size + "");

			Task ruTask = taskService.createTaskQuery().processInstanceId(hio.getProcessInstanceId()).suspended()
					.singleResult();
			if (ruTask != null) {
				hio.setSuspended(true);
			}
			if (!"".endsWith(hio.getEndTime()) && hio.getDeleteReason() == null) {
				List<Comment> comments = taskService.getProcessInstanceComments(hio.getProcessInstanceId(), "comment");
				hio.setSftg(JSON.parseObject(comments.get(0).getFullMessage()).getBoolean("agree") == null ? true
						: JSON.parseObject(comments.get(0).getFullMessage()).getBoolean("agree"));
			}
			if (params.get("u_id") == null && hio.getOwerId() != null) {
				User user = userMapper.getUserById(hio.getOwerId());
				if (user != null) {
					hio.setOwerId(user.getNickname());
				} else {
					hio.setOwerId("--");
				}
			}
		}
		return LayUiUtil.pagination(count, historicProcessInstanceVOs);
	}

	// 已办事项 查询
	public JSONObject getHistoricTaskInstance(Integer page, Integer limit, Map<String, String> params) {
		String ownerid = params.get("u_id");
		String dateS = params.get("dateS");
		String dateE = params.get("dateE");
		String keys = params.get("keys");
		String businessKey = params.get("businessKey");
		HistoricTaskInstanceQuery hit = hs.createHistoricTaskInstanceQuery().finished().taskOwner(ownerid);
		if (businessKey != null && !"".equals(businessKey)) {
			hit.processInstanceBusinessKeyLike(businessKey);
		}
		if (keys != null && !"".equals(keys)) {
			hit.processDefinitionKeyIn(Arrays.asList(keys.split(",")));
		}
		if (dateS != null && !"".equals(dateS)) {
			hit.taskCompletedAfter(TimeUtil.getDateByCustom("yyyy-MM-dd HH:mm:ss", dateS + " 00:00:00"));
		}
		if (dateE != null && !"".equals(dateE)) {
			hit.taskCompletedBefore(TimeUtil.getDateByCustom("yyyy-MM-dd HH:mm:ss", dateE + " 23:59:59"));
		}
		long count = hit.count();
		hit = hit.orderByHistoricTaskInstanceEndTime().desc();
		List<HistoricTaskInstance> list = hit.listPage((page - 1) * limit, page * limit);
		List<HistoricTaskInstanceVO> htvs = HistoricTaskInstanceVO.getHistoricTaskInstanceVOs(list);
		for (HistoricTaskInstanceVO htv : htvs) {
			Task ruTask = taskService.createTaskQuery().processInstanceId(htv.getProcessInstanceId()).singleResult();
			if (ruTask != null) {
				htv.setCurrentName(ruTask.getName());
			} else {
				htv.setCurrentName("--");
			}
			HistoricProcessInstance hpi = hs.createHistoricProcessInstanceQuery()
					.processInstanceId(htv.getProcessInstanceId()).singleResult();
			htv.setBusinessKey(hpi.getBusinessKey());
			htv.setLcsfjs(hpi.getEndTime() == null ? false : true);
			htv.setProcessDefinitionName(hpi.getProcessDefinitionName());
		}
		return LayUiUtil.pagination(count, htvs);
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
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (u_id != null) {
			if (task.getAssignee() == null && task.getClaimTime() == null) {
				taskService.claim(taskId, u_id);
				return 1;
			} else {
				return 2;
			}
		} else {
			List<IdentityLink> identityLinksForTask = taskService.getIdentityLinksForTask(taskId);
			Integer count = 0;
			for (IdentityLink identityLink : identityLinksForTask) {
				logger.debug(identityLink.getType() + "==" + identityLink.getUserId());
				if (!identityLink.getType().equals("owner")) {
					count++;
				}
			}
			if (count > 1) {
				Map<String, String> params = new HashMap<>();
				params.put("taskId", task.getId());
				// 放弃任务时 将领办时间设置为null
				activitiMapper.updateHiRuTaskWhenUnclaim(params);
				activitiMapper.updateRuTaskWhenUnclaim(params);
				taskService.setAssignee(taskId, null);
				return 3;
			} else {
				return 0;
			}
		}
	}

	@Transactional
	public JSONObject delegateTask(String taskId, String owerId, String userId) {
		JSONObject result = new JSONObject();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task != null) {
			String u_id = task.getAssignee();
			User user = userMapper.getUserById(u_id);
			// 添加委托人名称
			task.setDescription(user.getNickname());
			taskService.saveTask(task);
			taskService.delegateTask(taskId, userId);

			// 更新历史表
			Map<String, String> params = new HashMap<>();
			params.put("taskId", taskId);
			params.put("assignee", userId);
			activitiMapper.updateHiTaskInst(params);

			result.put("code", 1);
			result.put("msg", "任务委托成功");
		} else {
			result.put("code", -2);
			result.put("msg", "当前任务已失效");
		}
		return result;
	}

	@Transactional
	public JSONObject transferTask(String taskId, String owerId, String userId) {
		JSONObject result = new JSONObject();
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task != null) {
			String u_id = task.getAssignee();
			User user = userMapper.getUserById(u_id);
			// 添加指派人名称
			task.setDescription(user.getNickname());
			task.setOwner(owerId);
			taskService.saveTask(task);
			taskService.setAssignee(taskId, userId);

			// 更新历史表
			Map<String, String> params = new HashMap<>();
			params.put("taskId", taskId);
			params.put("assignee", userId);
			activitiMapper.updateHiTaskInst(params);

			result.put("code", 1);
			result.put("msg", "任务指派成功");
		} else {
			result.put("code", -2);
			result.put("msg", "当前任务已失效");
		}
		return result;
	}

	@Transactional
	public JSONObject revoke(String userId, String bk) {
		JSONObject result = new JSONObject();
		Task task = taskService.createTaskQuery().processInstanceBusinessKey(bk).singleResult();
		if (task == null) {
			result.put("code", -1);
			result.put("msg", "流程已执行完成，无法撤回");
		} else {
			List<HistoricTaskInstance> htiList = hs.createHistoricTaskInstanceQuery().processInstanceBusinessKey(bk)
					.orderByTaskCreateTime().asc().list();

			String myTaskId = null;
			HistoricTaskInstance myTask = null;
			for (HistoricTaskInstance hti : htiList) {
				if (userId.equals(hti.getAssignee()) || userId.equals(hti.getOwner())) {
					myTaskId = hti.getId();
					myTask = hti;
					break;
				}
			}
			if (null == myTaskId) {
				result.put("code", -1);
				result.put("msg", "任务已被完成，无法撤回");
			} else {
				hs.deleteHistoricTaskInstance(myTaskId);
				String processDefinitionId = myTask.getProcessDefinitionId();
				BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinitionId);

				String myActivityId = null;
				List<HistoricActivityInstance> haiList = hs.createHistoricActivityInstanceQuery()
						.executionId(myTask.getExecutionId()).orderByHistoricActivityInstanceStartTime().desc().list();
				List<String> delList = new ArrayList<>();
				for (HistoricActivityInstance hai : haiList) {
					if (hai.getEndTime() != null && "userTask".equals(hai.getActivityType())) {
						myActivityId = hai.getActivityId();
						break;
					} else {
						delList.add(hai.getId());
					}
				}
				activitiMapper.delHiActInst(delList);

				FlowNode myFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(myActivityId);

				org.activiti.engine.runtime.Execution execution = runtimeService.createExecutionQuery()
						.executionId(task.getExecutionId()).singleResult();
				String activityId = execution.getActivityId();

				FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId);

				// 记录原活动方向
				List<SequenceFlow> oriSequenceFlows = new ArrayList<SequenceFlow>();
				oriSequenceFlows.addAll(flowNode.getOutgoingFlows());

				// 清理活动方向
				flowNode.getOutgoingFlows().clear();

				// 建立新方向
				List<SequenceFlow> newSequenceFlowList = new ArrayList<SequenceFlow>();
				SequenceFlow newSequenceFlow = new SequenceFlow();
				newSequenceFlow.setId("newSequenceFlowId");
				newSequenceFlow.setSourceFlowElement(flowNode);
				newSequenceFlow.setTargetFlowElement(myFlowNode);
				newSequenceFlowList.add(newSequenceFlow);
				flowNode.setOutgoingFlows(newSequenceFlowList);

				JSONObject comment = new JSONObject();
				comment.put("agree", "false");
				User userById = userMapper.getUserById(userId);
				comment.put("cancel_spyj", "撤回" + "【" + userById.getNickname() + "】");
				Authentication.setAuthenticatedUserId(userId);
				taskService.addComment(task.getId(), task.getProcessInstanceId(), JSON.toJSONString(comment));

				Map<String, Object> currentVariables = new HashMap<String, Object>();
				currentVariables.put("sprs", userId);
				// 完成任务
				taskService.complete(task.getId(), currentVariables);
				// 恢复原方向
				flowNode.setOutgoingFlows(oriSequenceFlows);
				result.put("code", 0);
				result.put("msg", "任务撤回成功");
			}
		}
		return result;
	}

	public JSONObject createPdf(Map<String, String> params) {

		JSONObject result = new JSONObject();

		String key = params.get("key");
		String bk = params.get("bk");
		String pdfName = key + "_" + bk + ".pdf";

		String tempPath = "static/pdftemp/" + key + "_temp.pdf";
		if (!new File(props.getTempPath()).exists()) {
			new File(props.getTempPath()).mkdirs();
		}
		String outPdfPath = props.getTempPath() + File.separator + pdfName;

		Map<String, String> data = new HashMap<>();
		List<HistoricTaskInstance> list = hs.createHistoricTaskInstanceQuery().processInstanceBusinessKey(bk).finished()
				.orderByTaskCreateTime().asc().list();
		Table table = tableMapper.getTableByProcdefkey(key);
		if (table == null) {
			result.put("code", 0);
			result.put("msg", key+"未维护sys_t_table表");
		} else {
			String sql = "SELECT * FROM " + table.getPrefix().trim() + table.getProcdefkey().trim();
			sql += " WHERE bk = '" + bk + "'";
			Map<String, Object> map = jdbcTemplate.queryForMap(sql);
			if ("leave".equals(key)) {
				data.put("sqr", map.get("sqr").toString());
				data.put("bmmc", map.get("bmmc").toString());

				String ksrq = map.get("ksrq").toString();
				data.put("ksrq_year", ksrq.substring(0, 4));
				data.put("ksrq_month", ksrq.substring(5, 7));
				data.put("ksrq_day", ksrq.substring(8, 10));

				String jsrq = map.get("jsrq").toString();
				data.put("jsrq_year", jsrq.substring(0, 4));
				data.put("jsrq_month", jsrq.substring(5, 7));
				data.put("jsrq_day", jsrq.substring(8, 10));

				data.put("qjlx", map.get("qjlx").toString());
				data.put("qjly", map.get("qjly").toString());

				for (HistoricTaskInstance hi : list) {
					String endtime = TimeUtil.getTimeByCustom("yyyy-MM-dd", hi.getEndTime());
					List<Comment> taskComments = taskService.getTaskComments(hi.getId(), "comment");
					for (Comment comment : taskComments) {
						JSONObject commentJ = JSON.parseObject(comment.getFullMessage());
						Set<String> keySet = commentJ.keySet();
						for (String string : keySet) {
							if (string.endsWith("spyj")) {
								String assignee = commentJ.getString("assignee");
								if (assignee != null) {
									data.put(string, commentJ.getString(string));
									String pre = string.split("_")[0] + "_";
									data.put(pre + "year", endtime.substring(0, 4));
									data.put(pre + "month", endtime.substring(5, 7));
									data.put(pre + "day", endtime.substring(8, 10));
									User userById = userMapper.getUserById(assignee);
									data.put(pre + "spr", userById.getNickname());
									logger.debug("===data===" + JSON.toJSONString(data));
								}
								break;
							}else if (string.endsWith("baqk")) {
								String assignee = commentJ.getString("assignee");
								if (assignee != null) {
									data.put(string, commentJ.getString(string));
									String pre = "ba_";
									data.put(pre + "year", endtime.substring(0, 4));
									data.put(pre + "month", endtime.substring(5, 7));
									data.put(pre + "day", endtime.substring(8, 10));
									User userById = userMapper.getUserById(assignee);
									data.put(pre + "spr", userById.getNickname());
									logger.debug("===data===" + JSON.toJSONString(data));
								}
							}else if (string.endsWith("xjqk")) {
								String assignee = commentJ.getString("assignee");
								if (assignee != null) {
									data.put(string, commentJ.getString(string));
									String pre = "xj_";
									data.put(pre + "year", endtime.substring(0, 4));
									data.put(pre + "month", endtime.substring(5, 7));
									data.put(pre + "day", endtime.substring(8, 10));
									User userById = userMapper.getUserById(assignee);
									data.put(pre + "spr", userById.getNickname());
									logger.debug("===data===" + JSON.toJSONString(data));
								}
							}
						}
					}
				}
			} else if ("chapter".equals(key)) {
				data.put("sqr", map.get("sqr").toString());
				data.put("bmmc", map.get("bmmc").toString());
				data.put("sqrq", map.get("sqrq").toString());
				data.put("yylx", map.get("yylx").toString());
				data.put("yyfs", map.get("yyfs").toString());
				data.put("yynr", map.get("yynr").toString());
				for (HistoricTaskInstance hi : list) {
					String endtime = TimeUtil.getTimeByCustom("yyyy-MM-dd", hi.getEndTime());
					// 查询审批意见
					List<Comment> taskComments = taskService.getTaskComments(hi.getId(), "comment");
					for (Comment comment : taskComments) {
						JSONObject commentJ = JSON.parseObject(comment.getFullMessage());
						Set<String> keySet = commentJ.keySet();
						for (String string : keySet) {
							if (string.endsWith("spyj")) {
								String assignee = commentJ.getString("assignee");
								if (assignee != null) {
									data.put(string, commentJ.getString(string));
									String pre = string.split("_")[0] + "_";
									data.put(pre + "year", endtime.substring(0, 4));
									data.put(pre + "month", endtime.substring(5, 7));
									data.put(pre + "day", endtime.substring(8, 10));
									User userById = userMapper.getUserById(assignee);
									data.put(pre + "spr", userById.getNickname());
									logger.debug("===data===" + JSON.toJSONString(data));
								}
								break;
							}
						}
					}
				}
			} else if ("docreadlist".equals(key)) {
				List<User> usersInOneOrgs = userMapper.getUsersInOneOrg("局领导");
				Map<String, Integer> tempMap = new HashMap<>();
				for (int i = 0; i < usersInOneOrgs.size(); i++) {
					data.put("spr" + i, usersInOneOrgs.get(i).getNickname());
					tempMap.put(usersInOneOrgs.get(i).getU_id() + "", i);
				}
				data.put("swrq", map.get("swrq").toString());
				data.put("fwjg", map.get("fwjg").toString());
				data.put("lwzh", map.get("lwzh").toString());
				data.put("lwxz", map.get("lwxz").toString());
				data.put("lwbh", map.get("lwbh").toString());
				data.put("wjbt", map.get("wjbt").toString());
				data.put("nb", map.get("nb").toString());
				String spyj = "";
				for (HistoricTaskInstance hi : list) {
					String endtime = TimeUtil.getTimeByCustom("MM/dd", hi.getEndTime());
					String starttime = TimeUtil.getTimeByCustom("MM/dd", hi.getStartTime());
					String assignee2 = hi.getAssignee();
					if (tempMap.containsKey(assignee2)) {
						data.put("spsj" + tempMap.get(assignee2), endtime);
						data.put("sdsj" + tempMap.get(assignee2), starttime);
					}
					List<Comment> taskComments = taskService.getTaskComments(hi.getId(), "comment");
					for (Comment comment : taskComments) {
						JSONObject commentJ = JSON.parseObject(comment.getFullMessage());
						Set<String> keySet = commentJ.keySet();
						for (String string : keySet) {
							if (string.endsWith("spyj")) {
								String assignee = commentJ.getString("assignee");
								User userById = userMapper.getUserById(assignee);
								spyj += userById.getNickname() + ": " + commentJ.getString(string)
										+ System.getProperty("line.separator");
								break;
							}
						}
					}
				}
				data.put("spyj", spyj);
			}else if ("assetsRecipients".equals(key)) {
				data.put("sqr", map.get("sqr").toString());
				data.put("bmmc", map.get("bmmc").toString());

				data.put("sqrq", map.get("sqrq").toString());
				data.put("bha", map.get("bha").toString());
				data.put("zcmca", map.get("zcmca").toString());
				data.put("xhpza", map.get("xhpza").toString());
				data.put("sla", map.get("sla").toString());
				data.put("yta", map.get("yta").toString());
				data.put("bza", map.get("bza").toString());
				
				data.put("bhb", map.get("bhb").toString());
				data.put("zcmcb", map.get("zcmcb").toString());
				data.put("xhpzb", map.get("xhpzb").toString());
				data.put("slb", map.get("slb").toString());
				data.put("ytb", map.get("ytb").toString());
				data.put("bzb", map.get("bzb").toString());
				
				data.put("bhc", map.get("bhc").toString());
				data.put("zcmcc", map.get("zcmcc").toString());
				data.put("xhpzc", map.get("xhpzc").toString());
				data.put("slc", map.get("slc").toString());
				data.put("ytc", map.get("ytc").toString());
				data.put("bzc", map.get("bzc").toString());
				
				data.put("bhd", map.get("bhd").toString());
				data.put("zcmcd", map.get("zcmcd").toString());
				data.put("xhpzd", map.get("xhpzd").toString());
				data.put("sld", map.get("sld").toString());
				data.put("ytd", map.get("ytd").toString());
				data.put("bzd", map.get("bzd").toString());
				
				data.put("bhe", map.get("bhe").toString());
				data.put("zcmce", map.get("zcmce").toString());
				data.put("xhpze", map.get("xhpze").toString());
				data.put("sle", map.get("sle").toString());
				data.put("yte", map.get("yte").toString());
				data.put("bze", map.get("bze").toString());

				for (HistoricTaskInstance hi : list) {
					String endtime = TimeUtil.getTimeByCustom("yyyy-MM-dd", hi.getEndTime());
					List<Comment> taskComments = taskService.getTaskComments(hi.getId(), "comment");
					for (Comment comment : taskComments) {
						JSONObject commentJ = JSON.parseObject(comment.getFullMessage());
						Set<String> keySet = commentJ.keySet();
						for (String string : keySet) {
							if (string.endsWith("spyj")) {
								String assignee = commentJ.getString("assignee");
								if (assignee != null) {
									data.put(string, commentJ.getString(string));
									String pre = string.split("_")[0] + "_";
									data.put(pre + "year", endtime.substring(0, 4));
									data.put(pre + "month", endtime.substring(5, 7));
									data.put(pre + "day", endtime.substring(8, 10));
									User userById = userMapper.getUserById(assignee);
									data.put(pre + "spr", userById.getNickname());
									logger.debug("===data===" + JSON.toJSONString(data));
								}
								break;
							}
						}
					}
				}
			} else if ("dispatchFile".equals(key)) {
				data.put("sqr", map.get("sqr").toString());
				data.put("bmmc", map.get("bmmc").toString());
				data.put("title", map.get("title").toString());
				data.put("copyMessage", map.get("copyMessage").toString());
				for (HistoricTaskInstance hi : list) {
					String endtime = TimeUtil.getTimeByCustom("yyyy-MM-dd", hi.getEndTime());
					List<Comment> taskComments = taskService.getTaskComments(hi.getId(), "comment");
					for (Comment comment : taskComments) {
						JSONObject commentJ = JSON.parseObject(comment.getFullMessage());
						Set<String> keySet = commentJ.keySet();
						for (String string : keySet) {
							if (string.endsWith("planOpinion")) {
								String ag = commentJ.getString("assignee");
								if (ag != null) {
									String pre = string.split("_")[0] + "_";
									data.put(string, commentJ.getString(string));
									data.put(pre + "year", endtime.substring(0, 4));
									data.put(pre + "day", endtime.substring(8, 10));
									data.put(pre + "month", endtime.substring(5, 7));
									User userById = userMapper.getUserById(ag);
									data.put(pre + "spr", userById.getNickname());
									logger.debug("===data===" + JSON.toJSONString(data));
								}
								break;
							}
						}
					}
				}
			}else if ("carapplication".equals(key)) {
				data.put("sqr", map.get("sqr").toString());
				data.put("bmmc", map.get("bmmc").toString());
				data.put("jsy", map.get("jsy").toString());
				data.put("clxh", map.get("clxh").toString());
				String ycrqq=map.get("ycrqq").toString();
				String ycrqz=map.get("ycrqz").toString();
				data.put("ycrqq", ycrqq !=null ? ycrqq.substring(0, 11):ycrqq);
				data.put("ycrqz", ycrqz !=null ? ycrqz.substring(0, 11):ycrqz);
				data.put("qd", map.get("qd").toString());
				data.put("zd", map.get("zd").toString());
				data.put("ycsy", map.get("ycsy").toString());

				for (HistoricTaskInstance hi : list) {
					String endtime = TimeUtil.getTimeByCustom("yyyy-MM-dd", hi.getEndTime());
					List<Comment> taskComments = taskService.getTaskComments(hi.getId(), "comment");
					for (Comment comment : taskComments) {
						JSONObject commentJ = JSON.parseObject(comment.getFullMessage());
						Set<String> keySet = commentJ.keySet();
						for (String string : keySet) {
							if (string.endsWith("spyj")) {
								String assignee = commentJ.getString("assignee");
								if (assignee != null) {
									data.put(string, commentJ.getString(string));
									String pre = string.split("_")[0] + "_";
									data.put(pre + "year", endtime.substring(0, 4));
									data.put(pre + "month", endtime.substring(5, 7));
									data.put(pre + "day", endtime.substring(8, 10));
									User userById = userMapper.getUserById(assignee);
									data.put(pre + "spr", userById.getNickname());
									logger.debug("===data===" + JSON.toJSONString(data));
								}
								break;
							}
						}
					}
				}
			}
			// 生成pdf
			String path = PdfUtil.createPdfByTemp(tempPath, outPdfPath, data);
			if (path != null) {
				result.put("code", 1);
				result.put("msg", pdfName);
			} else {
				result.put("code", 0);
				result.put("msg", "pdf生成失败");
			}
		}
		return result;
	}

		//统计请假总天数，以0.5天为单位,startDate为开始日期，endDate为结束日期，startId为开始上下午，"1"代表是上午，“2”代表下午，endId代表结束的上下午
		@Transactional
		public float getCount(Map<String, String> params) throws Exception{
			String startDate = params.get("startDate");
			String endDate = params.get("endDate");
			float count = 0;
			Calendar startCalendar = Calendar.getInstance();
			Calendar endCalendar = Calendar.getInstance();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Date startDat = df.parse(startDate);
			startCalendar.setTime(startDat);
			Date endDat = df.parse(endDate);
			endCalendar.setTime(endDat);
			
			String startDateStr=startDate.substring(0, 10);
			String endDateStr=endDate.substring(0, 10);
			String startDateMM=startDate.substring(11,13);
			String startId="";
			if(startDateMM.compareTo("12")<0) {
				startId="1";
			}else {
				startId="2";
			}
			String endId="";
			String endDateMM=endDate.substring(11,13);
			if(endDateMM.compareTo("12")<0) {
				endId="1";
			}else {
				endId="2";
			}
			
			if(startDateStr.equals(endDateStr)) {
				//同一天
				if(startId.equals(1) && endId.equals(2)) {
					count = 1;
				}else {
					count = (float)0.5;
				}
			}else if(startCalendar.getTimeInMillis() < endCalendar.getTimeInMillis()) {
				//不是同一天，获取从开始到结束时间间有几天工作日
				Map<String, String> params2 = new HashMap<>();
				params2.put("startDateStr", startDateStr);
				params2.put("endDateStr", endDateStr);
				int leaveDays=userMapper.getWorkDays(params2);
				if(leaveDays == 2) {
					if(startId.equals(1) && endId.equals(2)) {
						count = 2;
					}else if(startId.equals(2) && endId.equals(1)){
						count = 1;
					}else {
						count = (float)1.5;
					}
				}else{
					int moreDay=leaveDays-2;
					if(startId.equals("1") && endId.equals("2")) {
						count = moreDay+2;
					}else if(startId.equals("2") && endId.equals("1")){
						count = moreDay+1;
					}else {
						count = moreDay+(float)1.5;
					}
				}
			}
			return count;
		}
}
