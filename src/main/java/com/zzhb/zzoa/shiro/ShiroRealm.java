package com.zzhb.zzoa.shiro;

import org.apache.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;


/**
 * 
 * @author qingquaner 认证
 */
public class ShiroRealm extends AuthorizingRealm {

	private static Logger logger = Logger.getLogger(ShiroRealm.class);

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		logger.debug("===ShiroRealm===");
		UsernamePasswordToken usertoken = (UsernamePasswordToken) token;
		String account = usertoken.getUsername();
		// TODO
		Object credentials = "a66abb5684c45962d887564f08346e8d";
		Object principal = "admin";
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
		Object credentials = "123456";
		Object salt = ByteSource.Util.bytes("admin");
		int hashIterations = 1;
		// 盐值加密
		Object result = new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations);
		System.out.println(result);
	}
}
