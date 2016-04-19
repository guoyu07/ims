package com.xuexibao.ops.web;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.enumeration.EnumSubjectType;
import com.xuexibao.ops.enumeration.TranOpsAuditStatus;
import com.xuexibao.ops.enumeration.TranOpsCompleteStatus;
import com.xuexibao.ops.model.OrcPicture;
import com.xuexibao.ops.model.TranOps;
import com.xuexibao.ops.service.OrcPictureService;
import com.xuexibao.ops.service.TikuTeamService;
import com.xuexibao.ops.service.TranOpsService;
import com.xuexibao.ops.util.DateUtils;

/**
 * 
 * @author 赵超群
 * 
 */
@Controller
@RequestMapping(value = "/tranops")
public class TranOpsController extends AbstractController {
	
	private static final int limit = 30;
	@Autowired
	protected TranOpsService tranOpsService;
	@Autowired
	protected TikuTeamService tikuTeamService;
	
	@Autowired
	protected OrcPictureService orcPictureService;
	
	@RequestMapping(value = "/editTranOpsById")
	public String editTranOpsById(ModelMap model, Long questionId) {
		TranOps tranOps = tranOpsService.getTranOpsById(questionId);
    	if(tranOps != null) {
    		model.addAttribute("SelectOption", JSONArray.parse(tranOps.getSelectOption()));
    		model.addAttribute("tranOps", tranOps);
			if(TranOpsAuditStatus.AUDIT_THROUGH.getId().equals(tranOps.getAuditStatus())) {
				return "tranops/tranOpsEditViewDetail";
			} else {
				return "tranops/tranOpsEditDetail";
			}
    	}
    	return null;
	}
	
	@RequestMapping(value = "/adminEditTranOpsById")
	public String adminEditTranOpsById(HttpServletRequest request, ModelMap model, Long questionId) {
		HttpSession session = request.getSession();
		String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
		if(!GroupNameConstant.ADMIN.equals(groupName))
			return null;
		TranOps tranOps = tranOpsService.getTranOpsById(questionId);
		if(tranOps != null) {
			model.addAttribute("SelectOption", JSONArray.parse(tranOps.getSelectOption()));
			model.addAttribute("tranOps", tranOps);
			return "tranops/tranOpsAdminEditDetail";
		}
		return null;
	}
	
	@RequestMapping(value = "/auditTranOpsById")
	public String auditTranOpsById(HttpServletRequest request, ModelMap model, Long questionId) {
		TranOps tranOps = tranOpsService.getTranOpsById(questionId);
    	if(tranOps != null) {
    		model.addAttribute("SelectOption", JSONArray.parse(tranOps.getSelectOption()));
    		model.addAttribute("tranOps", tranOps);
    		HttpSession session = request.getSession();
    		String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
    		Integer type = tranOps.getOrcType();
    		if(type == 0){ //person    		
	    		if(GroupNameConstant.ADMIN.equals(groupName)) {
	    			return "tranops/tranOpsAdminAuditDetail";
	    		} else {
	    			return "tranops/tranOpsAuditDetail";
	    		}
    		}
    		else if (type == 1){ 			
    			OrcPicture orcPicture  = orcPictureService.getOrcPictureById(tranOps.getOrcPictureId());
    			if(orcPicture != null) {
	    			model.addAttribute("orcPicture", orcPicture);
	    			if(GroupNameConstant.ADMIN.equals(groupName)) {
		    			return "tranops/tranOpsAdminAuditPictureDetail";
		    		} else {
		    			return "tranops/tranOpsAuditPictureDetail";
		    		}
    			}
    			
    		}
    	}
    	return null;
	}
	
	@RequestMapping(value = "/viewTranOpsById")
	public String viewTranOpsById(ModelMap model, Long questionId) {
		TranOps tranOps = tranOpsService.getTranOpsById(questionId);
    	if(tranOps != null) {
    		model.addAttribute("SelectOption", JSONArray.parse(tranOps.getSelectOption()));
    		model.addAttribute("tranOps", tranOps);
			return "tranops/tranOpsViewDetail";
    	}
    	return null;
	}
	
	@RequestMapping(value = "/viewTranOpsByPictureId")
	public String viewTranOpsByPictureId(HttpServletRequest request, ModelMap model, Long pictureId) {
		TranOps tranOps = tranOpsService.getTranOpsByPictureId(pictureId);
    	if(tranOps != null) {
    		model.addAttribute("SelectOption", JSONArray.parse(tranOps.getSelectOption()));
    		model.addAttribute("tranOps", tranOps);
    		OrcPicture orcPicture  = orcPictureService.getOrcPictureById(tranOps.getOrcPictureId());
			if(orcPicture != null) {
    			model.addAttribute("orcPicture", orcPicture);
//    		if(GroupNameConstant.ADMIN.equals(groupName)) {
//    			return "tranops/tranOpsAdminAuditPictureDetail";
//    		} else {
    			return "tranops/tranOpsViewAuditPictureDetail";
//    		}
    			}
    	}
    	return null;
	}
	
	@RequestMapping(value = "/tranOpsEditSearch")
	public String tranOpsEditSearch(HttpServletRequest request, ModelMap model,
			Long questionId, Integer subject,Integer realType,Integer realLearnPhase, String status, Integer complete, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			HttpSession session = request.getSession();
			String teacher = (String) session.getAttribute(SessionConstant.USER_NAME);
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			long totalNum = tranOpsService.searchCount(questionId, subject, realType, realLearnPhase, teacher, status, complete, startDate, endDate);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<TranOps> tranOpsList = tranOpsService.searchList(questionId, subject, realType, realLearnPhase, teacher, status, complete, startDate, endDate, page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("subject", subject);
			model.addAttribute("realType", realType);
			model.addAttribute("realLearnPhase", realLearnPhase);
			model.addAttribute("teacher", teacher);
			model.addAttribute("status", status);
			model.addAttribute("complete", complete);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("tranOpsList", tranOpsList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "tranops/tranOpsEditList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/tranOpsAdminEditSearch")
	public String tranOpsAdminEditSearch(HttpServletRequest request, ModelMap model,
			Long questionId, Integer subject,Integer realType,Integer realLearnPhase, String status, Integer complete, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			HttpSession session = request.getSession();
			String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
			if(!GroupNameConstant.ADMIN.equals(groupName))
				return null;
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			long totalNum = tranOpsService.searchCount(questionId, subject, realType, realLearnPhase, status, complete, startDate, endDate);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<TranOps> tranOpsList = tranOpsService.searchList(questionId, subject,  realType, realLearnPhase, status, complete, startDate, endDate, page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("subject", subject);
			model.addAttribute("realType", realType);
			model.addAttribute("realLearnPhase", realLearnPhase);
//			model.addAttribute("teacher", teacher);
			model.addAttribute("status", status);
			model.addAttribute("complete", complete);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("tranOpsList", tranOpsList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "tranops/tranOpsAdminEditList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/tranOpsAuditSearch")
	public String tranOpsAuditSearch(HttpServletRequest request, ModelMap model,
			Long questionId, Integer subject,Integer realType,Integer realLearnPhase, String teacher, String status, Integer complete, String startTime, String endTime, String auditStartTime, String auditEndTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			HttpSession session = request.getSession();
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			if(StringUtils.isNotEmpty(teacher))
				teacher = new String(teacher.getBytes("ISO-8859-1"), "UTF-8");
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			Date auditStartDate = DateUtils.parseDate(auditStartTime, "yyyy-MM-dd HH:mm");
			Date auditEndDate = DateUtils.parseDate(auditEndTime, "yyyy-MM-dd HH:mm");
			long totalNum = tranOpsService.searchCountByTeam(teamid, questionId, subject, realType, realLearnPhase, teacher, status, complete, startDate, endDate, auditStartDate, auditEndDate);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<TranOps> tranOpsList = tranOpsService.searchListByTeam(teamid, questionId, subject,  realType, realLearnPhase,teacher, status, complete, startDate, endDate, auditStartDate, auditEndDate, page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("subject", subject);

			model.addAttribute("realType", realType);
			model.addAttribute("realLearnPhase", realLearnPhase);
			model.addAttribute("teacher", teacher);
			model.addAttribute("status", status);
			model.addAttribute("complete", complete);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("auditStartTime", auditStartTime);
			model.addAttribute("auditEndTime", auditEndTime);
			model.addAttribute("tranOpsList", tranOpsList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "tranops/tranOpsAuditList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/tranOpsAdminAuditSearch")
	public String tranOpsAdminAuditSearch(HttpServletRequest request, ModelMap model,
			Long questionId, Integer subject,Integer realType,Integer realLearnPhase, String teacher, String status, Integer complete, String startTime, String endTime, String approvor, String auditStartTime, String auditEndTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(teacher))
				teacher = new String(teacher.getBytes("ISO-8859-1"), "UTF-8");
			if(StringUtils.isNotEmpty(approvor))
				approvor = new String(approvor.getBytes("ISO-8859-1"), "UTF-8");
			Integer teamId = null;
			List<TranOps> tranOpsList = Collections.emptyList();
			long totalNum = 0;
			long totalPageNum = 0;
			if(StringUtils.isNotEmpty(approvor) && (teamId = tikuTeamService.getTeamIdByCaptain(approvor)) == null) {
				// 根据审核人差不多小组，返回空列表
			} else {
				Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
				Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
				Date auditStartDate = DateUtils.parseDate(auditStartTime, "yyyy-MM-dd HH:mm");
				Date auditEndDate = DateUtils.parseDate(auditEndTime, "yyyy-MM-dd HH:mm");
				totalNum = tranOpsService.searchCountByTeam(teamId, questionId, subject, realType, realLearnPhase, teacher, status, complete, startDate, endDate, auditStartDate, auditEndDate);
				totalPageNum = totalNum / limit;
				if(totalNum > totalPageNum * limit)
					totalPageNum++;
				if(page >= totalPageNum && totalPageNum != 0)
					page = totalPageNum - 1;
				tranOpsList = tranOpsService.searchListByTeamWithTeamName(teamId, questionId, subject, realType, realLearnPhase, teacher, status, complete, startDate, endDate, auditStartDate, auditEndDate, page * limit, limit);
			}
			model.addAttribute("questionId", questionId);
			model.addAttribute("subject", subject);
			model.addAttribute("realType", realType);
			model.addAttribute("realLearnPhase", realLearnPhase);
			model.addAttribute("teacher", teacher);
			model.addAttribute("status", status);
			model.addAttribute("complete", complete);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("approvor", approvor);
			model.addAttribute("auditStartTime", auditStartTime);
			model.addAttribute("auditEndTime", auditEndTime);
			model.addAttribute("tranOpsList", tranOpsList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "tranops/tranOpsAdminAuditList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/tranOpsViewSearch")
	public String tranOpsViewSearch(HttpServletRequest request, ModelMap model,
			Long questionId, Integer subject, Integer realType,Integer realLearnPhase, String teacher, String status, Integer complete, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(teacher))
				teacher = new String(teacher.getBytes("ISO-8859-1"), "UTF-8");
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			long totalNum = tranOpsService.searchCount(questionId, subject,  realType, realLearnPhase,teacher, status, complete, startDate, endDate);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<TranOps> tranOpsList = tranOpsService.searchList(questionId, subject, realType, realLearnPhase, teacher, status, complete, startDate, endDate, page * limit, limit);
			model.addAttribute("questionId", questionId);
			model.addAttribute("subject", subject);
			model.addAttribute("realType", realType);
			model.addAttribute("realLearnPhase", realLearnPhase);
			model.addAttribute("teacher", teacher);
			model.addAttribute("status", status);
			model.addAttribute("complete", complete);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("tranOpsList", tranOpsList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "tranops/tranOpsViewList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateTranOps")

	public @ResponseBody ResponseResult updateTranOps(HttpServletRequest request, Long questionId, Integer complete, Integer subject, Integer target, Integer realType, Integer realLearnPhase, 
			String content, String selectContent , String[] selectOptionArray ,String selectOption, String latex, String answer, String answerLatex, String solution) {

		try {
			if (questionId == null || complete == null || subject == null || realType == null || realLearnPhase == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			
			// 根据第一次录入的情况验证参数
			TranOps oldTranOps = tranOpsService.getTranOpsById(questionId);
			if (oldTranOps == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			int oldComplete = oldTranOps.getComplete();
			
			// 第一次{只录题干} + 第二次{只更新答案}或{更新题干+答案}
			if (TranOpsCompleteStatus.NOT_COMPLETE.getId() == oldComplete && TranOpsCompleteStatus.COMPLETE.getId() == complete) {
				// 题干可以为空，答案必须不为空
				if (StringUtils.isEmpty(answer) || StringUtils.isEmpty(solution)) {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
				}
				if(realType == EnumSubjectType.RADIO.getId() || realType == EnumSubjectType.CHECK.getId()){
					if(StringUtils.isEmpty(selectContent) || selectOptionArray.length <= 0){
						return errorJson(EnumResCode.SERVER_ERROR.value(), "单选，多选,参数异常");
					}
				}
			}
			if(target == null){
				target = 0;
			}
			
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			if(StringUtils.isNotEmpty(content))
				content = content.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(latex))
				latex = latex.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(answer))
				answer = answer.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(answerLatex))
				answerLatex = answerLatex.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(solution))
				solution = solution.replaceAll("'&nbsp';", "&nbsp");
			
			if(StringUtils.isNotEmpty(selectContent))
				selectContent = selectContent.replaceAll("'&nbsp';", "&nbsp");
			if( selectOptionArray != null && selectOptionArray.length > 0) {
				List<Object> options =new ArrayList();
				for(String option : selectOptionArray) {
					option = URLDecoder.decode(option, "UTF-8");
					options.add(option);
				}			
				selectOption = JSONArray.toJSONString(options);
			}
			
			TranOps tranOps = new TranOps(questionId, complete, subject, 
					target, realType, realLearnPhase, content, latex, answer, 
					answerLatex, solution,operatorName, teamid,
					new Date(), selectContent, selectOption);

			boolean updateSuccess = tranOpsService.updateTranOps(tranOps);
			if(updateSuccess)
				return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "更新失败");
	}
	
	@RequestMapping(value = "/addTranOps")
	public @ResponseBody ResponseResult addTranOps(HttpServletRequest request, Integer complete, Integer subject, Integer target, Integer realType, Integer realLearnPhase,
			String content, String selectContent , String[] selectOptionArray ,String selectOption, String latex, String answer, String answerLatex, String solution) {

		try {
			if (complete == null || subject == null || realType == null || realLearnPhase == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			if (TranOpsCompleteStatus.NOT_COMPLETE.getId() == complete) {
				if (StringUtils.isEmpty(content) || StringUtils.isEmpty(latex)) {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
				}
			} else {
				if (StringUtils.isEmpty(content) || StringUtils.isEmpty(latex) || StringUtils.isEmpty(answer) || StringUtils.isEmpty(solution)) {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
				}
				if(realType == EnumSubjectType.RADIO.getId() || realType == EnumSubjectType.CHECK.getId()){
					if(StringUtils.isEmpty(selectContent) || selectOptionArray.length <= 0){
						return errorJson(EnumResCode.SERVER_ERROR.value(), "单选，多选,参数异常");
					}
				}
			}
			if(target == null){
				target = 0;
			}
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			if(StringUtils.isNotEmpty(content))
				content = content.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(latex))
				latex = latex.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(answer))
				answer = answer.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(answerLatex))
				answerLatex = answerLatex.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(solution))
				solution = solution.replaceAll("'&nbsp';", "&nbsp");

			if(StringUtils.isNotEmpty(selectContent))
				selectContent = selectContent.replaceAll("'&nbsp';", "&nbsp");
			if( selectOptionArray != null && selectOptionArray.length > 0) {
				List<Object> options =new ArrayList();
				for(String option : selectOptionArray) {
					option = URLDecoder.decode(option, "UTF-8");
					options.add(option);
				}			
				selectOption = JSONArray.toJSONString(options);
			}
			
			TranOps tranOps = new TranOps(complete, subject, target, realType, realLearnPhase, content, latex, answer, answerLatex, solution, operatorName, teamid, new Date(), selectContent, selectOption);

			tranOps = tranOpsService.insertSelective(tranOps);
			if(tranOps != null)
				return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "录题失败");
	}
	
	@RequestMapping(value = "/audit")
	public @ResponseBody ResponseResult audit(HttpServletRequest request, String questionIds, String status, Integer reason, String reasonStr) {
		try {
			if (StringUtils.isEmpty(questionIds)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			if (StringUtils.isEmpty(status)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			if (TranOpsAuditStatus.AUDIT_NOT_THROUGH.getId().equals(status) && reason == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			if(StringUtils.isNotEmpty(reasonStr))
				reasonStr = new String(reasonStr.getBytes("ISO-8859-1"), "UTF-8");
			String[] audioIdArray = questionIds.split(",");
			Long[] audioIds = new Long[audioIdArray.length];
			for(int i = 0; i < audioIdArray.length; i++) {
				if(StringUtils.isEmpty(audioIdArray[i]) || !StringUtils.isNumeric(audioIdArray[i]))
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
				audioIds[i] = Long.valueOf(audioIdArray[i]);
			}
			HttpSession session = request.getSession();
			String approvor = (String) session.getAttribute(SessionConstant.USER_NAME);
			String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
			tranOpsService.auditTranOps(audioIds, approvor, groupName, status, reason, reasonStr);
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "审核失败");
	}
}
