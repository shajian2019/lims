package com.zzhb.zzoa.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.Org;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.ActivitiMapper;
import com.zzhb.zzoa.mapper.OrgMapper;
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

	@Autowired
	OrgMapper orgMapper;

	public JSONObject zzjgList() {
		List<Org> list = orgMapper.getOrgs(null);
		JSONObject result = new JSONObject();
		JSONArray data = new JSONArray();
		JSONArray children = new JSONArray();
		for (Org org : list) {
			JSONObject groupJ = new JSONObject();
			groupJ.put("id", org.getId());
			groupJ.put("title", org.getName());
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
		PageHelper.startPage(page, limit);
		List<Map<String, String>> list = orgMapper.getUsers(params);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<>(list);
		return LayUiUtil.pagination(pageInfo);
	}

	public JSONObject zzjgAddUserList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<User> addUsers = activitiMapper.getAddUsers(params);
		PageInfo<User> pageInfo = new PageInfo<User>(addUsers);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer zzjgUserDel(String u_id) {
		return orgMapper.delUserOrgByUid(u_id);
	}

	@Transactional
	public Integer zzjgAdd(Org org) {
		Integer addOrg = orgMapper.addOrg(org);
		return addOrg;
	}

	@Transactional
	public Integer zzjgEdit(Org org) {
		Integer updateOrg = orgMapper.updateOrg(org);
		return updateOrg;
	}

	@Transactional
	public Integer zzjgDel(String o_id) {
		orgMapper.delUserOrgByOid(o_id);
		return orgMapper.delOrg(o_id);
	}

	@Transactional
	public Integer zzjgUserAdd(String o_id, String u_ids) {
		List<String> asList = Arrays.asList(u_ids.split("\\|"));
		Map<String, Object> params = new HashMap<>();
		params.put("u_ids", asList);
		params.put("o_id", o_id);
		return orgMapper.addUserOrg(params);
	}

}
