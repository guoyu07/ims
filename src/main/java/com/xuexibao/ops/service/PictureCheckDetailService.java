package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.dao.impl.PictureCheckDetailDao;
import com.xuexibao.ops.dao.impl.PictureCheckDetailRecordDao;
import com.xuexibao.ops.dao.impl.PictureCheckRecordDao;
import com.xuexibao.ops.enumeration.TranOpsCheckStatus;
import com.xuexibao.ops.enumeration.TranOpsParentCheckStatus;
import com.xuexibao.ops.model.PictureCheckDetail;
import com.xuexibao.ops.model.PictureCheckDetailRecord;
import com.xuexibao.ops.model.PictureCheckRecord;

@Service
public class PictureCheckDetailService {
	
	@Resource
	PictureCheckRecordDao pictureCheckRecordDao;
	@Resource
	PictureCheckDetailDao pictureCheckDetailDao;
	@Resource
	PictureCheckDetailRecordDao pictureCheckDetailRecordDao;

	public long searchCount(Long questionId,  String teacher, Integer cstatus, Integer teamid , Long parent_id , Long grand_parent_id) {
		return pictureCheckDetailDao.searchCount(questionId,  teacher,	cstatus , teamid , parent_id , grand_parent_id);
	}
	
	public List<PictureCheckDetail> searchList(Long questionId, String teacher, Integer cstatus , Integer teamid, Long parent_id , Long grand_parent_id, 
			Long page, int limit) {
		List<PictureCheckDetail> tranOpsDetailList = pictureCheckDetailDao.searchList(questionId, teacher, cstatus, teamid, parent_id, grand_parent_id,
				page, limit);
		for(PictureCheckDetail audio : tranOpsDetailList) {
			
			setCstatus(audio);
			
		}
		return tranOpsDetailList;
	}
	
	
	public long searchCaptainCount(Long questionId,  String teacher, Integer cstatus, Integer teamid , Long parent_id , Long grand_parent_id) {
		return pictureCheckDetailDao.searchCaptainCount(questionId,  teacher,	cstatus , teamid , parent_id , grand_parent_id);
	}
	
	public List<PictureCheckDetail> searchCaptainList(Long questionId, String teacher, Integer cstatus , Integer teamid, Long parent_id , Long grand_parent_id,
			Long page, int limit) {
		List<PictureCheckDetail> tranOpsDetailList = pictureCheckDetailDao.searchCaptainList(questionId, teacher, cstatus, teamid, parent_id, grand_parent_id,
				page, limit);
		for(PictureCheckDetail audio : tranOpsDetailList) {
			
			setCstatus(audio);
			
		}
		return tranOpsDetailList;
	}
	
	public void auditCheckTranOps(Long[] audioIdArray, String approvor, Integer status) {
		for(Long audioId : audioIdArray) {
			auditCheckTranOps(audioId, approvor, status);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	private boolean auditCheckTranOps(Long id, String approvor, Integer status) {
		// 通过id查询tranOps，不存在则退出此次循环
		PictureCheckDetail checkDetail = pictureCheckDetailDao.getById(id);
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
		updateCheckDetail(checkDetail, approvor, status);
		
		// 更新checkDetailRecord的冗余字段合格数或未合格数
		updatePassOrUnpassNum(checkDetail, status);
		
		updateCheckDetailRecordStatus(checkDetail);
		
		return true;
	}
	
	private void updateCheckDetailRecordStatus(PictureCheckDetail checkDetail) {
		long uncheckCount = getCheckDetailUncheckCount(checkDetail.getParentId());
		if(uncheckCount == 0) {
			PictureCheckDetailRecord checkDetailRecord = pictureCheckDetailRecordDao.getById(checkDetail.getParentId());
			checkDetailRecord.setStatus(TranOpsParentCheckStatus.CHECK.getId());
			pictureCheckDetailRecordDao.updateIfNecessary(checkDetailRecord);
			
			updateCheckRecordStatus(checkDetailRecord.getParentId());
		}
	}
	
	private void updateCheckRecordStatus(Long parentId) {
		long uncheckCount = getCheckDetailRecordUncheckCount(parentId);
		if(uncheckCount == 0) {
			PictureCheckRecord checkRecord = pictureCheckRecordDao.getById(parentId);
			checkRecord.setStatus(TranOpsParentCheckStatus.CHECK.getId());
			pictureCheckRecordDao.updateIfNecessary(checkRecord);
			
		}
	}
	
	private int updateCheckDetail(PictureCheckDetail checkDetail, String approvor, Integer status) {
		checkDetail.setCstatus(status);
		checkDetail.setChecker(approvor);
		checkDetail.setCheckTime(new Date());
		return pictureCheckDetailDao.updateIfNecessary(checkDetail);
	}
	
	private int updatePassOrUnpassNum(PictureCheckDetail checkDetail, Integer status) {
		Long parentId = checkDetail.getParentId();
		PictureCheckDetailRecord checkDetailRecord = pictureCheckDetailRecordDao.getById(parentId);
		if(Integer.valueOf(TranOpsCheckStatus.ELIGIBLE.getId()).equals(status)) {
			checkDetailRecord.setPassNum(checkDetailRecord.getPassNum() + 1);
		} else if(Integer.valueOf(TranOpsCheckStatus.UNELIGIBLE.getId()).equals(status)) {
			checkDetailRecord.setUnpassNum(checkDetailRecord.getUnpassNum() + 1);
		}
		return pictureCheckDetailRecordDao.updateIfNecessary(checkDetailRecord);
	}
	
	private long getCheckDetailUncheckCount(Long parentId) {
		return pictureCheckDetailDao.getUncheckCount(parentId);
	}
	
	private long getCheckDetailRecordUncheckCount(Long parentId) {
		return pictureCheckDetailRecordDao.getUncheckCount(parentId);
	}
	
	public PictureCheckDetail getCheckDetailById(Long questionId) {
		PictureCheckDetail checkDetail = pictureCheckDetailDao.getById(questionId);
		if(checkDetail != null) {
			setCstatus(checkDetail);
		}
		return checkDetail;
	}
	
	public PictureCheckDetail getCheckDetailBygrandParentId(Long grandParentId) {
		PictureCheckDetail checkDetail = pictureCheckDetailDao.getCheckDetailBygrandParentId(grandParentId);
		return checkDetail;
	}
	
	
	private void setCstatus(PictureCheckDetail audio) {
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
