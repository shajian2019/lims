package com.zzhb.zzoa.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.joda.time.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.config.Props;
import com.zzhb.zzoa.domain.activiti.ProcessDefinitionExt;
import com.zzhb.zzoa.mapper.ActivitiMapper;
import com.zzhb.zzoa.utils.DateUtil;
import com.zzhb.zzoa.utils.FileUtil;
import com.zzhb.zzoa.utils.LayUiUtil;
import com.zzhb.zzoa.utils.TimeUtil;
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

	@Transactional
	public Integer deploy(Map<String, String> params, MultipartFile file) throws IOException {
		String resourceName = file.getOriginalFilename();
		Deployment deploy = repositoryService.createDeployment().addInputStream(resourceName, file.getInputStream())
				.name(resourceName).deploy();
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
		String fileDir = TimeUtil.getTimeByCustom("yyyyMMddHHmmssSSS");
		String dir = props.getTempPath() + "/" + fileDir;
		if (new File(dir).mkdirs()) {
			InputStream processModel = repositoryService.getProcessModel(processDefinitionId);
			FileUtil.saveFileFromInputStream(processModel, dir, resource_name);
			InputStream processDiagram = repositoryService.getProcessDiagram(processDefinitionId);
			FileUtil.saveFileFromInputStream(processDiagram, dir, dgrm_resource_name);
			try {
				ZipUtils.toZip(dir, new FileOutputStream(dir + ".zip"), true);
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
}
