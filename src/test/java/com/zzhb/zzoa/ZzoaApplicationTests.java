package com.zzhb.zzoa;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import com.zzhb.controller.FileController;
import com.zzhb.controller.xtgl.ZzglController;
import com.zzhb.service.OrgUserService;
import com.zzhb.utils.CustomProcessDiagramGenerator;
import com.zzhb.utils.FileUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZzoaApplicationTests {

	@Autowired
	ZzglController zzglController;

	@Autowired
	FileController fileController;

	@Autowired
	FormService formService;

	@Autowired
	OrgUserService orgUserService;

	@Test
	public void contextLoads() {

	}

	@Autowired
	ProcessEngine pes;

	@Test
	public void contextLoads1() {
		String fontName = "宋体";
		String processDefinitionId = "leave:1:432508";
		List<String> list = new ArrayList<>();
		BpmnModel model = repositoryService.getBpmnModel(processDefinitionId);
		if (model != null) {
			Collection<FlowElement> flowElements = model.getMainProcess().getFlowElements();
			for (FlowElement e : flowElements) {
				System.out.println("flowelement id:" + e.getId() + "  name:" + e.getName() + "   class:"
						+ e.getClass().toString());
				list.add(e.getId());
			}
		}

		ProcessDiagramGenerator processDiagramGenerator = new CustomProcessDiagramGenerator();

		InputStream generateDiagram = processDiagramGenerator.generateDiagram(model, "png", list, list, fontName,
				fontName, fontName, null, 1.0);

		FileUtil.saveFileFromInputStream(generateDiagram, "D:/", "2.png");
	}

	@Test
	public void contextLoads2() {
		String deploymentId = "160006";
		String resourceName = "leave_ksldsp.html";
		InputStream resourceAsStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
		FileUtil.saveFileFromInputStream(resourceAsStream, "D:/", resourceName);
	}

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	RuntimeService rs;

	@Autowired
	IdentityService is;

	@Autowired
	TaskService ts;

	@Test
	public void contextLoads3() {
		String processDefinitionKey = "leave";
		Map<String, Object> params = new HashMap<>();
		params.put("sprs", "3");
		ProcessInstance processInstance = rs.startProcessInstanceByKey(processDefinitionKey, params);
		System.out.println(processInstance.getId());
	}

	@Test // 删除运行中的流程
	public void deleteProcessInstance() {
		System.out.println(rs.createProcessInstanceQuery().count());
		rs.deleteProcessInstance("455001", "test");
		rs.deleteProcessInstance("457501", "test");
		System.out.println(rs.createProcessInstanceQuery().count());
	}

	@Test
	public void testIdentityService() {
		Group singleResult = is.createGroupQuery().groupMember("2").singleResult();
		System.out.println(singleResult.getName());
	}

	@Test
	public void testDelGroupIdentity() {
		List<Group> list = is.createGroupQuery().list();
		for (Group group : list) {
			is.deleteGroup(group.getId());
		}
	}

	@Test
	public void testDelUserIdentity() {
		List<User> list = is.createUserQuery().list();
		for (User user : list) {
			is.deleteUser(user.getId());
		}
	}

	@Test
	public void testTaskQuery() {
		// List<Task> list = ts.createTaskQuery().taskAssignee("test").list();
		// for (Task task : list) {
		// task.getAssignee();
		// }
		// list = ts.createTaskQuery().taskCandidateUser("2").list();
		// for (Task task : list) {
		// System.out.println(task.getId());
		// ts.claim(task.getId(), "1");
		// }
		List<Task> list = ts.createTaskQuery().taskAssignee("3").list();
		list = ts.createTaskQuery().taskCandidateUser("3").list();

		for (Task task : list) {
			System.out.println(task.getAssignee());
			System.out.println(task.getId());
		}
	}

	@Autowired
	HistoryService hs;

	@Test
	public void testHistoryService() {
		/*
		 * List<HistoricTaskInstance> list =
		 * hs.createHistoricTaskInstanceQuery().taskOwner("dev").unfinished() .list();
		 * for (HistoricTaskInstance h : list) {
		 * System.out.println(h.getProcessDefinitionId());
		 * System.out.println(h.getName()); System.out.println(h.getStartTime());
		 * System.out.println(h.getEndTime()); System.out.println(h.getOwner());
		 * System.out.println(h.getTaskDefinitionKey()); }
		 */

		/*
		 * List<HistoricProcessInstance> hps =
		 * hs.createHistoricProcessInstanceQuery().startedBy("2").list(); for
		 * (HistoricProcessInstance hp : hps) { System.out.println(hp.getBusinessKey());
		 * System.out.println(hp.getProcessDefinitionKey());
		 * System.out.println(hp.getStartActivityId());
		 * System.out.println(hp.getProcessDefinitionName());
		 * System.out.println(hp.getStartTime()); System.out.println(hp.getEndTime()); }
		 */
		System.out.println(hs.createHistoricProcessInstanceQuery().count());

		String processInstanceId = "455001";
		hs.deleteHistoricProcessInstance(processInstanceId);
		processInstanceId = "457501";
		hs.deleteHistoricProcessInstance(processInstanceId);

		System.out.println(hs.createHistoricProcessInstanceQuery().count());
	}

	@Test
	public void testHistoryTaskService() {
		String processInstanceBusinessKey = "201901171528162";
		List<HistoricTaskInstance> list = hs.createHistoricTaskInstanceQuery()
				.processInstanceBusinessKey(processInstanceBusinessKey).list();
		for (HistoricTaskInstance h : list) {
			System.out.println(h.getId());
			System.out.println(h.getName());
			System.out.println(h.getStartTime());
			System.out.println(h.getEndTime());
			System.out.println(h.getAssignee());
			System.out.println(h.getDurationInMillis());
			System.out.println(h.getClaimTime());
		}
	}

	@Test
	public void testSuspend() {
		String processInstanceId = "255001";
		rs.suspendProcessInstanceById(processInstanceId);
		// rs.activateProcessInstanceById(processInstanceId);
	}

	@Autowired
	RepositoryService ps;

	@Autowired
	ProcessEngine pe;

	@Test
	public void testView() {
		String processInstanceBusinessKey = "201901171528162";

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
			processDiagramGenerator = pe.getProcessEngineConfiguration().getProcessDiagramGenerator();
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

		FileUtil.saveFileFromInputStream(generateDiagram, "D:/", "leave.png");
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

	@Test // 中止与激活流程
	public void testSupend() {
		String processInstanceId = "160011";
		// rs.suspendProcessInstanceById(processInstanceId);
		rs.activateProcessInstanceById(processInstanceId);
	}

	@Test // s
	public void testDeleteTaskService() {

	}
}
