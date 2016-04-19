package com.xuexibao.ops.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.PictureCheckDetail;
import com.xuexibao.ops.service.PictureCheckDetailService;

@Controller
@RequestMapping(value = "/piccheck")
public class PictureCheckDetailController extends AbstractController {
	
	private static final int limit = 30;
	@Autowired
	protected PictureCheckDetailService picturePictureCheckDetailService;

	@RequestMapping(value = "/auditCheckDetailById")
	public String auditCheckDetailById(HttpServletRequest request, ModelMap model, Long questionId, Long parentId, Long grandParentId) {
		PictureCheckDetail checkDetail = picturePictureCheckDetailService.getCheckDetailById(questionId);
	
    	if(checkDetail != null ) {	
    		model.addAttribute("checkDetail", checkDetail);
    		model.addAttribute("parentId", parentId);
    		model.addAttribute("grandParentId", grandParentId);
    	
    		HttpSession session = request.getSession();
    		String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
    		if(GroupNameConstant.ADMIN.equals(groupName)) {
    			return "picture/picOpsCheckDetail";
    		} else {
    			return "picture/picCaptainOpsCheckDetail";
    		}
    	}
    	return null;
	}
	
	
	@RequestMapping(value = "/tranOpsDetailViewSearch")
	public String tranOpsDetailViewSearch(HttpServletRequest request, ModelMap model,
			Long questionId,  String teacher, Integer cstatus, Integer complete, String startTime, String endTime, Long page , Long parentId , Long grandParentId) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(teacher))
				teacher = new String(teacher.getBytes("ISO-8859-1"), "UTF-8");
			
			HttpSession session = request.getSession();
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);			
			
			long totalNum = picturePictureCheckDetailService.searchCount(questionId,  teacher, cstatus , teamid , parentId , grandParentId);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<PictureCheckDetail> tranOpsDetailList = picturePictureCheckDetailService.searchList(questionId,  teacher, cstatus, teamid, parentId , grandParentId,  page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("teacher", teacher);
			model.addAttribute("cstatus", cstatus);
			model.addAttribute("tranOpsDetailList", tranOpsDetailList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			model.addAttribute("parentId", parentId);
			model.addAttribute("grandParentId", grandParentId);
			return "picture/picCheckOpsDetailList";		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/tranCaptainOpsDetailViewSearch")
	public String tranCaptainOpsDetailViewSearch(HttpServletRequest request, ModelMap model,
			Long questionId,  String teacher, Integer cstatus, Integer complete, String startTime, String endTime, Long page , Long parentId , Long grandParentId) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(teacher))
				teacher = new String(teacher.getBytes("ISO-8859-1"), "UTF-8");

			HttpSession session = request.getSession();
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);			

			long totalNum = picturePictureCheckDetailService.searchCaptainCount(questionId,  teacher, cstatus , teamid , parentId, grandParentId);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<PictureCheckDetail> tranOpsDetailList = picturePictureCheckDetailService.searchCaptainList(questionId,  teacher, cstatus, teamid, parentId, grandParentId, page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("teacher", teacher);
			model.addAttribute("cstatus", cstatus);
			model.addAttribute("tranOpsDetailList", tranOpsDetailList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);	
			model.addAttribute("parentId", parentId);
			model.addAttribute("grandParentId", grandParentId);
			return "picture/picCaptainCheckOpsDetailList";			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/audit")
	public @ResponseBody ResponseResult audit(HttpServletRequest request, String questionIds, Integer status, Long parentId, Long grandParentId) {
		try {
			if (StringUtils.isEmpty(questionIds)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "录题id参数异常");
			}
			if (status == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			String[] audioIdArray = questionIds.split(",");
			Long[] audioIds = new Long[audioIdArray.length];
			for(int i = 0; i < audioIdArray.length; i++) {
				if(StringUtils.isEmpty(audioIdArray[i]) || !StringUtils.isNumeric(audioIdArray[i]))
					return errorJson(EnumResCode.SERVER_ERROR.value(), "批量检查id参数异常");
				audioIds[i] = Long.valueOf(audioIdArray[i]);
			}
			HttpSession session = request.getSession();
			String approvor = (String) session.getAttribute(SessionConstant.USER_NAME);
			picturePictureCheckDetailService.auditCheckTranOps(audioIds, approvor, status);
			for(int i = 0; i < audioIds.length; i++) {
				PictureCheckDetail checkDetail = picturePictureCheckDetailService.getCheckDetailById(audioIds[0]);
				grandParentId = checkDetail.getGrandParentId();
			}
			PictureCheckDetail checkDetail =picturePictureCheckDetailService.getCheckDetailBygrandParentId(grandParentId);
			if(checkDetail != null){
			   long nextTranId;
		       nextTranId=checkDetail.getId();
		       return successJson(nextTranId);
			}		
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "检查失败");
	}

}
