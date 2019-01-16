package com.zzhb.zzoa.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.config.Props;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.domain.activiti.Leave;
import com.zzhb.zzoa.domain.activiti.ProcessDefinitionExt;
import com.zzhb.zzoa.domain.activiti.ProcessDefinitionType;
import com.zzhb.zzoa.mapper.ActivitiMapper;
import com.zzhb.zzoa.mapper.LeaveMapper;
import com.zzhb.zzoa.utils.FileUtil;
import com.zzhb.zzoa.utils.LayUiUtil;
import com.zzhb.zzoa.utils.SessionUtils;
import com.zzhb.zzoa.utils.ZipUtils;

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

	@Transactional
	public Integer deploy(Map<String, String> params, MultipartFile file) throws IOException {
		String resourceName = file.getOriginalFilename();
		Deployment deploy = repositoryService.createDeployment()
				.addZipInputStream(new ZipInputStream(file.getInputStream())).name(resourceName).deploy();

		ProcessDefinition pdf = repositoryService.createProcessDefinitionQuery().deploymentId(deploy.getId())
				.singleResult();
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
		Integer addProcessDefinitionExt = activitiMapper.addProcessDefinitionExt(pde);
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

	@Transactional
	public Integer lcdyDel(Map<String, String> params) {
		repositoryService.deleteDeployment(params.get("deployment_id"), true);
		return activitiMapper.delProcessDefinitionExt(params);
	}

	public String download(Map<String, String> params) {
		String fileName = "";
		String processDefinitionId = params.get("id");
		String resource_name = params.get("resource_name");
		String dgrm_resource_name = params.get("dgrm_resource_name");
		String fileDir = resource_name.split("\\.")[0];
		String dir = props.getTempPath() + "/" + fileDir;
		if (new File(dir).mkdirs()) {
			InputStream processModel = repositoryService.getProcessModel(processDefinitionId);
			FileUtil.saveFileFromInputStream(processModel, dir, resource_name);
			InputStream processDiagram = repositoryService.getProcessDiagram(processDefinitionId);
			FileUtil.saveFileFromInputStream(processDiagram, dir, dgrm_resource_name);
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

	@Autowired
	TaskService taskService;

	@Autowired
	RuntimeService runtimeService;

	@Transactional
	public JSONObject startProcessInstance(String key, Map<String, String> params) {
		JSONObject result = new JSONObject();
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery().processDefinitionKey(key)
				.latestVersion().singleResult();
		Map<String, String> vars = new HashMap<>();
		ProcessInstance pi = formService.submitStartFormData(pd.getId(), params.get("bk"), vars);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		User user = SessionUtils.getUser();
		taskService.setOwner(task.getId(), user.getU_id() + "");
		Map<String, Object> variable = new HashMap<>();
		variable.put("spr", params.get("spr"));
		taskService.complete(task.getId(), variable);
		Integer saveBusiness = saveBusiness(key, params);
		task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		result.put("code", saveBusiness);
		result.put("msg", task.getName());
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
}
