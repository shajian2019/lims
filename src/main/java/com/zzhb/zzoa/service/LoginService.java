package com.zzhb.zzoa.service;

import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zzhb.zzoa.domain.Role;
import com.zzhb.zzoa.domain.User;
import com.zzhb.zzoa.mapper.RoleMapper;
import com.zzhb.zzoa.mapper.UserMapper;
import com.zzhb.zzoa.utils.Constant;

@Service
public class LoginService {

	private static Logger logger = LoggerFactory.getLogger(LoginService.class);
	@Autowired
	RoleMapper roleMapper;

	@Autowired
	UserMapper userMapper;

	@Transactional
	public void initLoginUser(User user, Session session) {
		Role role = roleMapper.getRol(user.getU_id());
		logger.debug("=======user======" + user.getUsername() + "=======role====" + role);
		session.setAttribute(Constant.ROLE, role);
		if (role == null) {
			session.setAttribute(Constant.ROLEID, Constant.SUPERADMIN);
		} else {
			session.setAttribute(Constant.ROLEID, role.getR_id());
		}
		session.setAttribute(Constant.USER, user);
		userMapper.updateRecentlogin(user);
		logger.debug("=====更新用户登录时间==user======" + user.getUsername());
	}
}
