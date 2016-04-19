package com.xuexibao.ops.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.constant.ShiroRoleConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.service.UserInfoService;
import com.xuexibao.ops.util.Md5PasswordEncoder;

/**
 * @author liujun
 *2015年7月4日
 */
@Controller
@RequestMapping(value = "/login")
public class LoginController extends AbstractController {
	private static Logger logger = LoggerFactory.getLogger("session_expired_log");
	@Autowired
	protected UserInfoService userInfoService;
	
	@RequestMapping(value = "/userLogin")
	public@ResponseBody
	ResponseResult userLogin(HttpServletRequest request, HttpServletResponse response) {
		String userKey = request.getParameter("loginId");
		String password = request.getParameter("password");
		try {
			if (StringUtils.isEmpty(userKey)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "用户名不能为空");
			}
			if (StringUtils.isEmpty(password)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "密码不能为空");
			}
			password = Md5PasswordEncoder.encode(password);
		} catch (Exception e) {
			e.printStackTrace();
		}
		UsernamePasswordToken token = new UsernamePasswordToken(userKey, password);
//		token.setRememberMe(true);
		// 获取当前的Subject
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		try {
			subject.login(token);
		} catch (UnknownAccountException uae) {
			return errorJson(EnumResCode.SERVER_ERROR.value(), "用户名或密码不正确");
		} catch (IncorrectCredentialsException ice) {
			return errorJson(EnumResCode.SERVER_ERROR.value(), "用户名或密码不正确");
		} catch (LockedAccountException lae) {
			return errorJson(EnumResCode.SERVER_ERROR.value(), "账户已锁定");
		} catch (ExcessiveAttemptsException eae) {
			return errorJson(EnumResCode.SERVER_ERROR.value(), "用户名或密码错误次数过多");
		} catch (AuthenticationException ae) {
			return errorJson(EnumResCode.SERVER_ERROR.value(), "用户名或密码不正确");
		}
		List<Map<String, Object>> result = new ArrayList<>();
		Map<String, Object> map = new HashMap<>();
		// 验证是否登录成功
		if (subject.isAuthenticated()) {
			logger.info("登录成功,userName:{}--Time:{}",token.getUsername(),com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
			if (subject.hasRole(ShiroRoleConstant.ADMIN)) {
				map.put("url", request.getContextPath() + "/index.jsp");
				result.add(map);
				return successJson(result);
			} else {
				String idNumber = (String) session.getAttribute(SessionConstant.ID_NUMBER);
				if (StringUtils.isNotEmpty(idNumber)) {
					map.put("url", request.getContextPath() + "/index.jsp");
					result.add(map);
					return successJson(result);
				} else {
					map.put("url", request.getContextPath() + "/user/getUserInfoByUserKey");
					result.add(map);
					return successJson(result);
				}
			}
		} else {
			logger.info("登录时用户没有被授权,跳转登录页面,userName:{}-Time:{}",token.getUsername(),com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
			token.clear();
			map.put("url", request.getContextPath() + "/login.jsp");
			result.add(map);
			return successJson(result);
		}
	}

	@SuppressWarnings("finally")
	@RequestMapping(value = "/userLogout")
	public String userLogout(HttpServletRequest request, HttpServletResponse response) {
		try{
			logger.info("登出{}--{}",SecurityUtils.getSubject().getPrincipal(),com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
			SecurityUtils.getSubject().logout();
			logger.info("登出成功{}",com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
		}catch(UnknownSessionException use){
			logger.error("登出失败，未知的session", use);
		}finally{
				 try {
					 response.sendRedirect(request.getContextPath() + "/login.jsp");
				 } catch (IOException e) {
					 logger.error("登出时,redirect失败",e);
				 }
		return null;
		}
	}
}
