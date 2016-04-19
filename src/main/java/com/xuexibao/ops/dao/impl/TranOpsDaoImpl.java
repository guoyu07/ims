package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.ITranOpsDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.TranOps;

@Repository
public class TranOpsDaoImpl extends EntityDaoImpl<TranOps> implements ITranOpsDao  {

	@Override
	public long searchCount(Long questionId, Integer subject, Integer realType,Integer realLearnPhase,String teacher,
			String status, Integer complete, Date startDate, Date endDate, String approvor, Date approveStartDate, Date approveEndDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("subject", subject);
		para.put("realType", realType);
		para.put("realLearnPhase", realLearnPhase);
		para.put("teacher", teacher);
		para.put("status", status);
		para.put("complete", complete);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("approvor", approvor);
		para.put("approveStartDate", approveStartDate);
		para.put("approveEndDate", approveEndDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	@Override
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

	@Override
	public List<TranOps> searchList(Long questionId, Integer subject, Integer realType,Integer realLearnPhase,
			String teacher, String status, Integer complete, Date startDate, Date endDate, String approvor, Date approveStartDate, Date approveEndDate,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("questionId", questionId);
		para.put("subject", subject);
		para.put("realType", realType);
		para.put("realLearnPhase", realLearnPhase);
		para.put("teacher", teacher);
		para.put("status", status);
		para.put("complete", complete);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("approvor", approvor);
		para.put("approveStartDate", approveStartDate);
		para.put("approveEndDate", approveEndDate);
		List<TranOps> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	@Override
	public List<TranOps> searchTeacherList(Long questionId, Integer subject, Integer status, String teacher, Integer teacherTeam,
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
		List<TranOps> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchTeacherList", para);
		return results;
	}
	
	@Override
	public long searchCountByTeam(Integer teamid, Long questionId, Integer subject, Integer realType,Integer realLearnPhase,String teacher,
			String status, Integer complete, Date startDate, Date endDate, Date approveStartDate, Date approveEndDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("subject", subject);
		para.put("realType", realType);
		para.put("realLearnPhase", realLearnPhase);
		para.put("teacher", teacher);
		para.put("status", status);
		para.put("complete", complete);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("teamid", teamid);
		para.put("approveStartDate", approveStartDate);
		para.put("approveEndDate", approveEndDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<TranOps> searchListByTeam(Integer teamid, Long questionId, Integer subject,Integer realType,Integer realLearnPhase,
			String teacher, String status, Integer complete, Date startDate, Date endDate, Date approveStartDate, Date approveEndDate,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("questionId", questionId);
		para.put("subject", subject);
		para.put("realType", realType);
		para.put("realLearnPhase", realLearnPhase);
		para.put("teacher", teacher);
		para.put("status", status);
		para.put("complete", complete);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("teamid", teamid);
		para.put("approveStartDate", approveStartDate);
		para.put("approveEndDate", approveEndDate);
		List<TranOps> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	@Override
	public long searchCount(Long questionId, Integer subject,Integer realType,Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("subject", subject);
		para.put("realType", realType);
		para.put("realLearnPhase", realLearnPhase);
		para.put("teacher", teacher);
		para.put("status", status);
		para.put("complete", complete);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<TranOps> searchList(Long questionId, Integer subject,Integer realType,Integer realLearnPhase,
			String teacher, String status, Integer complete, Date startDate, Date endDate,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("questionId", questionId);
		para.put("subject", subject);
		para.put("realType", realType);
		para.put("realLearnPhase", realLearnPhase);
		para.put("teacher", teacher);
		para.put("status", status);
		para.put("complete", complete);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<TranOps> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	@Override
	public long searchContentCount(long teamId,  String transStartDate, String transEndDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		para.put("transStartDate", transStartDate);
		para.put("transEndDate", transEndDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchTeamContentCount", para);
		return count;
	}
	
	@Override
	public long searchCompleteCount(long teamId,  String transStartDate, String transEndDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		para.put("transStartDate", transStartDate);
		para.put("transEndDate", transEndDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchTeamCompleteCount", para);
		return count;
	}
	
	@Override
	public long searchCheckCount(long teamId, int status, String checkStartDate, String checkEndDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		para.put("status", Integer.toString(status));
		para.put("checkStartDate", checkStartDate);
		para.put("checkEndDate", checkEndDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchTeamCheckCount", para);
		return count;
	}
	
	@Override
	public int updateTranOpsOperatorByUserKey(Integer teamId,String userKey){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operatorTeamId", teamId);
		para.put("operatorName", userKey);
		int count = getSqlSessionTemplate().update(getNameSpace() + ".updateTranOpsOperatorByUserKey", para);
		return count;
	}

    @Override
	public int updateTranOpsContentOperatorByUserKey(Integer teamId,String userKey){
    	Map<String, Object> para = new HashMap<String, Object>();
		para.put("contentOperatorTeamId", teamId);
		para.put("contentOperatorName", userKey);
		int count = getSqlSessionTemplate().update(getNameSpace() + ".updateTranOpsContentOperatorByUserKey", para);
		return count;
	}

	@Override
	public List<TranOps> searchIds(Date startDate, Date endDate, Integer teamId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("teamid", teamId);
		List<TranOps> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchIds", para);
		return results;
	}
	
	@Override
	public TranOps getByPictureId(Long pictureId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("orcPictureId", pictureId);
		List<TranOps> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getByPictureId", para);
		 if(results != null && results.size() == 1) {
			 TranOps tranOps=results.get(0);
			 return tranOps;
		 }
		return null;
	}
	@Override
	public TranOps getByIdWithoutJoin(Long id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", id);
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".getByIdWithoutJoin", para);
	}
	@Override
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
	@Override
	public List<TranOps> getSignMarkList(Integer processStatus, Integer limit){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("processStatus", processStatus);
		para.put("limit", limit);
		List<TranOps> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getSignMarkList", para);
		return results;		
	}
	@Override
	public void recallDisableData(){
		getSqlSessionTemplate().update(getNameSpace() + ".recallDisableData");		
	}
	@Override
	public void updateProcessStatusById(Long Id, Integer processStatus){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", Id);
		para.put("processStatus", processStatus);
		getSqlSessionTemplate().update(getNameSpace() + ".updateProcessStatusById", para);		
	}
	@Override
	public TranOps auditNext(Integer teacherTeam) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teacherTeam", teacherTeam);
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".auditNext", para);
	}
	
}
