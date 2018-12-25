package com.zzhb.zzoa.service;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.shiro.ShiroRealm;
import com.zzhb.zzoa.utils.DateUtil;
import com.zzhb.zzoa.utils.LayUiUtil;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

	@Autowired
	UserMapper userMapper;

	@Autowired
	RoleMapper roleMapper;

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
		return userMapper.delUserById(map);
	}

	@Transactional
	public Integer addUser(User user, String role, String flag) {
		Integer addUser = 0;
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("r_id", Integer.parseInt(role));
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
				userMapper.addUrole(map);
			}
		} else {
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
		return userMapper.updateUser(map);
	}

	@Transactional
	public Integer addUrole(Map<String, Integer> map) {
		return userMapper.addUrole(map);
	}

}
