package com.zzhb.zzoa.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.activiti.ProcessDefinitionExt;
import com.zzhb.zzoa.domain.activiti.UserSpr;
import com.zzhb.zzoa.mapper.ActivitiMapper;
import com.zzhb.zzoa.mapper.UserSprMapper;
import com.zzhb.zzoa.utils.LayUiUtil;

@Service
public class FqlcService {
	
	@Autowired
	ActivitiMapper activitiMapper;
	
	@Autowired
	UserSprMapper userSprMapper;

	public JSONObject fqlcList(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<ProcessDefinitionExt> processDefinitionExts = activitiMapper.getProcessDefinitionExtsByUid(params);
		PageInfo<ProcessDefinitionExt> pageInfo = new PageInfo<>(processDefinitionExts);
		return LayUiUtil.pagination(pageInfo);
	}
	
	
	@Transactional
	public Integer saveSpr(Map<String,String> params) {
		return userSprMapper.replaceSprsById(params);
	}
	
	@Transactional
	public List<Map<String, String>> getSprs(Map<String,String> params) {
		List<Map<String, String>> sprs = new ArrayList<>();
		UserSpr userSprs = userSprMapper.getUserSprs(params);
		if(userSprs != null) {
			List<String> asList = Arrays.asList(userSprs.getSprs().split(","));
			sprs = userSprMapper.getSprs(asList);
		}
		return sprs;
	}
}

