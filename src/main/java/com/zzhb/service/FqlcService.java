package com.zzhb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.domain.Org;
import com.zzhb.domain.activiti.ProcessDefinitionExt;
import com.zzhb.domain.activiti.UserSpr;
import com.zzhb.mapper.ActivitiMapper;
import com.zzhb.mapper.OrgMapper;
import com.zzhb.mapper.UserSprMapper;
import com.zzhb.utils.LayUiUtil;

@Service
public class FqlcService {

	@Autowired
	ActivitiMapper activitiMapper;

	@Autowired
	UserSprMapper userSprMapper;
	
	@Autowired
	OrgMapper orgMapper;

	public JSONObject fqlcList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<ProcessDefinitionExt> processDefinitionExts = activitiMapper.getProcessDefinitionExtsByUid(params);
		PageInfo<ProcessDefinitionExt> pageInfo = new PageInfo<>(processDefinitionExts);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer saveSpr(UserSpr userSpr) {
		return userSprMapper.replaceSprsById(userSpr);
	}

	public List<Map<String, String>> getSprs(UserSpr userSpr) {
		List<Map<String, String>> sprs = new ArrayList<>();
		UserSpr userSprs = userSprMapper.getUserSprs(userSpr);
		if (userSprs != null) {
			List<String> asList = Arrays.asList(userSprs.getSprs().split(","));
			Map<String, String> params = new HashMap<>(); 
			for (String key : asList) {
				params.put("o_id", key.split("#")[0]);
				params.put("u_id", key.split("#")[1]);
				sprs.add(userSprMapper.getSprs(params));
			}
		}
		return sprs;
	}
	
	public List<Org> getOrgs(String u_id) {
		return orgMapper.getUserOrgByUid(u_id);
	}
}
