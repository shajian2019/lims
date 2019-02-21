package com.zzhb.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.zzhb.domain.User;
import com.zzhb.mapper.RoleMapper;
import com.zzhb.mapper.UserMapper;
import com.zzhb.utils.Constant;

/**
 * 
 * @author qingquaner 认证
 */
public class ShiroRealm extends AuthorizingRealm {

	private static Logger logger = Logger.getLogger(ShiroRealm.class);

	@Autowired
	UserMapper userMapper;

	@Autowired
	RoleMapper roleMapper;

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.debug("===ShiroRealm===");
		UsernamePasswordToken usertoken = (UsernamePasswordToken) token;
		String account = usertoken.getUsername();
		User user = userMapper.getUser(account);
		if (user == null) {
			throw new UnknownAccountException("用户不存在");
		}
		if (!"0".equals(user.getStatus())) {
			if ("1".equals(user.getStatus())) {
				throw new LockedAccountException("账号已锁定");
			} else if ("3".equals(user.getStatus())) {
				throw new LockedAccountException("角色已禁用");
			}
		} else {
			Integer r_id = user.getR_id();
			if (!Constant.SUPERADMIN.equals(user.getUsername()) && r_id == null) {
				throw new LockedAccountException("账号尚未分配角色");
			}
		}
		Object credentials = user.getPassword();
		Object principal = user;
		String realmName = getName();
		ByteSource credentialsSalt = ByteSource.Util.bytes(account);
		SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(principal, credentials,
				credentialsSalt, realmName);
		return authenticationInfo;
	}

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		logger.debug("===授权未重写====");
		return null;
	}

	public static void main(String[] args) {
		String hashAlgorithmName = "MD5";
		Object credentials = "2";
		Object salt = ByteSource.Util.bytes("2");
		int hashIterations = 1;
		// 盐值加密
		Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
		System.out.println(result);
	}
}