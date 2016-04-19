package com.xuexibao.ops.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.enumeration.EnumTiKuPersonStatus;
import com.xuexibao.ops.model.UserInfo;
import com.xuexibao.ops.service.UserInfoService;

public class LoginFilter extends HttpServlet implements Filter {

	private static final long serialVersionUID = 1L;
	@Autowired
    private UserInfoService userInfoService;

    @Override
    public void init(FilterConfig filterConfig) {
//        ServletContext servletContext = filterConfig.getServletContext();  
//        WebApplicationContext wac = null;  
//        wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);  
//        this.userInfoService = (UserInfoService) wac.getBean("userInfoService");  
    }

    @Override
    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) arg0;
        HttpServletResponse response = (HttpServletResponse) arg1;
        Subject currentUser = SecurityUtils.getSubject();
        Session session = currentUser.getSession();
        String userName = (String) session.getAttribute(SessionConstant.USER_NAME);// 登录人
        String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
        Integer teamId = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
        String url = request.getRequestURI();
        if(StringUtils.isEmpty(userName) || StringUtils.isEmpty(groupName)) {
            if(url.indexOf("Login") >= 0 || url.indexOf("login") >= 0
                    || url.indexOf("userSign") >= 0
                    || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".gif")
                    || url.endsWith(".css") || url.endsWith(".png")) {
                filterChain.doFilter(request, response);
                return ;
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return ;
            }
        }
        // 组员和组长必须有组
        if(GroupNameConstant.EDITOR.equals(groupName) || GroupNameConstant.SHEN_HE.equals(groupName)) {
            if(teamId == null) {
                if(url.indexOf("Login") >= 0 || url.indexOf("login") >= 0
                        || url.indexOf("userSign") >= 0
                        || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".gif")
                        || url.endsWith(".css") || url.endsWith(".png")) {
                    filterChain.doFilter(request, response);
                    return ;
                } else {
                    response.sendRedirect(request.getContextPath() + "/login.jsp");
                    return ;
                }
            }
        }
        UserInfo user = userInfoService.getUserInfoByKey(userName);
        // 用户被禁用
        if(Byte.valueOf(EnumTiKuPersonStatus.CLOSE.getId()).equals(user.getStatus())) {
            if(url.indexOf("Login") >= 0 || url.indexOf("login") >= 0
                    || url.indexOf("userSign") >= 0
                    || url.endsWith(".js") || url.endsWith(".jpg") || url.endsWith(".gif")
                    || url.endsWith(".css") || url.endsWith(".png")) {
                filterChain.doFilter(request, response);
                return ;
            } else {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return ;
            }
        }
        filterChain.doFilter(arg0, arg1);
    }
    
}
