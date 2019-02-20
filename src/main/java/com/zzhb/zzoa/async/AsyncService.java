package com.zzhb.zzoa.async;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.domain.Message;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.service.MessageService;

@Service
public class AsyncService {

	private static Logger logger = LoggerFactory.getLogger(AsyncService.class);

	@Autowired
	HistoryService historyService;

	@Autowired
	RepositoryService repositoryService;

	@Autowired
	UserMapper userMapper;

	@Autowired
	TaskService taskService;

	@Autowired
	MessageService messageService;

	@Async
	public void async() {
		logger.debug("==>>异步处理");
	}

	// 异步生成消息
	public void message(String bk) {
		List<Message> messages = new ArrayList<>();
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bk)
				.singleResult();
		String createrId = hpi.getStartUserId();
		String processDefinitionId = hpi.getProcessDefinitionId();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
		List<HistoricTaskInstance> list = historyService.createHistoricTaskInstanceQuery()
				.processInstanceBusinessKey(bk).finished().orderByHistoricTaskInstanceEndTime().desc().list();
		HistoricTaskInstance historicTaskInstance = list.get(0);
		String spyj = "";
		Comment comment = taskService.getTaskComments(historicTaskInstance.getId(), "comment").get(0);
		JSONObject commentJ = JSON.parseObject(comment.getFullMessage());
		Set<String> keySet = commentJ.keySet();
		for (String string : keySet) {
			if (string.endsWith("spyj")) {
				spyj = commentJ.getString(string);
				break;
			}
		}

		Message message0 = new Message();
		message0.setTitle(processDefinition.getName());
		message0.setContent(processDefinition.getName() + "->审批意见->" + spyj);
		message0.setType("1");
		message0.setStatus("0");
		message0.setRecipientId(createrId);
		messages.add(message0);

		User user = userMapper.getUserById(createrId);
		for (HistoricTaskInstance hti : list) {
			Message message = new Message();
			String title = processDefinition.getName() + "【" + user.getNickname() + "】";
			message.setTitle(title);
			String content = user.getNickname() + "->" + processDefinition.getName() + "->审批意见->" + spyj;
			message.setContent(content);
			message.setType("1");
			message.setStatus("0");
			message.setRecipientId(hti.getAssignee());
			messages.add(message);
		}
		messageService.saveMessage(messages);
	}

}
