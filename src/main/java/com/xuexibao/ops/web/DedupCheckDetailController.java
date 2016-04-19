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

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.DedupCheckDetail;
import com.xuexibao.ops.service.DedupCheckDetailService;

@Controller
@RequestMapping(value = "/dedup")
public class DedupCheckDetailController extends AbstractController {
	
	private static final int limit = 30;
	@Autowired
	protected DedupCheckDetailService dedupCheckDetailService;

	@RequestMapping(value = "/auditCheckDetailById")
	public String auditCheckDetailById(HttpServletRequest request, ModelMap model, Long questionId, Long parentId, Long grandParentId) {
		
		
//		DedupCheckDetail checkDetail = dedupCheckDetailService.getCheckDetailById(questionId);
//		Long pictureid=checkDetail.getTranOps().getOrcPictureId();
//		OrcPicture orcPicture = null;
//		if( pictureid != null){
//		  orcPicture  = orcPictureService.getOrcPictureById(pictureid);
//		}
//    	if(checkDetail != null ) {	
//    		model.addAttribute("checkDetail", checkDetail);
//    		model.addAttribute("parentId", parentId);
//    		model.addAttribute("grandParentId", grandParentId);
//    		HttpSession session = request.getSession();
//    		String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
//    		if(GroupNameConstant.ADMIN.equals(groupName)) {
//    			return "dedup/dedupCheckDetail";
//    		} 
//    	}
    	return null;
	}
	
	
	@RequestMapping(value = "/dedupDetailViewSearch")
	public String dedupDetailViewSearch(HttpServletRequest request, ModelMap model,
			Long questionId,  String teacher, Integer cstatus, Integer complete, String startTime, String endTime, Long page , Long parentId , Long grandParentId) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(teacher))
				teacher = new String(teacher.getBytes("ISO-8859-1"), "UTF-8");
			
			HttpSession session = request.getSession();
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);			
			
			long totalNum = dedupCheckDetailService.searchCount(questionId,  teacher, cstatus , teamid , parentId , grandParentId);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<DedupCheckDetail> tranOpsDetailList = dedupCheckDetailService.searchList(questionId,  teacher, cstatus, teamid, parentId , grandParentId,  page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("teacher", teacher);
			model.addAttribute("cstatus", cstatus);
			model.addAttribute("tranOpsDetailList", tranOpsDetailList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			model.addAttribute("parentId", parentId);
			model.addAttribute("grandParentId", grandParentId);
			return "dedup/dedupCheckDetailList";		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	//抽检正确错误判断
	@RequestMapping(value = "/audit")
	public @ResponseBody ResponseResult audit(HttpServletRequest request, Long id, Integer status) {
		try {
			if (id == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "录题id参数异常");
			}
			if (status == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			
			HttpSession session = request.getSession();
			String approvor = (String) session.getAttribute(SessionConstant.USER_NAME);
			
			dedupCheckDetailService.auditCheckTranOps(id, approvor, status);
			DedupCheckDetail checkDetail = dedupCheckDetailService.getCheckDetailById(id);
			
			Long parentId=null;
			if( checkDetail != null){
				parentId = checkDetail.getParentId();
			}
			
			if(parentId != null){
				checkDetail =dedupCheckDetailService.getCheckDetailByParentId(parentId);
				if(checkDetail != null)
				{
				   Long nextTranId = checkDetail.getId();
			       return successJson(nextTranId);
				 }		
			}
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "检查失败");
	}

}
