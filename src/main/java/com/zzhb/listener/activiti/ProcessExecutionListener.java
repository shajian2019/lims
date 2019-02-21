package com.zzhb.listener.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zzhb.mapper.UserMapper;

@Component("processExecutionListener")
public class ProcessExecutionListener implements ExecutionListener {

	private static final long serialVersionUID = 7182968777123637771L;

	@Autowired
	UserMapper userMapper;

	@Override
	public void notify(DelegateExecution execution) {
		String eventName = execution.getEventName();
		System.out.println("====================" + userMapper);
		if ("start".equals(eventName)) {
			System.out.println("==eventName==" + eventName + "==bk==" + execution.getProcessInstanceBusinessKey());
		} else if ("end".equals(eventName)) {
			// TODO 删除流程中的产生的附件文件
			System.out.println("==eventName==" + eventName + "==bk==" + execution.getProcessInstanceBusinessKey());
		} else {
			System.out.println("====eventName=====" + eventName);
		}
	}

}
