package com.xuexibao.ops.web;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xuexibao.ops.model.UserSignUp;
import com.xuexibao.ops.service.UserSignUpService;
import com.xuexibao.ops.util.ParamUtil;

/**
 * 
 * @author 赵超群
 * 
 */
@Controller
@RequestMapping(value = "/")
public class UserSignUpController extends AbstractController {

	@Autowired
	protected UserSignUpService userSignUpService;
	
	@RequestMapping(value = "userSignUp")
	public String userSigningUpIndex(HttpServletRequest request, ModelMap model) {
		try{			
	    	return "management/userSigningUpIndex";
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "userSigningUpFunc")
	public  String userSigningUpFunc(HttpServletRequest request, ModelMap model,
			String teachername, String phonenumber, String IDnumber,
			String city, String province) {
		try {
			
			if (StringUtils.isEmpty(teachername) || StringUtils.isEmpty(phonenumber) 
					|| StringUtils.isEmpty(IDnumber) || StringUtils.isEmpty(city)) {
				return "management/userSigningUpFail";
			}
			if (!ParamUtil.isMobile(phonenumber)) {
				return "management/userSigningUpFail";
			}
			UserSignUp userSignUp = new UserSignUp(teachername, phonenumber, IDnumber, city, province, new Date());
			userSignUp = userSignUpService.insertSelective(userSignUp);
			if (userSignUp != null)
				return "management/userSigningUpSuccess";
		} catch (Exception e) {
			e.printStackTrace();
			return "management/userSigningUpFail";
		}
		
		return "";
	}
}
