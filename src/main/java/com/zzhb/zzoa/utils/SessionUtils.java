package com.zzhb.zzoa.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import com.alibaba.fastjson.JSON;
import com.zzhb.zzoa.domain.User;

public class SessionUtils {

	public static User getUser() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		Object obj = session.getAttribute(Constant.USER);
		User user = new User();
		if (obj instanceof User) {
			user = (User) obj;
		} else {
			user = JSON.parseObject(JSON.toJSON(obj).toString(), User.class);
		}
		return user;
	}
}
