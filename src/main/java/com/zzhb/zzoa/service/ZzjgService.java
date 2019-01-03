package com.zzhb.zzoa.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.identity.UserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.mapper.ActivitiMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.utils.LayUiUtil;

@Service
public class ZzjgService {

	@Autowired
	IdentityService identityService;

	@Autowired
	ActivitiMapper activitiMapper;

	@Autowired
	UserMapper userMapper;

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
		JSONObject status = new JSONObject();
		status.put("code", 200);
		status.put("message", "操作成功");
		result.put("status", status);
		return result;
	}

	public JSONObject zzjgUserList(Integer page, Integer limit, Map<String, String> params) {
		String groupId = params.get("groupId");
		String firstname = params.get("firstname");
		String lastname = params.get("lastname");
		PageHelper.startPage(page, limit);
		List<User> list = null;
		UserQuery createUserQuery = identityService.createUserQuery();
		if (groupId != null && !"".equals(groupId)) {
			createUserQuery = createUserQuery.memberOfGroup(groupId);
		}
		if (firstname != null && !"".equals(firstname)) {
			createUserQuery = createUserQuery.userFirstNameLike("%" + firstname + "%");
		}
		if (lastname != null && !"".equals(lastname)) {
			createUserQuery = createUserQuery.userLastNameLike("%" + lastname + "%");
		}
		list = createUserQuery.list();
		PageInfo<User> pageInfo = new PageInfo<>(list);
		JSONObject result = new JSONObject();
		result.put("code", 0);
		result.put("msg", "");
		result.put("count", pageInfo.getTotal());
		List<User> list2 = pageInfo.getList();
		JSONArray array = new JSONArray();
		for (User user : list2) {
			JSONObject json = new JSONObject();
			json.put("id", user.getId());
			Group group = identityService.createGroupQuery().groupMember(user.getId()).singleResult();
			json.put("groupname", group.getName());
			json.put("username", user.getFirstName());
			json.put("email", user.getEmail());
			json.put("nickname", user.getLastName());
			array.add(json);
		}
		result.put("data", array);
		return result;
	}

	public JSONObject zzjgAddUserList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<com.zzhb.zzoa.domain.User> addUsers = activitiMapper.getAddUsers(params);
		PageInfo<com.zzhb.zzoa.domain.User> pageInfo = new PageInfo<>(addUsers);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer zzjgUserDel(String userId) {
		identityService.deleteUser(userId);
		return 1;
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
		List<User> list = identityService.createUserQuery().memberOfGroup(groupId).list();
		identityService.deleteGroup(groupId);
		for (User user : list) {
			identityService.deleteUser(user.getId());
		}
		return 1;
	}

	@Transactional
	public Integer zzjgUserAdd(String groupId, String u_ids) {
		List<String> asList = Arrays.asList(u_ids.split("\\|"));
		Map<String, Object> params = new HashMap<>();
		params.put("u_ids", asList);
		List<com.zzhb.zzoa.domain.User> users = userMapper.getUsers(params);
		for (com.zzhb.zzoa.domain.User user : users) {
			User userA = identityService.newUser(user.getU_id() + "");
			userA.setFirstName(user.getUsername());
			userA.setLastName(user.getNickname());
			userA.setEmail(user.getEmail());
			identityService.saveUser(userA);
			identityService.createMembership(user.getU_id() + "", groupId);
		}
		return 1;
	}

}
