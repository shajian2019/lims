package com.zzhb.async;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.zzhb.domain.Message;
import com.zzhb.domain.User;
import com.zzhb.mapper.MessageMapper;
import com.zzhb.mapper.UserMapper;
import com.zzhb.service.MessageService;
import com.zzhb.websocket.MessageWebSocket;

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

	@Autowired
	MessageWebSocket messageWebSocket;

	@Autowired
	MessageMapper messageMapper;

	@Async
	public void async() {
		logger.debug("==>>异步处理");
	}

	// 异步生成消息
	@Async
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

		List<String> uIds = new ArrayList<>();
		Message message0 = new Message();
		message0.setTitle(processDefinition.getName());
		message0.setContent("流程单号：" + bk + "->" + processDefinition.getName() + "->流程结束");
		message0.setType("1");
		message0.setStatus("0");
		message0.setRecipientId(createrId);
		uIds.add(createrId);
		messages.add(message0);

		User user = userMapper.getUserById(createrId);
		for (HistoricTaskInstance hti : list) {
			Message message = new Message();
			String title = processDefinition.getName() + "【" + user.getNickname() + "】";
			message.setTitle(title);
			String content = "流程单号：" + bk + "->" + user.getNickname() + "->" + processDefinition.getName() + "->流程结束";
			message.setContent(content);
			message.setType("1");
			message.setStatus("0");
			message.setRecipientId(hti.getAssignee());
			messages.add(message);
			uIds.add(hti.getAssignee());
		}
		messageService.saveMessage(messages);

		try {
			for (String u_id : uIds) {
				Integer countMessages = messageMapper.countMessages(u_id);
				messageWebSocket.sendtoUser(countMessages + "", u_id);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
