package com.zzhb.zzoa.service;

import java.util.List;
import java.util.Map;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzhb.zzoa.domain.Org;
import com.zzhb.zzoa.domain.Role;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.OrgMapper;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.mapper.UserOrgJobMapper;
import com.zzhb.zzoa.utils.Constant;

@Service
public class LoginService {

	private static Logger logger = LoggerFactory.getLogger(LoginService.class);

	@Autowired
	RoleMapper roleMapper;

	@Autowired
	OrgMapper orgMapper;

	@Autowired
	UserMapper userMapper;

	@Autowired
	UserOrgJobMapper userOrgJobMapper;

	@Transactional
	public void initLoginUser(User user, Session session) {
		Role role = roleMapper.getRol(user.getR_id());
		session.setAttribute(Constant.ROLE, role);
		session.setAttribute(Constant.USER, user);
		List<Map<String, String>> userOrgJobs = userOrgJobMapper.getUserOrgJobs(user.getU_id() + "");
		session.setAttribute(Constant.USERORGJOBS, userOrgJobs);
		userMapper.updateRecentlogin(user);
		logger.debug("=====更新用户登录时间==user======" + user.getUsername());
	}
}
