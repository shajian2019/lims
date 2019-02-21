package com.zzhb.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.domain.User;
import com.zzhb.mapper.OrgMapper;
import com.zzhb.mapper.OrgUserMapper;
import com.zzhb.mapper.RoleMapper;
import com.zzhb.mapper.UserMapper;
import com.zzhb.mapper.UserOrgJobMapper;

import com.zzhb.mapper.UserSprMapper;
import com.zzhb.utils.Constant;
import com.zzhb.utils.LayUiUtil;

@Service
public class UserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	RoleMapper roleMapper;

	@Autowired
	OrgMapper orgMapper;

	@Autowired
	LoginService loginService;

	@Autowired
	IdentityService identityService;

	@Autowired
	OrgUserMapper orgUserMapper;

	@Autowired
	UserOrgJobMapper userOrgJobMapper;

	@Autowired
	UserSprMapper userSprMapper;

	public User getUser(String username) {
		return userMapper.getUser(username);
	}

	@Transactional
	public Integer updateUser(User user) {
		Integer updateUser = userMapper.updateUser(user);
		user = userMapper.getUser(user.getUsername());
		SecurityUtils.getSubject().getSession().setAttribute(Constant.USER, user);
		return updateUser;
	}

	public JSONObject getAllUsers(Integer page, Integer limit, Map<String, Object> params) {
		Object object = params.get("o_ids");
		if (object != null && !"".equals(object)) {
			params.put("oIds", Arrays.asList(object.toString().split(",")));
		}
		PageHelper.startPage(page, limit);
		List<Map<String, String>> userList = userMapper.getAllUsers(params);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(userList);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer delUserById(String u_id) {
		// 删除用户部门职位中间表
		userOrgJobMapper.delByUId(u_id);
		// 删除流程授权中间表
		userMapper.delUserProcdefByUid("%#" + u_id);
		// 删除审批人缓存表
		Map<String, String> params = new HashMap<>();
		params.put("uid", u_id);
		userSprMapper.delSprs(params);
		// 删除用户表
		return userMapper.delUser(u_id);
	}

	@Transactional
	public Integer changeUserStatus(User user) {
		return userMapper.updateUser(user);
	}

	@Transactional
	public Integer changeOrAdd(User user, Map<String, String> map) {
		Integer addUser = 0;
		if (user.getU_id() == null) {
			Object result = new SimpleHash("MD5", user.getPassword(), user.getUsername(), 1);
			user.setPassword(result.toString());
			user.setStatus("0");
			addUser = userMapper.addUser(user);
		} else {
			userOrgJobMapper.delByUId(user.getU_id() + "");
			addUser = userMapper.updateUser(user);
		}

		String org_jobs = map.get("org_jobs");
		List<String> asList = Arrays.asList(org_jobs.split(","));
		if (asList.size() > 0) {
			List<Map<String, String>> list = new ArrayList<>();
			for (String string : asList) {
				List<String> asList2 = Arrays.asList(string.split("="));
				String dw = asList2.get(0).equals("#") ? null : asList2.get(0);
				String zw = asList2.get(1).equals("#") ? null : asList2.get(1);
				if (!(dw == null && zw == null)) {
					Map<String, String> param = new HashMap<>();
					param.put("u_id", user.getU_id() + "");
					param.put("o_id", dw);
					param.put("j_id", zw);
					list.add(param);
				}
			}
			if (list.size() > 0) {
				userOrgJobMapper.addUserOrgJob(list);
			}
		}
		return addUser;
	}

	@Transactional
	public Integer addUrole(Map<String, Integer> map) {
		return userMapper.addUrole(map);
	}

	@Transactional
	public Integer resetPass(Map<String, String> map) {
		Object result = new SimpleHash("MD5", "123456", map.get("username"), 1);
		map.put("password", String.valueOf(result));
		return userMapper.resetPass(map);
	}

	public Integer countUserByUserName(Map<String, String> params) {
		return userMapper.countUserByUserName(params);
	}

	public Integer checkPass(User user) {
		User oldUser = userMapper.getUser(user.getUsername());
		Integer result = 0;
		// 对输入的旧密码加密
		Object oldPassword = new SimpleHash("MD5", user.getPassword(), user.getUsername(), 1);
		String oldpassword = String.valueOf(oldPassword);
		if (!oldpassword.equals(oldUser.getPassword())) {
			result = 1;
		}
		return result;
	}

	@Transactional
	public Integer updatePass(User user) {
		Object password = new SimpleHash("MD5", user.getPassword(), user.getUsername(), 1);
		user.setPassword(password.toString());
		return userMapper.updateUser(user);
	}
}
