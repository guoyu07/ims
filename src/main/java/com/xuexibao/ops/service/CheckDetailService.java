package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.dao.ICheckDetailDao;
import com.xuexibao.ops.dao.ICheckDetailRecordDao;
import com.xuexibao.ops.dao.ICheckRecordDao;
import com.xuexibao.ops.enumeration.EnumSubject;
import com.xuexibao.ops.enumeration.TranOpsAuditStatus;
import com.xuexibao.ops.enumeration.TranOpsCheckReason;
import com.xuexibao.ops.enumeration.TranOpsCheckStatus;
import com.xuexibao.ops.enumeration.TranOpsParentCheckStatus;
import com.xuexibao.ops.model.CheckDetail;
import com.xuexibao.ops.model.CheckDetailRecord;
import com.xuexibao.ops.model.CheckRecord;

@Service
public class CheckDetailService {
	
	@Resource
	ICheckRecordDao checkRecordDao;
	@Resource
	ICheckDetailDao checkDetailDao;
	@Resource
	ICheckDetailRecordDao checkDetailRecordDao;

	public long searchCount(Long questionId,  String teacher, Integer cstatus, Integer teamid , Long parent_id , Long grand_parent_id) {
		return checkDetailDao.searchCount(questionId,  teacher,	cstatus , teamid , parent_id , grand_parent_id);
	}
	
	public List<CheckDetail> searchList(Long questionId, String teacher, Integer cstatus , Integer teamid, Long parent_id , Long grand_parent_id, 
			Long page, int limit) {
		List<CheckDetail> tranOpsDetailList = checkDetailDao.searchList(questionId, teacher, cstatus, teamid, parent_id, grand_parent_id,
				page, limit);
		for(CheckDetail audio : tranOpsDetailList) {
			
			setCstatus(audio);
			
		}
		return tranOpsDetailList;
	}
	
	
	public long searchCaptainCount(Long questionId,  String teacher, Integer cstatus, Integer teamid , Long parent_id , Long grand_parent_id) {
		return checkDetailDao.searchCaptainCount(questionId,  teacher,	cstatus , teamid , parent_id , grand_parent_id);
	}
	
	public List<CheckDetail> searchCaptainList(Long questionId, String teacher, Integer cstatus , Integer teamid, Long parent_id , Long grand_parent_id,
			Long page, int limit) {
		List<CheckDetail> tranOpsDetailList = checkDetailDao.searchCaptainList(questionId, teacher, cstatus, teamid, parent_id, grand_parent_id,
				page, limit);
		for(CheckDetail audio : tranOpsDetailList) {
			
			setCstatus(audio);
			
		}
		return tranOpsDetailList;
	}
	
	public void auditCheckTranOps(Long[] audioIdArray, String approvor, Integer status, Integer reason, String reasonStr) {
		for(Long audioId : audioIdArray) {
			auditCheckTranOps(audioId, approvor, status, reason, reasonStr);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	private boolean auditCheckTranOps(Long id, String approvor, Integer status, Integer reason, String reasonStr) {
		// 通过id查询tranOps，不存在则退出此次循环
		CheckDetail checkDetail = checkDetailDao.getById(id);
		if(checkDetail == null) {
			return false;
		}
		// status必须为合格或不合格
		if(!Integer.valueOf(TranOpsCheckStatus.ELIGIBLE.getId()).equals(status) && !Integer.valueOf(TranOpsCheckStatus.UNELIGIBLE.getId()).equals(status)) {
			return false;
		}
		// 非待检查的不可以再次抽检
		if(!Integer.valueOf(TranOpsCheckStatus.UCHECK.getId()).equals(checkDetail.getCstatus())) {
			return false;
		}
		
		// 更新checkDetail
		updateCheckDetail(checkDetail, approvor, status, reason, reasonStr);
		
		// 更新checkDetailRecord的冗余字段合格数或未合格数
		updatePassOrUnpassNum(checkDetail, status);
		
		updateCheckDetailRecordStatus(checkDetail);
		
		return true;
	}
	
	private void updateCheckDetailRecordStatus(CheckDetail checkDetail) {
		long uncheckCount = getCheckDetailUncheckCount(checkDetail.getParentId());
		if(uncheckCount == 0) {
			CheckDetailRecord checkDetailRecord = checkDetailRecordDao.getById(checkDetail.getParentId());
			checkDetailRecord.setStatus(TranOpsParentCheckStatus.CHECK.getId());
			checkDetailRecordDao.updateIfNecessary(checkDetailRecord);
			
			updateCheckRecordStatus(checkDetailRecord.getParentId());
		}
	}
	
	private void updateCheckRecordStatus(Long parentId) {
		long uncheckCount = getCheckDetailRecordUncheckCount(parentId);
		if(uncheckCount == 0) {
			CheckRecord checkRecord = checkRecordDao.getById(parentId);
			checkRecord.setStatus(TranOpsParentCheckStatus.CHECK.getId());
			checkRecordDao.updateIfNecessary(checkRecord);
			
		}
	}
	
	private int updateCheckDetail(CheckDetail checkDetail, String approvor, Integer status, Integer reason, String reasonStr) {
		checkDetail.setCstatus(status);
		checkDetail.setChecker(approvor);
		checkDetail.setCheckTime(new Date());
		checkDetail.setCreason(getAuditReason(status, reason, reasonStr));
		return checkDetailDao.updateIfNecessary(checkDetail);
	}
	
	private int updatePassOrUnpassNum(CheckDetail checkDetail, Integer status) {
		Long parentId = checkDetail.getParentId();
		CheckDetailRecord checkDetailRecord = checkDetailRecordDao.getById(parentId);
		if(Integer.valueOf(TranOpsCheckStatus.ELIGIBLE.getId()).equals(status)) {
			checkDetailRecord.setPassNum(checkDetailRecord.getPassNum() + 1);
		} else if(Integer.valueOf(TranOpsCheckStatus.UNELIGIBLE.getId()).equals(status)) {
			checkDetailRecord.setUnpassNum(checkDetailRecord.getUnpassNum() + 1);
		}
		return checkDetailRecordDao.updateIfNecessary(checkDetailRecord);
	}
	
	private long getCheckDetailUncheckCount(Long parentId) {
		return checkDetailDao.getUncheckCount(parentId);
	}
	
	private long getCheckDetailRecordUncheckCount(Long parentId) {
		return checkDetailRecordDao.getUncheckCount(parentId);
	}
	
	// 抽查原因
	private String getAuditReason(Integer status, Integer reason, String reasonStr) {
		if(Integer.valueOf(TranOpsCheckStatus.ELIGIBLE.getId()).equals(status)) {
			reasonStr = null;
		} else if(Integer.valueOf(TranOpsCheckStatus.UNELIGIBLE.getId()).equals(status)
				&& TranOpsCheckReason.OTHER.getId() != reason) {
			for(TranOpsCheckReason enumReason : TranOpsCheckReason.values()) {
				if(reason == enumReason.getId()) {
					reasonStr = enumReason.getDesc();
					break;
				}
			}
		}
		return reasonStr;
	}
	
	public CheckDetail getCheckDetailById(Long questionId) {
		CheckDetail checkDetail = checkDetailDao.getById(questionId);
		if(checkDetail != null) {
			setSubject(checkDetail);
			setStatus(checkDetail);
			setIsRerecord(checkDetail);
			setCstatus(checkDetail);
			
			String knowledge = checkDetail.getTranOps().getKnowledge();
			if(StringUtils.isNotEmpty(knowledge)) {
				checkDetail.getTranOps().setKnowledgeArray(knowledge.split(","));
			}
		}
		return checkDetail;
	}
	
	public CheckDetail getCheckDetailBygrandParentId(Long grandParentId) {
		CheckDetail checkDetail = checkDetailDao.getCheckDetailBygrandParentId(grandParentId);
		return checkDetail;
	}
	
	
	private void setSubject(CheckDetail audio) {
    	Integer subject = audio.getTranOps().getRealSubject();
		if(subject != null) {
			for(EnumSubject enumsubject : EnumSubject.values()) {
				if(subject.equals(enumsubject.getId())) {
					audio.getTranOps().setSubject(enumsubject.getChineseName());
					break;
				}
			}
		}
	}
    private void setIsRerecord(CheckDetail audio) {
    	Date approveTime = audio.getTranOps().getApproveTime();
    	Date createTime = audio.getTranOps().getCreateTime();
    	if(approveTime != null && createTime != null && createTime.compareTo(approveTime) > 0) {
    		audio.getTranOps().setIsRerecordForShow("是");
    	} else {
    		audio.getTranOps().setIsRerecordForShow("否");
    	}
    }
	private void setStatus(CheckDetail audio) {
    	String status = audio.getTranOps().getAuditStatus();
    	if(status != null) {
    		for(TranOpsAuditStatus audioStatus : TranOpsAuditStatus.values()) {
    			if(status.equals(audioStatus.getId())) {
    				audio.getTranOps().setStatusForShow(audioStatus.getDesc());
    				break;
    			}
    		}
    	}
    }
	private void setCstatus(CheckDetail audio) {
		Integer status = audio.getCstatus();
    	if(status != null) {
    		for(TranOpsCheckStatus checkStatus : TranOpsCheckStatus.values()) {
    			if(status.equals(checkStatus.getId())) {
    				audio.setStatusForShow(checkStatus.getDesc());
    				break;
    			}
    		}
    	}
    }
}
