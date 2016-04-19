package com.xuexibao.ops.web;

import java.util.Date;
import java.util.LinkedList;
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
import com.xuexibao.ops.dto.KnowledgeNode;
import com.xuexibao.ops.dto.KnowledgeTree;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.enumeration.EnumTeacherAuditStatus;
import com.xuexibao.ops.model.Knowledge;
import com.xuexibao.ops.model.QuestionKnowledge;
import com.xuexibao.ops.model.TencentQuestion;
import com.xuexibao.ops.service.KnowledgeService;
import com.xuexibao.ops.service.TencentQuestionService;
import com.xuexibao.ops.task.QuestionAddSignTask;
import com.xuexibao.ops.util.DateUtils;

/**
 * 
 * @author 赵超群
 * 
 */
@Controller
@RequestMapping(value = "/teacher")
public class TeacherController extends AbstractController {
	
	private static final int limit = 30;
	@Autowired
	protected TencentQuestionService tencentQuestionService;
	@Autowired
	protected KnowledgeService knowledgeService;
	
	@RequestMapping(value = "/getNext")
	public String getNext(HttpServletRequest request, ModelMap model) {
		try {
			TencentQuestion tencentQuestion = QuestionAddSignTask.tranOpsSignList.poll();
			if(tencentQuestion != null) {
				tencentQuestion.setProcessStatus(2);
				tencentQuestionService.updateIfNecessary(tencentQuestion);
				
				tencentQuestion = tencentQuestionService.getByIdWithoutJoin(tencentQuestion.getId());
				if(StringUtils.isNotEmpty(tencentQuestion.getRealKnowledge()) && StringUtils.isNotEmpty(tencentQuestion.getKnowledge())) {
					String[] realKnwArray = tencentQuestion.getRealKnowledge().split(",");
					String[] knwArray = tencentQuestion.getKnowledge().split(",");
					KnowledgeNode[] nodeList = new KnowledgeNode[realKnwArray.length];
					if(realKnwArray.length == knwArray.length) {
						for(int i = 0; i < realKnwArray.length; i++) {
							nodeList[i] = new KnowledgeNode(realKnwArray[i], knwArray[i]);
						}
					}
					tencentQuestion.setKnowledgeNodeArray(nodeList);
				}
				if(StringUtils.isNotEmpty(tencentQuestion.getQuestionCategory()))
					tencentQuestion.setQuestionCategoryArray(tencentQuestion.getQuestionCategory().split(","));
			} else {
				return tranOpsEditSearch(request, model, null, null, null, null, null, null);
			}
			model.addAttribute("tranOps", tencentQuestion);
			return "teacher/tranOpsEditDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/editDetail")
	public String editDetail(HttpServletRequest request, ModelMap model, Long questionId) {
		try {
			TencentQuestion tencentQuestion = tencentQuestionService.getByIdWithoutJoin(questionId);
			if(StringUtils.isNotEmpty(tencentQuestion.getRealKnowledge()) && StringUtils.isNotEmpty(tencentQuestion.getKnowledge())) {
				String[] realKnwArray = tencentQuestion.getRealKnowledge().split(",");
				String[] knwArray = tencentQuestion.getKnowledge().split(",");
				KnowledgeNode[] nodeList = new KnowledgeNode[realKnwArray.length];
				if(realKnwArray.length == knwArray.length) {
					for(int i = 0; i < realKnwArray.length; i++) {
						nodeList[i] = new KnowledgeNode(realKnwArray[i], knwArray[i]);
					}
				}
				tencentQuestion.setKnowledgeNodeArray(nodeList);
			}
			if(tencentQuestion != null && StringUtils.isNotEmpty(tencentQuestion.getQuestionCategory()))
				tencentQuestion.setQuestionCategoryArray(tencentQuestion.getQuestionCategory().split(","));
			model.addAttribute("tranOps", tencentQuestion);
			return "teacher/tranOpsEditDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/auditNext")
	public String auditNext(HttpServletRequest request, ModelMap model) {
		try {
			HttpSession session = request.getSession();
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			TencentQuestion tencentQuestion = tencentQuestionService.auditNext(teamid);
			if(tencentQuestion == null) return tranOpsAuditSearch(request, model, null, null, null, null, null, null);
			if(StringUtils.isNotEmpty(tencentQuestion.getRealKnowledge()) && StringUtils.isNotEmpty(tencentQuestion.getKnowledge())) {
				String[] realKnwArray = tencentQuestion.getRealKnowledge().split(",");
				String[] knwArray = tencentQuestion.getKnowledge().split(",");
				KnowledgeNode[] nodeList = new KnowledgeNode[realKnwArray.length];
				if(realKnwArray.length == knwArray.length) {
					for(int i = 0; i < realKnwArray.length; i++) {
						nodeList[i] = new KnowledgeNode(realKnwArray[i], knwArray[i]);
					}
				}
				tencentQuestion.setKnowledgeNodeArray(nodeList);
			}
			if(tencentQuestion != null && StringUtils.isNotEmpty(tencentQuestion.getQuestionCategory()))
				tencentQuestion.setQuestionCategoryArray(tencentQuestion.getQuestionCategory().split(","));
			model.addAttribute("tranOps", tencentQuestion);
			return "teacher/tranOpsAuditDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/auditDetail")
	public String auditDetail(HttpServletRequest request, ModelMap model, Long questionId) {
		try {
			TencentQuestion tranOps = tencentQuestionService.getByIdWithoutJoin(questionId);
			if(StringUtils.isNotEmpty(tranOps.getRealKnowledge()) && StringUtils.isNotEmpty(tranOps.getKnowledge())) {
				String[] realKnwArray = tranOps.getRealKnowledge().split(",");
				String[] knwArray = tranOps.getKnowledge().split(",");
				KnowledgeNode[] nodeList = new KnowledgeNode[realKnwArray.length];
				if(realKnwArray.length == knwArray.length) {
					for(int i = 0; i < realKnwArray.length; i++) {
						nodeList[i] = new KnowledgeNode(realKnwArray[i], knwArray[i]);
					}
				}
				tranOps.setKnowledgeNodeArray(nodeList);
			}
			if(tranOps != null && StringUtils.isNotEmpty(tranOps.getQuestionCategory()))
				tranOps.setQuestionCategoryArray(tranOps.getQuestionCategory().split(","));
			model.addAttribute("tranOps", tranOps);
			return "teacher/tranOpsAuditDetail";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/tranOpsEditSearch")
	public String tranOpsEditSearch(HttpServletRequest request, ModelMap model,
			Long questionId, Integer subject, Integer status, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			HttpSession session = request.getSession();
			String teacher = (String) session.getAttribute(SessionConstant.USER_NAME);
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			long totalNum = tencentQuestionService.searchTeacherCount(questionId, subject, status, teacher, null, startDate, endDate, null, null);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<TencentQuestion> tranOpsList = tencentQuestionService.searchTeacherList(questionId, subject, status, teacher, null, startDate, endDate, null, null, page * limit, limit, false);
			model.addAttribute("questionId", questionId);
			model.addAttribute("subject", subject);
			model.addAttribute("status", status);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("tranOpsList", tranOpsList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "teacher/tranOpsEditList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@RequestMapping(value = "/tranOpsAuditSearch")
	public String tranOpsAuditSearch(HttpServletRequest request, ModelMap model,
			Long questionId, Integer subject, Integer status, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			HttpSession session = request.getSession();
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			long totalNum = tencentQuestionService.searchTeacherCount(questionId, subject, status, null, teamid, null, null, startDate, endDate);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<TencentQuestion> tranOpsList = tencentQuestionService.searchTeacherList(questionId, subject, status, null, teamid, null, null, startDate, endDate, page * limit, limit, true);
			model.addAttribute("questionId", questionId);
			model.addAttribute("subject", subject);
			model.addAttribute("status", status);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("tranOpsList", tranOpsList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "teacher/tranOpsAuditList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/updateTranOps")
	public @ResponseBody ResponseResult updateTranOps(HttpServletRequest request, Long questionId, Integer learnPhase,
			Integer subject, Integer realType, String[] realKnowledge, String[] knowledgeText, Integer realDiff, Integer[] questionCategory) {
		try {
			if (questionId == null || learnPhase == null || subject == null || realType == null 
					|| realKnowledge == null || realKnowledge.length == 0 || realDiff == null || questionCategory == null)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			// 根据第一次录入的情况验证参数
			TencentQuestion oldTranOps = tencentQuestionService.getTranOpsById(questionId);
			if (oldTranOps == null)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			HttpSession session = request.getSession();
			String teacher = (String) session.getAttribute(SessionConstant.USER_NAME);
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			TencentQuestion tranOps = new TencentQuestion(questionId, learnPhase, subject, realType, realKnowledge, knowledgeText, realDiff, 
					questionCategory, teacher, teamid, new Date(), EnumTeacherAuditStatus.COMPLETE.getId(), 3);
			QuestionKnowledge[] questArray = new QuestionKnowledge[realKnowledge.length];
			for(int i = 0; i < realKnowledge.length; i++) {
				questArray[i] = new QuestionKnowledge(questionId, realKnowledge[i], new Date());
			}
			boolean updateSuccess = tencentQuestionService.updateMarkInfo(tranOps, questArray);
			if(updateSuccess)
				return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "更新失败");
	}
	
	@RequestMapping(value = "/audit")
	public @ResponseBody ResponseResult audit(HttpServletRequest request, Long[] questionId, Integer status, String reason) {
		try {
			if (questionId == null || questionId.length == 0 || status == null)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			if(status == EnumTeacherAuditStatus.AUDIT_NOT_THROUGH.getId() && StringUtils.isEmpty(reason))
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
//			if(StringUtils.isNotEmpty(reason))
//				reason = new String(reason.getBytes("ISO-8859-1"), "UTF-8");
			HttpSession session = request.getSession();
			String approver = (String) session.getAttribute(SessionConstant.USER_NAME);
			tencentQuestionService.auditTeacherTranOps(questionId, approver, status, reason, new Date());
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "审核失败");
	}
	
	@RequestMapping(value = "/getKnowledgeTree")
	public @ResponseBody ResponseResult getKnowledgeTree(HttpServletRequest request, Integer learnPhase, Integer subject, String keyWord) {
		try {
			if (learnPhase == null || subject == null)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			List<Knowledge> knowledgeList = knowledgeService.getKnowledgeTree(learnPhase, subject, keyWord);
			List<KnowledgeTree> treeList = new LinkedList<>();
			for(Knowledge knowledge : knowledgeList) {
				if(treeList.size() == 0 || !treeList.get(treeList.size() - 1).getText().equals(knowledge.getKnowledgeSummary())) {
					KnowledgeTree tree = new KnowledgeTree(knowledge.getKnowledgeSummary());
					treeList.add(tree);
				}
				treeList.get(treeList.size() - 1).getNodes().add(new KnowledgeNode(knowledge));
			}
			return successJson(treeList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "查询失败");
	}
}
