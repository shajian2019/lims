package com.zzhb.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zzhb.utils.Constant;

@Component
public class MvcInterceptor implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(MvcInterceptor.class);

	@Value(value = "${server.context-path:}")
	private String contextpath;

	@Autowired
	StringRedisTemplate stringRedisTemplate;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		String url = request.getRequestURI().replace(contextpath + "/", "");
		// API访问量统计
		Long increment = stringRedisTemplate.opsForHash().increment(Constant.APICOUNT, url, 1);
		logger.debug(url + "=====access==" + increment);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
