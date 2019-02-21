package com.zzhb.listener.activiti;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class UserTaskCreateListener implements TaskListener {

	private static final long serialVersionUID = -945873544868871911L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String eventName = delegateTask.getEventName();
		System.out.println("==name======" + delegateTask.getName() + "====eventName====" + eventName + "===sprs====="
				+ delegateTask.getVariable("sprs").toString());
		String sprs = delegateTask.getVariable("sprs").toString();
		if (sprs.indexOf(",") == -1) {
			delegateTask.setAssignee(sprs);
		} else {
			List<String> candidateUsers = Arrays.asList(sprs.split(","));
			delegateTask.addCandidateUsers(candidateUsers);
		}
	}

}