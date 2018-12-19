package com.zzhb.zzoa.service;

import org.apache.shiro.session.Session;
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

	@Autowired
	RoleMapper roleMapper;

	@Autowired
	UserMapper userMapper;

	@Transactional
	public void initLoginUser(User user, Session session) {
		Role rol = roleMapper.getRol(user.getU_id());
		session.setAttribute(Constant.ROLE, rol);
		session.setAttribute(Constant.USER, user);
		userMapper.updateRecentlogin(user);
	}
}
