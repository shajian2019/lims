package com.zzhb.listener.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class CustomExecutionListener implements ExecutionListener {

	private static final long serialVersionUID = 7182968777123637771L;

	@Override
	public void notify(DelegateExecution execution) {
		String eventName = execution.getEventName();
		if ("start".equals(eventName)) {
			System.out.println("========="+eventName);
		} else if ("end".equals(eventName)) {
			System.out.println("========="+eventName);
		} else {
			System.out.println("========="+eventName);
		}
	}

}
