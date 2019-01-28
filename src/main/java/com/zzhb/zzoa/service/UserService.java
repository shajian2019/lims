package com.zzhb.zzoa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.OrgMapper;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.utils.LayUiUtil;

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

	public User getUser(String username) {
		return userMapper.getUser(username);
	}

	public JSONObject getAllUsers(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<Map<String, String>> userList = userMapper.getAllUsers(params);
		PageInfo<Map<String, String>> pageInfo = new PageInfo<Map<String, String>>(userList);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer delUserById(String u_id) {
		orgMapper.delUserOrgByUid(u_id);
		return userMapper.delUser(u_id);
	}

	@Transactional
	public Integer addUser(User user, String role, String flag) {
		Integer addUser = 0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		if ("add".equals(flag)) {
			Object result = new SimpleHash("MD5", user.getPassword(), user.getUsername(), 1);
			user.setPassword(result.toString());
			user.setStatus("0");
			if (role == null || "".equals(role)) {
				addUser = userMapper.addUser(user);
			} else {
				addUser = userMapper.addUser(user);
				Integer u_id = user.getU_id();
				map.put("u_id", u_id);
				map.put("r_id", Integer.parseInt(role));
				userMapper.addUrole(map);
			}
		} else {
			map.put("r_id", Integer.parseInt(role));
			map.put("u_id", user.getU_id());
			userMapper.addUrole(map);
			addUser = userMapper.updateUserByUser(user);
		}
		return addUser;
	}

	@Transactional
	public Integer updateUser(Map<String, String> map) {
		Integer updateUser = 0;
		if (map.containsKey("password")) {
			String password = (String) map.get("password");
			Object result = new SimpleHash("MD5", password, map.get("username"), 1);
			map.put("password", String.valueOf(result));
		}
		updateUser = userMapper.updateUser(map);
		return updateUser;
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

	public Integer getAllUname(String username) {
		return userMapper.getCountByName(username);
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
}
