package com.zzhb.zzoa.listener.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class ProcessExecutionListener implements ExecutionListener {

	private static final long serialVersionUID = 7182968777123637771L;

	@Override
	public void notify(DelegateExecution execution) {
		String eventName = execution.getEventName();
		if ("start".equals(eventName)) {
			System.out.println("==eventName==" + eventName + "==bk==" + execution.getProcessInstanceBusinessKey());
		} else if ("end".equals(eventName)) {
			// TODO 删除流程中的产生的附件文件
			System.out.println("==eventName==" + eventName + "==bk==" + execution.getProcessInstanceBusinessKey());
		} else {
			System.out.println("=========" + eventName);
		}
	}

}
