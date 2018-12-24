package com.zzhb.zzoa.service;

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

	@Transactional
	public Integer addRole(Map<String, String> params) {
		Integer addRoleMenus = 0;
		String flag = params.get("flag");
		if ("add".equals(flag)) {
			Role role = new Role();
			role.setRolecode(params.get("rolecode"));
			role.setRolename(params.get("rolename"));
			role.setStatus(params.get("status"));
			role.setRemark(params.get("remark"));
			roleMapper.addRole(role);
			Integer r_id = role.getR_id();

			Map<String, Object> params2 = new HashMap<>();
			params2.put("r_id", r_id + "");
			String paramStr = params.get("paramStr");
			params2.put("m_ids", Arrays.asList(paramStr.split("\\|")));
			addRoleMenus = roleMapper.addRoleMenus(params2);
		} else if ("edit".equals(flag)) {

		}
		return addRoleMenus;
	}

	@Transactional
	public Integer delRole(Map<String, String> params) {

		return null;
	}

}
