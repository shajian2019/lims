package com.zzhb.zzoa.service;

import java.util.Arrays;
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
import com.zzhb.zzoa.mapper.JobUserMapper;
import com.zzhb.zzoa.mapper.OrgMapper;
import com.zzhb.zzoa.mapper.OrgUserMapper;
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

	@Autowired
	OrgUserMapper orgUserMapper;

	@Autowired
	JobUserMapper jobUserMapper;

	public User getUser(String username) {
		return userMapper.getUser(username);
	}

	public JSONObject getAllUsers(Integer page, Integer limit, Map<String, Object> params) {
		Object object = params.get("o_ids");
		if (object != null && !"".equals(object)) {
			params.put("o_ids", Arrays.asList(object.toString().split(",")));
		}
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
	public Integer addUser(User user, Map<String, String> map) {
		Map<String, Object> params = new HashMap<>();
		Integer addUser = 0;
		if (user.getU_id() == null) {
			Object result = new SimpleHash("MD5", user.getPassword(), user.getUsername(), 1);
			user.setPassword(result.toString());
			user.setStatus("0");
			Integer u_id = userMapper.addUser(user);
			params.put("u_id", u_id);
			String o_ids = map.get("o_ids");
			if (o_ids.indexOf(",") != -1) {
				params.put("o_ids", Arrays.asList(o_ids.split(",")));
				orgUserMapper.addUserOrgs(params);
			}
			String j_ids = map.get("j_ids");
			if (j_ids.indexOf(",") != -1) {
				params.put("j_ids", Arrays.asList(j_ids.split(",")));
				jobUserMapper.addUserJobs(params);
			}
		} else {
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
