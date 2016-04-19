package com.xuexibao.ops.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.DedupMark;
import com.xuexibao.ops.service.DedupCheckService;
import com.xuexibao.ops.service.DedupMarkService;


@Controller
@RequestMapping(value = "/dedup")
public class DedupCheckController extends AbstractController {
	
	@Autowired
	protected DedupCheckService dedupCheckService;
	
	@Autowired
	protected DedupMarkService dedupMarkService;
	
	@RequestMapping(value = "/generateCheckRecord")
	public @ResponseBody ResponseResult generateCheckRecord(HttpServletRequest request, ModelMap model, Integer num) {
		try {
			
			if (num == null || num <= 0) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "抽题数目异常");
			}
			HttpSession session = request.getSession();
			String operator = (String) session.getAttribute(SessionConstant.USER_NAME);
			List<DedupMark> dedupMarkList = null;
			dedupMarkList =  dedupMarkService.getBlockIds();
		    if(dedupMarkList == null || dedupMarkList.size() <= 0) {
		    	return errorJson(EnumResCode.SERVER_ERROR.value(), "无可抽检的包");
		    }
			dedupCheckService.generateCheckRecord(num, dedupMarkList, operator);
	    	return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "生成抽检异常");
		}
	}
}
