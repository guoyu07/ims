package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.dao.IBooksDao;
import com.xuexibao.ops.dao.IOrcPictureDao;
import com.xuexibao.ops.dao.ITranOpsApproveDao;
import com.xuexibao.ops.dao.ITranOpsDao;
import com.xuexibao.ops.dao.impl.TencentQuestionDao;
import com.xuexibao.ops.enumeration.EnumBookBest;
import com.xuexibao.ops.enumeration.EnumGradeType;
import com.xuexibao.ops.enumeration.EnumSubject;
import com.xuexibao.ops.enumeration.EnumSubjectType;
import com.xuexibao.ops.enumeration.TranOpsAuditReason;
import com.xuexibao.ops.enumeration.TranOpsAuditStatus;
import com.xuexibao.ops.enumeration.TranOpsCompleteStatus;
import com.xuexibao.ops.model.OrcPicture;
import com.xuexibao.ops.model.QuestionKnowledge;
import com.xuexibao.ops.model.TencentQuestion;
import com.xuexibao.ops.model.TikuTeam;
import com.xuexibao.ops.model.TranOps;
import com.xuexibao.ops.model.TranOpsApprove;

@Service
public class TranOpsService {
	
	@Resource
	IOrcPictureDao orcPictureDao;
	@Resource
	ITranOpsDao tranOpsDao;
	@Resource
	TikuTeamService tikuTeamService;
	@Resource
	ITranOpsApproveDao tranOpsApproveDao;
	@Resource
	QuestionKnowledgeService questionKnowledgeService;
	@Resource
	IBooksDao booksDao;
	@Resource
	TencentQuestionDao tencentQuestionDao;
	
	public TranOps getTranOpsById(Long questionId) {
		TranOps tranOps = tranOpsDao.getById(questionId);
		if(tranOps != null) {
			setSubject(tranOps);
			setType(tranOps);
			setLearnphase(tranOps);
			setStatus(tranOps);
			setIsRerecord(tranOps);
			String knowledge = tranOps.getKnowledge();
			if(StringUtils.isNotEmpty(knowledge)) {
				tranOps.setKnowledgeArray(knowledge.split(","));
			}
		}
		return tranOps;
	}
	
	public TranOps getTranOpsByPictureId(Long pictureId) {
		TranOps tranOps = tranOpsDao.getByPictureId(pictureId);
		if(tranOps != null) {
			setSubject(tranOps);
			setType(tranOps);
			setLearnphase(tranOps);
			setStatus(tranOps);
			setIsRerecord(tranOps);
			String knowledge = tranOps.getKnowledge();
			if(StringUtils.isNotEmpty(knowledge)) {
				tranOps.setKnowledgeArray(knowledge.split(","));
			}
		}
		return tranOps;
	}
	
	public TranOps insertSelective(TranOps tranOps) {
		return tranOpsDao.insertSelective(tranOps);
	}
	
	private TranOps clearAnswer(TranOps tranOps) {
		tranOps.setAnswer(null);
		tranOps.setAnswerlatex(null);
		tranOps.setSolution(null);
		tranOps.setKnowledge(null);
		return tranOps;
	}
	private TranOps clearContent(TranOps tranOps) {
		tranOps.setContent(null);
		tranOps.setLatex(null);
		return tranOps;
	}
	
	public boolean updateIfNecessary(TranOps tranOps) {
		int updateNum = tranOpsDao.updateIfNecessary(tranOps);
		if (updateNum == 1) {
			return true;
		} else {
			return false;
		}
	}
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public boolean updateMarkInfo(TranOps tranOps,QuestionKnowledge[] questKnowArray) {
		questionKnowledgeService.saveQuesKnow(questKnowArray);
		int updateNum = tranOpsDao.updateIfNecessary(tranOps);
		if (updateNum == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean updateTranOps(TranOps tranOps) {
		// 设置为待审核
		tranOps.setAuditStatus(TranOpsAuditStatus.PENDING_AUDIT.getId());
		TranOps oldTranOps = this.getTranOpsById(tranOps.getId());
		if (oldTranOps == null) {
			return false;
		}
		int oldComplete = oldTranOps.getComplete();
		String oldStatus = oldTranOps.getAuditStatus();
		
		// 第一次{只录题干}
		if (TranOpsCompleteStatus.NOT_COMPLETE.getId() == oldComplete) {
			// 第二次{只更新题干}
			if (TranOpsCompleteStatus.NOT_COMPLETE.getId() == tranOps.getComplete()) {
				// 题干的审核状态必须是未通过或待审核
				if (!TranOpsAuditStatus.AUDIT_NOT_THROUGH.getId().equals(oldStatus) && !TranOpsAuditStatus.PENDING_AUDIT.getId().equals(oldStatus)) {
					return false;
				}
				return updateIfNecessary(this.clearAnswer(tranOps));
			// 第二次{只更新答案}或{更新题干+答案}
			} else {
				// 因为题干已经审核通过，{只更新答案}
				if (TranOpsAuditStatus.HALF_THROUGH.getId().equals(oldStatus)) {
					return updateIfNecessary(this.clearContent(tranOps));
				}
				// {更新题干+答案}
				// 题干的审核状态必须是未通过或待审核
				if (!TranOpsAuditStatus.AUDIT_NOT_THROUGH.getId().equals(oldStatus) && !TranOpsAuditStatus.PENDING_AUDIT.getId().equals(oldStatus)) {
					return false;
				}
				return updateIfNecessary(tranOps);
			}
		// 第一次{录入题干+答案}
		} else {
			// 题目的审核状态必须是未通过或待审核
			if (!TranOpsAuditStatus.AUDIT_NOT_THROUGH.getId().equals(oldStatus) && !TranOpsAuditStatus.PENDING_AUDIT.getId().equals(oldStatus)) {
				return false;
			}
			return updateIfNecessary(tranOps);
		}
	}

	public long searchCount(Long questionId, Integer subject,  Integer realType, Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate) {
		return tranOpsDao.searchCount(questionId, subject, realType, realLearnPhase, teacher,
				status, complete, startDate, endDate);
	}
	public long searchTeacherCount(Long questionId, Integer subject, Integer status, String teacher, Integer teacherTeam,
			Date startEditDate, Date endEditDate, Date startAuditDate, Date endAuditDate) {
		return tranOpsDao.searchTeacherCount(questionId, subject, status, teacher, teacherTeam,
				startEditDate, endEditDate, startAuditDate, endAuditDate);
	}

	public List<TranOps> searchList(Long questionId, Integer subject, Integer realType, Integer realLearnPhase,
			String teacher, String status, Integer complete, Date startDate, Date endDate,
			Long page, int limit) {
		List<TranOps> tranOpsList = tranOpsDao.searchList(questionId, subject, realType, realLearnPhase,
				teacher, status, complete, startDate, endDate,
				page, limit);
		for(TranOps audio : tranOpsList) {
			setSubject(audio);
			setType(audio);
			setLearnphase(audio);
			setStatus(audio);
			setIsRerecord(audio);
			setComplete(audio);
		}
		return tranOpsList;
	}
	
	public long searchCount(Long questionId, Integer subject, Integer realType, Integer realLearnPhase, String status, Integer complete, Date startDate, Date endDate) {
		return tranOpsDao.searchCount(questionId, subject, realType, realLearnPhase, null, status, complete, startDate, endDate);
	}
	
	public List<TranOps> searchList(Long questionId, Integer subject,  Integer realType, Integer realLearnPhase,String status, Integer complete, Date startDate, Date endDate,
			Long page, int limit) {
		List<TranOps> tranOpsList = tranOpsDao.searchList(questionId, subject, realType, realLearnPhase, null, status, complete, startDate, endDate,
				page, limit);
		for(TranOps audio : tranOpsList) {
			setSubject(audio);
			setType(audio);
			setLearnphase(audio);
			setStatus(audio);
			setIsRerecord(audio);
			setComplete(audio);
		}
		return tranOpsList;
	}
	public List<TranOps> searchTeacherList(Long questionId, Integer subject, Integer status, String teacher, Integer teacherTeam,
			Date startEditDate, Date endEditDate, Date startAuditDate, Date endAuditDate, Long page, int limit, boolean audit) {
		List<TranOps> tranOpsList = tranOpsDao.searchTeacherList(questionId, subject, status, teacher, teacherTeam,
				startEditDate, endEditDate, startAuditDate, endAuditDate, page, limit, audit);
		for(TranOps audio : tranOpsList) {
			setSubject(audio);
			setType(audio);
			setLearnphase(audio);
			setStatus(audio);
			setIsRerecord(audio);
			setComplete(audio);
		}
		return tranOpsList;
	}
	public long searchCountByTeam(Integer teamid, Long questionId, Integer subject, Integer realType, Integer realLearnPhase, String teacher,
			String status, Integer complete, Date startDate, Date endDate, Date auditStartTime, Date auditEndTime) {
		return tranOpsDao.searchCountByTeam(teamid, questionId, subject, realType, realLearnPhase, teacher,
				status, complete, startDate, endDate, auditStartTime, auditEndTime);
	}
	
	public List<TranOps> searchListByTeam(Integer teamid, Long questionId, Integer subject, Integer realType, Integer realLearnPhase,
			String teacher, String status, Integer complete, Date startDate, Date endDate, Date auditStartTime, Date auditEndTime,
			Long page, int limit) {
		List<TranOps> tranOpsList = tranOpsDao.searchListByTeam(teamid, questionId, subject, realType, realLearnPhase,
				teacher, status, complete, startDate, endDate, auditStartTime, auditEndTime,
				page, limit);
		for(TranOps audio : tranOpsList) {
			setSubject(audio);
			setType(audio);
			setLearnphase(audio);
			setStatus(audio);
			setIsRerecord(audio);
			setComplete(audio);
		}
		return tranOpsList;
	}
	public List<TranOps> searchListByTeamWithTeamName(Integer teamid, Long questionId, Integer subject, Integer realType, Integer realLearnPhase,
			String teacher, String status, Integer complete, Date startDate, Date endDate, Date auditStartTime, Date auditEndTime,
			Long page, int limit) {
		List<TranOps> tranOpsList = tranOpsDao.searchListByTeam(teamid, questionId, subject, realType, realLearnPhase,
				teacher, status, complete, startDate, endDate, auditStartTime, auditEndTime,
				page, limit);
		for(TranOps audio : tranOpsList) {
			setSubject(audio);
			setType(audio);
			setLearnphase(audio);
			setStatus(audio);
			setIsRerecord(audio);
			setComplete(audio);
			setTikuTeam(audio);
		}
		return tranOpsList;
	}
	
	// 审核原因
	private String getAuditReason(String status, Integer reason, String reasonStr) {
		if(TranOpsAuditStatus.AUDIT_THROUGH.getId().equals(status)) {
			reasonStr = null;
		} else if(TranOpsAuditStatus.AUDIT_NOT_THROUGH.getId().equals(status)
				&& TranOpsAuditReason.OTHER.getId() != reason) {
			for(TranOpsAuditReason enumReason : TranOpsAuditReason.values()) {
				if(reason == enumReason.getId()) {
					reasonStr = enumReason.getDesc();
					break;
				}
			}
		}
		return reasonStr;
	}
	
	// 限制一秒内有多个审核结果
	private Date getAuditApproveTime(TranOps tranOps, Date date) {
		Date lastApproveTime = tranOps.getApproveTime();
		if(lastApproveTime != null) {
//			if(date.getTime() - lastApproveTime.getTime() <= TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS)) {
			if(date.getTime() / 1000 <= lastApproveTime.getTime() / 1000) {
				date = DateUtils.addSeconds(lastApproveTime, 1);
			}
		}
		return date;
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	private synchronized boolean auditTranOps(Long id, String approvor, String groupName, String status, Integer reason, String reasonStr) {
		// 通过id查询tranOps，不存在则退出此次循环
		TranOps tranOps = tranOpsDao.getById(id);
		if(tranOps == null) {
			return false;
		}
		int complete = tranOps.getComplete();
		// 新的审核结果与旧结果一致，不进行审核
		if(status.equals(tranOps.getAuditStatus())) {
			return false;
		}
		// 保证两张表的审核时间是同一秒
		Date approveTime = getAuditApproveTime(tranOps, new Date());
		// half though（题干通过，但答案不通过）
		if(TranOpsCompleteStatus.NOT_COMPLETE.getId() == complete && TranOpsAuditStatus.AUDIT_THROUGH.getId().equals(status))
			status = TranOpsAuditStatus.HALF_THROUGH.getId();
		// 新增审核结果
		TranOpsApprove approve = new TranOpsApprove(id, complete, approvor, status, 
				getAuditReason(status, reason, reasonStr), approveTime);
		tranOpsApproveDao.insert(approve);
		
		// 审核成功 --> 是精品书（tran_ops的orc_picture_id关联orc_picture表，再判断orc_picutre的bookid是否是精品书） --> 插入tiku_question表
		if(TranOpsAuditStatus.AUDIT_THROUGH.getId().equals(status)) {
			Integer best = booksDao.getBestByTranId(id);
			if(best != null && best == EnumBookBest.BEST.getId()) {
				TencentQuestion question = tencentQuestionDao.getByTranId(id);
				if(question == null) {
					question = new TencentQuestion(tranOps);
					tencentQuestionDao.insertSelective(question);
				} else {
					question.setContent(tranOps.getContent());
					question.setLatex(tranOps.getLatex());
					question.setAnswer(tranOps.getAnswer());
					question.setAnswerlatex(tranOps.getAnswerlatex());
					question.setSolution(tranOps.getSolution());
					question.setKnowledge(tranOps.getKnowledge());
					question.setSelectContent(tranOps.getSelectContent());
					question.setSelectOption(tranOps.getSelectOption());
					tencentQuestionDao.updateIfNecessary(question);
				}
				OrcPicture orcPicture = orcPictureDao.getById(tranOps.getOrcPictureId());
				if( orcPicture != null){
					if( orcPicture.getStatus() == 6){
						status = TranOpsAuditStatus.BEST_AUDIT_THROUGH.getId();
					}else{
						status = TranOpsAuditStatus.AUDIT_THROUGH.getId();
					}
				}
			}
		}
		
		// 更新tranOps
		tranOps.setAuditStatus(status);
		tranOps.setApproveTime(approveTime);
		tranOpsDao.updateIfNecessary(tranOps);
		return true;
	}
	
	public void auditTranOps(Long[] audioIdArray, String approvor, String groupName,
			String status, Integer reason, String reasonStr) {
		for(Long audioId : audioIdArray) {
			auditTranOps(audioId, approvor, groupName, status, reason, reasonStr);
		}
	}
	public void auditTeacherTranOps(Long[] questionId, String approver, Integer status, String reason, Date auditDate) {
		tranOpsDao.auditTeacherTranOps(questionId, approver, status, reason, auditDate);
	}
	
	private void setSubject(TranOps audio) {
    	Integer subject = audio.getRealSubject();
		if(subject != null) {
			for(EnumSubject enumsubject : EnumSubject.values()) {
				if(subject.equals(enumsubject.getId())) {
					audio.setSubject(enumsubject.getChineseName());
					break;
				}
			}
		}
	}
	private void setType(TranOps audio) {
    	Integer subject = audio.getRealType();
		if(subject != null) {
			for(EnumSubjectType enumsubject : EnumSubjectType.values()) {
				if(subject.equals(enumsubject.getId())) {
					audio.setType(enumsubject.getChineseName());
					break;
				}
			}
		}
	}
	private void setLearnphase(TranOps audio) {
    	Integer subject = audio.getRealLearnPhase();
		if(subject != null) {
			for(EnumGradeType enumsubject : EnumGradeType.values()) {
				if(subject.equals(enumsubject.getId())) {
					audio.setLearnphase(enumsubject.getChineseName());
					break;
				}
			}
		}
	}
	private void setComplete(TranOps audio) {
		Integer complete = audio.getComplete();
		if(complete != null) {
			for(TranOpsCompleteStatus enumsubject : TranOpsCompleteStatus.values()) {
				if(complete.equals(enumsubject.getId())) {
					audio.setCompleteForShow(enumsubject.getDesc());
					break;
				}
			}
		}
	}
	
	private void setTikuTeam(TranOps audio) {
		Integer complete = audio.getComplete();
		Integer teamId = null;
		TikuTeam tikuTeam = null;
		if(Integer.valueOf(TranOpsCompleteStatus.COMPLETE.getId()).equals(complete)) {
			teamId = audio.getOperatorTeamId();
		} else if(Integer.valueOf(TranOpsCompleteStatus.NOT_COMPLETE.getId()).equals(complete)) {
			teamId = audio.getContentOperatorTeamId();
		}
		if(teamId != null && (tikuTeam = tikuTeamService.getTeamById(teamId)) != null) {
			audio.setTeamNameForShow(tikuTeam.getName());
		}
	}
	
	private void setStatus(TranOps audio) {
    	String status = audio.getAuditStatus();
    	if(status != null) {
    		for(TranOpsAuditStatus audioStatus : TranOpsAuditStatus.values()) {
    			if(status.equals(audioStatus.getId())) {
    				audio.setStatusForShow(audioStatus.getDesc());
    				break;
    			}
    		}
    	}
    }
	
    private void setIsRerecord(TranOps audio) {
    	Date approveTime = audio.getApproveTime();
    	Date createTime = audio.getCreateTime();
    	if(approveTime != null && createTime != null && createTime.compareTo(approveTime) > 0) {
    		audio.setIsRerecordForShow("是");
    	} else {
    		audio.setIsRerecordForShow("否");
    	}
    }
    
    public long searchContentCount(long teamId,  String transStartDate, String transEndDate) {
		return tranOpsDao.searchContentCount(teamId, transStartDate, transEndDate);
	}
    
    public long searchCompleteCount(long teamId,  String transStartDate, String transEndDate) {
    	return tranOpsDao.searchCompleteCount(teamId, transStartDate, transEndDate);
    }
    
    public long searchCheckCount(long teamId, int status, String checkStartDate, String checkEndDate) {
    	return tranOpsDao.searchCheckCount(teamId, status,  checkStartDate, checkEndDate);
    }

	public TranOps getByIdWithoutJoin(Long id) {
		if(id == null) return null;
		return tranOpsDao.getByIdWithoutJoin(id);
	}

	public TranOps auditNext(Integer teacherTeam) {
		return tranOpsDao.auditNext(teacherTeam);
	}
	
}