package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.impl.TencentQuestionDao;
import com.xuexibao.ops.model.QuestionKnowledge;
import com.xuexibao.ops.model.TencentQuestion;

@Service
public class TencentQuestionService {
	
	@Resource
	TencentQuestionDao tencentQuestionDao;
	@Resource
	QuestionKnowledgeService questionKnowledgeService;
	
	public TencentQuestion getTranOpsById(Long questionId) {
		TencentQuestion tencentQuestion = tencentQuestionDao.getById(questionId);
		return tencentQuestion;
	}
	
	public long searchTeacherCount(Long questionId, Integer subject, Integer status, String teacher, Integer teacherTeam,
			Date startEditDate, Date endEditDate, Date startAuditDate, Date endAuditDate) {
		return tencentQuestionDao.searchTeacherCount(questionId, subject, status, teacher, teacherTeam,
				startEditDate, endEditDate, startAuditDate, endAuditDate);
	}

	public List<TencentQuestion> searchTeacherList(Long questionId, Integer subject, Integer status, String teacher, Integer teacherTeam,
			Date startEditDate, Date endEditDate, Date startAuditDate, Date endAuditDate, Long page, int limit, boolean audit) {
		List<TencentQuestion> tranOpsList = tencentQuestionDao.searchTeacherList(questionId, subject, status, teacher, teacherTeam,
				startEditDate, endEditDate, startAuditDate, endAuditDate, page, limit, audit);
		return tranOpsList;
	}
	public void auditTeacherTranOps(Long[] questionId, String approver, Integer status, String reason, Date auditDate) {
		tencentQuestionDao.auditTeacherTranOps(questionId, approver, status, reason, auditDate);
	}

	public TencentQuestion getByIdWithoutJoin(Long id) {
		if(id == null) return null;
		return tencentQuestionDao.getByIdWithoutJoin(id);
	}

	public TencentQuestion auditNext(Integer teacherTeam) {
		return tencentQuestionDao.auditNext(teacherTeam);
	}
	
	public boolean updateMarkInfo(TencentQuestion tranOps, QuestionKnowledge[] questKnowArray) {
		questionKnowledgeService.saveQuesKnow(questKnowArray);
		int updateNum = tencentQuestionDao.updateIfNecessary(tranOps);
		if (updateNum == 1)
			return true;
		return false;
	}
	
	public boolean updateIfNecessary(TencentQuestion tranOps) {
		int updateNum = tencentQuestionDao.updateIfNecessary(tranOps);
		if (updateNum == 1)
			return true;
		return false;
	}
	
}