package com.zzhb.zzoa.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.Role;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.utils.LayUiUtil;

@Service
public class RoleService {

	@Autowired
	RoleMapper roleMapper;

	public JSONObject listRoles(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Role> roles = roleMapper.getRoles(params);
		PageInfo<Role> pageInfo = new PageInfo<Role>(roles);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer updateRole(Map<String, String> params) {
		Integer updateRole = roleMapper.updateRole(params);
		List<String> userIds = roleMapper.getUserIds(params);
		
		return roleMapper.updateRole(params);
	}

}
