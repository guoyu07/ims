package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.TencentQuestion;

@Repository
public class TencentQuestionDao extends EntityDaoImpl<TencentQuestion>  {

	public List<TencentQuestion> getSignMarkList(Integer processStatus, Integer limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("processStatus", processStatus);
		para.put("limit", limit);
		List<TencentQuestion> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getSignMarkList", para);
		return results;		
	}
	
	public void recallDisableData() {
		getSqlSessionTemplate().update(getNameSpace() + ".recallDisableData");		
	}
	
	public void updateProcessStatusById(Long Id, Integer processStatus) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", Id);
		para.put("processStatus", processStatus);
		getSqlSessionTemplate().update(getNameSpace() + ".updateProcessStatusById", para);		
	}
	
	public TencentQuestion auditNext(Integer teacherTeam) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teacherTeam", teacherTeam);
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".auditNext", para);
	}
	
	public long searchTeacherCount(Long questionId, Integer subject, Integer status, String teacher, Integer teacherTeam,
			Date startEditDate, Date endEditDate, Date startAuditDate, Date endAuditDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("subject", subject);
		para.put("teacherStatus", status);
		para.put("teacher", teacher);
		para.put("teacherTeam", teacherTeam);
		para.put("startEditDate", startEditDate);
		para.put("endEditDate", endEditDate);
		para.put("startAuditDate", startAuditDate);
		para.put("endAuditDate", endAuditDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchTeacherCount", para);
		return count;
	}
	
	public List<TencentQuestion> searchTeacherList(Long questionId, Integer subject, Integer status, String teacher, Integer teacherTeam,
			Date startEditDate, Date endEditDate, Date startAuditDate, Date endAuditDate, Long page, int limit, boolean audit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("subject", subject);
		para.put("teacherStatus", status);
		para.put("teacher", teacher);
		para.put("teacherTeam", teacherTeam);
		para.put("startEditDate", startEditDate);
		para.put("endEditDate", endEditDate);
		para.put("startAuditDate", startAuditDate);
		para.put("endAuditDate", endAuditDate);
		para.put("offset", page);
		para.put("limit", limit);
		para.put("audit", audit);
		List<TencentQuestion> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchTeacherList", para);
		return results;
	}
	
	public TencentQuestion getByIdWithoutJoin(Long id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", id);
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".getByIdWithoutJoin", para);
	}	

	public int auditTeacherTranOps(Long[] questionId, String approver, Integer status, String reason, Date auditDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("approver", approver);
		para.put("teacherStatus", status);
		para.put("auditReason", reason);
		para.put("auditDate", auditDate);
		int count = getSqlSessionTemplate().update(getNameSpace() + ".auditTeacherTranOps", para);
		return count;	
	}
	
	public TencentQuestion getByTranId(Long tranid) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("tranid", tranid);
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".getByTranId", para);
	}
	
}
