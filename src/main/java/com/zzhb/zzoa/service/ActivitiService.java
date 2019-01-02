package com.zzhb.zzoa.service;

import java.util.List;
import java.util.Map;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class ActivitiService {

	private static Logger logger = LoggerFactory.getLogger(ActivitiService.class);

	@Autowired
	RepositoryService repositoryService;

	public void bsglList(Integer page, Integer limit, Map<String, String> params) {
		List<Deployment> list = null;

		DeploymentQuery deploymentQuery = repositoryService.createDeploymentQuery();
	}
}
