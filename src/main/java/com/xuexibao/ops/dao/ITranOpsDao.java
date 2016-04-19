package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.TranOps;
public interface ITranOpsDao extends IEntityDao<TranOps>   {

	long searchCount(Long questionId, Integer subject, Integer realType, Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate, String approvor, Date auditStartTime, Date auditEndTime);
	
	long searchCount(Long questionId, Integer subject, Integer realType, Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate);
	
	long searchCountByTeam(Integer teamid, Long questionId, Integer subject, Integer realType, Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate, Date auditStartTime, Date auditEndTime);
	
	
	public long searchContentCount(long teamId,  String transStartDate, String transEndDate);
	
	public long searchCompleteCount(long teamId,  String transStartDate, String transEndDate);
	
	public long searchCheckCount(long teamId, int status, String checkStartDate, String checkEndDate);
	
	List<TranOps> searchList(Long questionId, Integer subject, Integer realType, Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate, String approvor, Date auditStartTime, Date auditEndTime, Long page, int limit);
	
	List<TranOps> searchListByTeam(Integer teamid, Long questionId, Integer subject, Integer realType, Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate, Date auditStartTime, Date auditEndTime, Long page, int limit);
	
	List<TranOps> searchList(Long questionId, Integer subject, Integer realType, Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate, Long page, int limit);
	
	List<TranOps> searchIds(Date startDate, Date endDate, Integer teamId);

	public int updateTranOpsOperatorByUserKey(Integer teamId,String userKey);
	public int updateTranOpsContentOperatorByUserKey(Integer teamId,String userKey);

	public TranOps insertSelective(TranOps tranOps);
	
	public TranOps getByPictureId(Long pictureId);
	List<TranOps> searchTeacherList(Long questionId, Integer subject,
			Integer status, String teacher, Integer teacherTeam,
			Date startEditDate, Date endEditDate, Date startAuditDate,
			Date endAuditDate, Long page, int limit, boolean audit);

	long searchTeacherCount(Long questionId, Integer subject, Integer status,
			String teacher, Integer teacherTeam, Date startEditDate,
			Date endEditDate, Date startAuditDate, Date endAuditDate);

	int auditTeacherTranOps(Long[] questionId, String approvor,
			Integer status, String reason, Date auditDate);
	
	public List<TranOps> getSignMarkList(Integer processStatus, Integer limit);
	public void recallDisableData();
	public void updateProcessStatusById(Long questionId, Integer processStatus);

	TranOps getByIdWithoutJoin(Long id);
	TranOps auditNext(Integer teacherTeam);
}
