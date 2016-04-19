package com.xuexibao.ops.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.constant.ShiroRoleConstant;
import com.xuexibao.ops.model.UserInfo;
import com.xuexibao.ops.service.UserInfoService;
/**
 * @author liujun
 *2015年6月29日
 */
public class UserRealm extends AuthorizingRealm {

	private static Logger logger = LoggerFactory.getLogger("session_expired_log");
	@Autowired
	private UserInfoService userInfoService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String currentUsername = (String) super.getAvailablePrincipal(principals);

		UserInfo userInfo = userInfoService.getUserInfoByKey(currentUsername);
		// 为当前用户设置角色和权限
		SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();

		if (null != userInfo) {
			if (GroupNameConstant.ADMIN.equals(userInfo.getGroupname())) {
				simpleAuthorInfo.addRole(ShiroRoleConstant.ADMIN);
				// 添加权限
//				simpleAuthorInfo.addStringPermission("admin:manage");
			} else if (GroupNameConstant.AUTO.equals(userInfo.getGroupname())) {
				simpleAuthorInfo.addRole(ShiroRoleConstant.AUTO);
			} else if (GroupNameConstant.EDITOR.equals(userInfo.getGroupname())) {
				simpleAuthorInfo.addRole(ShiroRoleConstant.EDITOR);
			} else if (GroupNameConstant.MARK.equals(userInfo.getGroupname())) {
				simpleAuthorInfo.addRole(ShiroRoleConstant.MARK);
			} else if (GroupNameConstant.SHEN_HE.equals(userInfo.getGroupname())) {
				simpleAuthorInfo.addRole(ShiroRoleConstant.SHEN_HE);
			} else if (GroupNameConstant.SHEN_HE.equals(userInfo.getGroupname())) {
				simpleAuthorInfo.addRole(ShiroRoleConstant.SHEN_HE);
			} else if (GroupNameConstant.T_SHEN_HE.equals(userInfo.getGroupname())) {
				simpleAuthorInfo.addRole(ShiroRoleConstant.T_SHEN_HE);
			}else if (GroupNameConstant.T_EDITOR.equals(userInfo.getGroupname())) {
				simpleAuthorInfo.addRole(ShiroRoleConstant.T_EDITOR);
			}
			logger.info("授权成功{}",simpleAuthorInfo.getRoles());
			return simpleAuthorInfo;
		}
		logger.info("授权失败");
		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		UserInfo userInfo = userInfoService.getUserInfoByKey(token.getUsername());
		if (null != userInfo) {
			AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(userInfo.getUserKey(), userInfo.getPassword(), UserRealm.class.getName());
			this.setSession(SessionConstant.ID_NUMBER, userInfo.getIdNumber());
			this.setSession(SessionConstant.USER_NAME, userInfo.getUserKey());
			this.setSession(SessionConstant.GROUP_NAME, userInfo.getGroupname());
			this.setSession(SessionConstant.TEAM_ID, userInfo.getTeamId());
			logger.info("认证成功{}",authcInfo.getCredentials());
			return authcInfo;
		} else {
			logger.info("认证失败");
			return null;
		}
	}

	private void setSession(Object key, Object value) {
		Subject currentUser = SecurityUtils.getSubject();
		if (null != currentUser) {
			Session session = currentUser.getSession();
			if (null != session) {
				session.setAttribute(key, value);
			}
		}
	}
}
