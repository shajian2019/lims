package com.zzhb.zzoa;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.identity.Group;
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
	
	@Autowired
	RuntimeService rs;
	
	@Autowired
	IdentityService is;
	
	@Test
	public void contextLoads3() {
		String deploymentId = "40001";
		repositoryService.deleteDeployment(deploymentId, true);
		
	}
	
	
	
	@Test //删除运行中的流程
	public void deleteProcessInstance() {
		System.out.println(rs.createProcessInstanceQuery().count());
		rs.deleteProcessInstance("65006", "test");
		System.out.println(rs.createProcessInstanceQuery().count());
	}

	
	@Test 
	public void testIdentityService() {
		Group singleResult = is.createGroupQuery().groupMember("2").singleResult();
		System.out.println(singleResult.getName());
	}
}
