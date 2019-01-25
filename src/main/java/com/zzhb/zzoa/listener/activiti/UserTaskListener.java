package com.zzhb.zzoa.listener.activiti;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class UserTaskListener implements TaskListener {

	private static final long serialVersionUID = -945873544868871911L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String eventName = delegateTask.getEventName();

		System.out.println("===========name======" + delegateTask.getName());
		System.out.println("=========create====sprs=====" + delegateTask.getVariable("sprs").toString());
		String sprs = delegateTask.getVariable("sprs").toString();
		if ("create".endsWith(eventName)) {
			if (sprs.indexOf(",") == -1) {
				delegateTask.setAssignee(sprs);
			} else {
				List<String> candidateUsers = Arrays.asList(sprs.split(","));
				delegateTask.addCandidateUsers(candidateUsers);
			}
		} else if ("assignment".endsWith(eventName)) {
			System.out.println("assignment========" + delegateTask.getAssignee());
		} else if ("complete".endsWith(eventName)) {
			System.out.println("complete===========");
		} else if ("delete".endsWith(eventName)) {
			System.out.println("delete=============");
		}
	}

}
