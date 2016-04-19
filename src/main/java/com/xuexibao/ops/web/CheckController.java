package com.xuexibao.ops.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.service.CheckService;
import com.xuexibao.ops.util.DateUtils;


@Controller
@RequestMapping(value = "/check")
public class CheckController extends AbstractController {
	
	@Autowired
	protected CheckService checkService;
	
	@RequestMapping(value = "/generateCheckRecord")
	public @ResponseBody ResponseResult generateCheckRecord(HttpServletRequest request, ModelMap model, String startTime, String endTime, Integer n, String teamIds) {
		try {
			
			if (StringUtils.isEmpty(startTime) || StringUtils.isEmpty(endTime) || n == null || StringUtils.isEmpty(teamIds)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			HttpSession session = request.getSession();
			String operator = (String) session.getAttribute(SessionConstant.USER_NAME);
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			String[] teamIdArray = teamIds.split(",");
			for(int i = 0; i < teamIdArray.length; i++) {
				if(StringUtils.isEmpty(teamIdArray[i]) && !StringUtils.isNumeric(teamIdArray[i]))
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			checkService.generateCheckRecord(startDate, endDate, n, teamIds, operator);
	    	return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "生成抽检异常");
		}
	}
}
