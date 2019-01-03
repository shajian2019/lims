package com.zzhb.zzoa.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.User;
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
	LoginService loginService;
	
	@Autowired
	IdentityService identityService;

	public User getUser(String username) {
		return userMapper.getUser(username);
	}

	public JSONObject getAllUsers(Integer page, Integer limit, Map<String, String> params) {
		PageHelper.startPage(page, limit);
		List<User> userList = userMapper.getAllUsers(params);
		PageInfo<User> pageInfo = new PageInfo<User>(userList);
		return LayUiUtil.pagination(pageInfo);
	}

	@Transactional
	public Integer delUserById(Map<String, Object> map) {
		// map中只有u_id
		User u = userMapper.getUserById(Integer.parseInt(String.valueOf(map.get("id"))));
		if (!u.getStatus().equals("3")) {// 已分配角色
			userMapper.delUserRole(Integer.parseInt(String.valueOf(map.get("id"))));
		}
		identityService.deleteUser(String.valueOf(map.get("id")));
		return userMapper.delUserById(map);
	}

	@Transactional
	public Integer addUser(User user, String role, String flag) {
		Integer addUser = 0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		if ("add".equals(flag)) {
			Object result = new SimpleHash("MD5", user.getPassword(), user.getUsername(), 1);
			user.setPassword(result.toString());
			if (role == null || "".equals(role)) {
				user.setStatus("3");
				addUser = userMapper.addUser(user);
			} else {
				user.setStatus("0");
				addUser = userMapper.addUser(user);
				Integer u_id = user.getU_id();
				map.put("u_id", u_id);
				map.put("r_id", Integer.parseInt(role));
				userMapper.addUrole(map);
			}
		} else {
			map.put("r_id", Integer.parseInt(role));
			map.put("u_id", user.getU_id());
			if ("3".equals(user.getStatus())) {
				user.setStatus("0");
				userMapper.addUrole(map);
			} else {
				userMapper.updateUserRole(map);
			}
			addUser = userMapper.updateUserByUser(user);
		}
		return addUser;
	}

	@Transactional
	public Integer updateUser(Map<String, Object> map) {
		Integer updateUser = 0;
		if (map.containsKey("password")) {
			String password = (String) map.get("password");
			Object result = new SimpleHash("MD5", password, map.get("username"), 1);
			map.put("password", String.valueOf(result));
			updateUser = userMapper.updateUser(map);
		} else {
			updateUser = userMapper.updateUser(map);
			Session session = SecurityUtils.getSubject().getSession();
			User user = userMapper.getUser(map.get("username").toString());
			loginService.initLoginUser(user, session);
		}
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
