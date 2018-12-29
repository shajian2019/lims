package com.zzhb.zzoa.service;

import java.util.List;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.utils.LayUiUtil;

@Service
public class ActivitiService {

	private static Logger logger = LoggerFactory.getLogger(ActivitiService.class);

	@Autowired
	RepositoryService repositoryService;

	public JSONObject listDeloy(Integer page, Integer limit) {
		PageHelper.startPage(page, limit);
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		PageInfo<ProcessDefinition> pageInfo = new PageInfo<>(list);
		return LayUiUtil.pagination(pageInfo);
	}

}
