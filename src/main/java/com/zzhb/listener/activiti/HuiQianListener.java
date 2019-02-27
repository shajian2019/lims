package com.zzhb.listener.activiti;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class HuiQianListener implements ExecutionListener {

	private static final long serialVersionUID = -5187440899740917371L;

	@Override
	public void notify(DelegateExecution execution) {
		String eventName = execution.getEventName();
		System.out.println(
				"====eventName====" + eventName + "===leaders=====" + execution.getVariable("sprs").toString());
		String leaders = execution.getVariable("sprs").toString();
		List<String> leaderList = Arrays.asList(leaders.split(","));
		execution.setVariable("leaderList", leaderList);
	}

}
