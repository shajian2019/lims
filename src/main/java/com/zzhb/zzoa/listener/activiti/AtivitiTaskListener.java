package com.zzhb.zzoa.listener.activiti;

import java.util.Arrays;
import java.util.List;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;

public class AtivitiTaskListener implements TaskListener {

	private static final long serialVersionUID = -945873544868871911L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String eventName = delegateTask.getEventName();
		if ("create".endsWith(eventName)) {
			System.out.println("=========create=========" + delegateTask.getVariable("spr").toString());
			String spr = delegateTask.getVariable("spr").toString();
			if(spr.indexOf(",") == -1) {
				delegateTask.setAssignee(spr);
			}else {
				List<String> candidateUsers = Arrays.asList(spr.split(","));
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
