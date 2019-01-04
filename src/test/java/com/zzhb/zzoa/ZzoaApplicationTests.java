package com.zzhb.zzoa;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.zzhb.zzoa.controller.xtgl.ZzglController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZzoaApplicationTests {

	@Autowired
	ZzglController zzglController;

	@Test
	public void contextLoads() {
		System.out.println(zzglController.zzjgAdd("销售部"));
		System.out.println(zzglController.zzjgAdd("行政部"));
		System.out.println(zzglController.zzjgAdd("工程部"));
	}

	@Test
	public void contextLoads2() {
		System.out.println(zzglController.zzjgList());
	
	}
	
	@Autowired
	RepositoryService repositoryService;
	
	@Test
	public void contextLoads3() {
		String deploymentId = "40001";
		repositoryService.deleteDeployment(deploymentId, true);
		
	}

}
