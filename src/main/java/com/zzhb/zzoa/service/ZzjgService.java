package com.zzhb.zzoa.service;

import java.util.List;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;

@Service
public class ZzjgService {

	@Autowired
	IdentityService identityService;

	public JSONObject zzjgList() {
		List<Group> list = identityService.createGroupQuery().orderByGroupName().asc().list();
		JSONObject result = new JSONObject();
		JSONArray data = new JSONArray();
		JSONArray children = new JSONArray();
		for (Group group : list) {
			JSONObject groupJ = new JSONObject();
			groupJ.put("id", group.getId());
			groupJ.put("title", group.getName());
			groupJ.put("parentId", "0");
			groupJ.put("children", children);
			data.add(groupJ);
		}
		result.put("data", data);
		result.put("code", 0);
		result.put("msg", "success");
		return result;
	}

	public JSONObject zzjgUserList(Integer page, Integer limit, String groupId) {
		PageHelper.startPage(page, limit);
		List<User> list = identityService.createUserQuery().memberOfGroup(groupId).list();
		
		return null;
	}

	@Transactional
	public Integer zzjgAdd(String groupName) {
		String groupId = UUID.randomUUID().toString();
		Group group = identityService.newGroup(groupId);
		group.setName(groupName);
		group.setType(groupName);
		identityService.saveGroup(group);
		return 1;
	}

	@Transactional
	public Integer zzjgEdit(String groupId, String groupName) {
		Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
		group.setName(groupName);
		group.setType(groupName);
		identityService.saveGroup(group);
		return 1;
	}

	@Transactional
	public Integer zzjgDel(String groupId) {
		identityService.deleteGroup(groupId);
		return 1;
	}

}
