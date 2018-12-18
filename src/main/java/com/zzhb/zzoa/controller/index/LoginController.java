package com.zzhb.zzoa.controller.index;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.zzhb.zzoa.ZzoaApplication;
import com.zzhb.zzoa.domain.Form;

@Controller
public class LoginController {

	private static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@GetMapping(value = { "/", "login" })
	public String loginIn(HttpServletResponse response) {
		logger.info("===get===login==");
		Subject currentUser = SecurityUtils.getSubject();
		if (currentUser.isAuthenticated()) {
			try {
				response.sendRedirect("index");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 登录页面
		return "login";
	}

	@PostMapping(value = "loginIn")
	@ResponseBody
	public JSONObject loginIn(Form form) {
		logger.info("===post===login==");
		Subject currentUser = SecurityUtils.getSubject();
		JSONObject result = new JSONObject();
		String code = "";
		String msg = "";
		Session session = currentUser.getSession();
		if (session.getAttribute("CAPTCHA") != null
				&& session.getAttribute("CAPTCHA").equals(form.getValidCode().trim().toUpperCase())) {
			if (!currentUser.isAuthenticated()) {
				UsernamePasswordToken token = new UsernamePasswordToken(form.getUserName(), form.getPassword());
				boolean rememberMe = form.getRememberMe() != null ? true : false;
				token.setRememberMe(rememberMe);
				try {
					currentUser.login(token);
					code = "0000";
				} catch (UnknownAccountException e) {
					code = "9999";
					msg = "账号不存在";
				} catch (LockedAccountException e) {
					code = "9999";
					msg = "账号已锁定,请联系管理员";
				} catch (AuthenticationException e) {
					code = "9999";
					msg = "用户名或密码错误";
				}
			} else {
				code = "0000";
				msg = "用户已登录";
			}
		} else {
			code = "9999";
			msg = "验证码错误";
		}
		result.put("code", code);
		result.put("msg", msg);
		return result;
	}

	@RequestMapping(value = "/index")
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/404")
	public String notfind() {
		return "404";
	}
}
