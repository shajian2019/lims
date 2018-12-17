package com.zzhb.zzoa.config;

import java.util.ArrayList;
import java.util.List;

import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zzhb.zzoa.listener.ShiroSessionListener;
import com.zzhb.zzoa.shiro.ShiroRealm;
import com.zzhb.zzoa.shiro.filter.FilterChainDefinitionMapBuilder;

@Configuration
public class ShiroConfig {

	@Value("${spring.redis.host}")
	private String host;
	@Value("${spring.redis.password}")
	private String password;
	@Value("${spring.redis.port}")
	private int port;
	@Value("${spring.redis.timeout}")
	private int timeout;

	@Autowired
	Props props;

	@Bean
	public RedisManager redisManager() {
		RedisManager redisManager = new RedisManager();
		redisManager.setHost(host);
		redisManager.setPassword(password);
		redisManager.setPort(port);
		redisManager.setTimeout(timeout);
		return redisManager;
	}

	@Bean
	public RedisCacheManager redisCacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		redisCacheManager.setRedisManager(redisManager());
		return redisCacheManager;
	}

	@Bean
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
		redisSessionDAO.setKeyPrefix("shiro:session");
		redisSessionDAO.setSessionIdGenerator(new JavaUuidSessionIdGenerator());
		redisSessionDAO.setRedisManager(redisManager());
		return redisSessionDAO;
	}

	@Bean
	public SimpleCookie simpleCookie() {
		SimpleCookie cookie = new SimpleCookie("zzoa");
		cookie.setPath("/");
		cookie.setHttpOnly(true);
		cookie.setMaxAge(props.getCookemaxage());
		return cookie;
	}

	@Bean
	public DefaultWebSessionManager defaultWebSessionManager() {
		DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
		defaultWebSessionManager.setGlobalSessionTimeout(props.getGlobalSessionTimeout());
		defaultWebSessionManager.setDeleteInvalidSessions(true);
		defaultWebSessionManager.setSessionIdUrlRewritingEnabled(false);
		defaultWebSessionManager.setSessionDAO(redisSessionDAO());
		defaultWebSessionManager.setSessionIdCookieEnabled(true);
		defaultWebSessionManager.setSessionIdCookie(simpleCookie());
		List<SessionListener> list = new ArrayList<SessionListener>();
		list.add(new ShiroSessionListener());
		defaultWebSessionManager.setSessionListeners(list);
		return defaultWebSessionManager;
	}

	@Bean
	public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
		defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
		return defaultAdvisorAutoProxyCreator;
	}

	@Bean
	public HashedCredentialsMatcher credentialsMatcher() {
		HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
		credentialsMatcher.setHashAlgorithmName("MD5");
		credentialsMatcher.setStoredCredentialsHexEncoded(true);

		return credentialsMatcher;
	}

	@Bean
	public ShiroRealm shiroRealm() {
		ShiroRealm shiroRealm = new ShiroRealm();
		shiroRealm.setCacheManager(redisCacheManager());
		shiroRealm.setCredentialsMatcher(credentialsMatcher());
		return shiroRealm;
	}

	@Bean
	public DefaultWebSecurityManager defaultWebSecurityManager() {
		DefaultWebSecurityManager defaultWebSecurityManager = new DefaultWebSecurityManager();
		defaultWebSecurityManager.setCacheManager(redisCacheManager());
		defaultWebSecurityManager.setSessionManager(defaultWebSessionManager());
		defaultWebSecurityManager.setRealm(shiroRealm());
		return defaultWebSecurityManager;
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor() {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(defaultWebSecurityManager());
		return authorizationAttributeSourceAdvisor;
	}

	@Bean
	public FilterChainDefinitionMapBuilder filterChainDefinitionMapBuilder() {
		FilterChainDefinitionMapBuilder filterChainDefinitionMapBuilder = new FilterChainDefinitionMapBuilder();
		return filterChainDefinitionMapBuilder;
	}

	@Bean(name = "shiroFilter")
	public ShiroFilterFactoryBean shiroFilterFactoryBean() {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager());
		shiroFilterFactoryBean.setLoginUrl(props.getLoginUrl());
		shiroFilterFactoryBean
				.setFilterChainDefinitionMap(filterChainDefinitionMapBuilder().buildFilterChainDefinitionMap());
		return shiroFilterFactoryBean;
	}
}
