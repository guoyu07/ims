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
import com.xuexibao.ops.enumeration.TranOpsCheckStatus;
import com.xuexibao.ops.model.CheckDetail;
import com.xuexibao.ops.model.OrcPicture;
import com.xuexibao.ops.service.CheckDetailService;
import com.xuexibao.ops.service.OrcPictureService;




@Controller
@RequestMapping(value = "/check")
public class CheckDetailController extends AbstractController {
	
	private static final int limit = 30;
	@Autowired
	protected CheckDetailService checkDetailService;

	@Autowired
	protected OrcPictureService orcPictureService;
	@RequestMapping(value = "/auditCheckDetailById")
	public String auditCheckDetailById(HttpServletRequest request, ModelMap model, Long questionId, Long parentId, Long grandParentId) {
		CheckDetail checkDetail = checkDetailService.getCheckDetailById(questionId);
		if(checkDetail != null) {	
			Long pictureid=checkDetail.getTranOps().getOrcPictureId();
			OrcPicture orcPicture = null;
			if( pictureid != null){
			  orcPicture  = orcPictureService.getOrcPictureById(pictureid);
			}
    		model.addAttribute("checkDetail", checkDetail);
    		model.addAttribute("parentId", parentId);
    		model.addAttribute("grandParentId", grandParentId);
    		model.addAttribute("orcPicture", orcPicture);
    		HttpSession session = request.getSession();
    		String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
    		if(GroupNameConstant.ADMIN.equals(groupName)) {
    			return "check/tranOpsCheckDetail";
    		} else {
    			return "check/tranCaptainOpsCheckDetail";
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
			
			long totalNum = checkDetailService.searchCount(questionId,  teacher, cstatus , teamid , parentId , grandParentId);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<CheckDetail> tranOpsDetailList = checkDetailService.searchList(questionId,  teacher, cstatus, teamid, parentId , grandParentId,  page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("teacher", teacher);
			model.addAttribute("cstatus", cstatus);
			model.addAttribute("tranOpsDetailList", tranOpsDetailList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			model.addAttribute("parentId", parentId);
			model.addAttribute("grandParentId", grandParentId);
			return "check/tranCheckOpsDetailList";		
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

			long totalNum = checkDetailService.searchCaptainCount(questionId,  teacher, cstatus , teamid , parentId, grandParentId);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<CheckDetail> tranOpsDetailList = checkDetailService.searchCaptainList(questionId,  teacher, cstatus, teamid, parentId, grandParentId, page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("teacher", teacher);
			model.addAttribute("cstatus", cstatus);
			model.addAttribute("tranOpsDetailList", tranOpsDetailList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);	
			model.addAttribute("parentId", parentId);
			model.addAttribute("grandParentId", grandParentId);
			return "check/tranCaptainCheckOpsDetailList";			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/audit")
	public @ResponseBody ResponseResult audit(HttpServletRequest request, String questionIds, Integer status, Integer reason, String reasonStr , Long parentId, Long grandParentId) {
		try {
			if (StringUtils.isEmpty(questionIds)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "录题id参数异常");
			}
			if (status == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			if ((TranOpsCheckStatus.UNELIGIBLE.getId() == status) && reason == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			if(StringUtils.isNotEmpty(reasonStr))
				reasonStr = new String(reasonStr.getBytes("ISO-8859-1"), "UTF-8");
			String[] audioIdArray = questionIds.split(",");
			Long[] audioIds = new Long[audioIdArray.length];
			for(int i = 0; i < audioIdArray.length; i++) {
				if(StringUtils.isEmpty(audioIdArray[i]) || !StringUtils.isNumeric(audioIdArray[i]))
					return errorJson(EnumResCode.SERVER_ERROR.value(), "批量检查id参数异常");
				audioIds[i] = Long.valueOf(audioIdArray[i]);
			}
			HttpSession session = request.getSession();
			String approvor = (String) session.getAttribute(SessionConstant.USER_NAME);
			checkDetailService.auditCheckTranOps(audioIds, approvor, status, reason, reasonStr);
			for(int i = 0; i < audioIds.length; i++) {
				CheckDetail checkDetail = checkDetailService.getCheckDetailById(audioIds[0]);
				grandParentId = checkDetail.getGrandParentId();
			}
			CheckDetail checkDetail =checkDetailService.getCheckDetailBygrandParentId(grandParentId);
			if(checkDetail != null)
			{
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
