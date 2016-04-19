package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.dao.IDedupCheckDetailDao;
import com.xuexibao.ops.dao.IDedupCheckDetailRecordDao;
import com.xuexibao.ops.dao.IDedupCheckRecordDao;
import com.xuexibao.ops.enumeration.DedupCheckStatus;
import com.xuexibao.ops.enumeration.TranOpsParentCheckStatus;
import com.xuexibao.ops.model.DedupCheckDetail;
import com.xuexibao.ops.model.DedupCheckDetailRecord;
import com.xuexibao.ops.model.DedupCheckRecord;

@Service
public class DedupCheckDetailService {
	
	@Resource
	IDedupCheckRecordDao dedupCheckRecordDao;

	@Resource
	IDedupCheckDetailDao dedupCheckDetailDao;
	@Resource
	IDedupCheckDetailRecordDao dedupCheckDetailRecordDao;

	public long searchCount(Long questionId,  String teacher, Integer cstatus, Integer teamid , Long parent_id , Long grand_parent_id) {
		return dedupCheckDetailDao.searchCount(questionId,  teacher,	cstatus , teamid , parent_id , grand_parent_id);
	}
	
	public List<DedupCheckDetail> searchList(Long questionId, String teacher, Integer cstatus , Integer teamid, Long parent_id , Long grand_parent_id, 
			Long page, int limit) {
		List<DedupCheckDetail> tranOpsDetailList = dedupCheckDetailDao.searchList(questionId, teacher, cstatus, teamid, parent_id, grand_parent_id,
				page, limit);
		for(DedupCheckDetail audio : tranOpsDetailList) {
			
			setCstatus(audio);
			
		}
		return tranOpsDetailList;
	}
	
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public synchronized boolean auditCheckTranOps(Long id, String approvor, Integer status) {
		// 通过id查询tranOps，不存在则退出此次循环
		DedupCheckDetail checkDetail = dedupCheckDetailDao.getById(id);
		if(checkDetail == null) {
			return false;
		}
		// status必须为正确或错误
		if(!Integer.valueOf(DedupCheckStatus.ELIGIBLE.getId()).equals(status) && !Integer.valueOf(DedupCheckStatus.UNELIGIBLE.getId()).equals(status)) {
			return false;
		}
		// 非待检查的不可以再次抽检
		if(!Integer.valueOf(DedupCheckStatus.UCHECK.getId()).equals(checkDetail.getCstatus())) {
			return false;
		}
		
		// 更新checkDetail
		updateCheckDetail(checkDetail, approvor, status);
		
		// 更新checkDetailRecord的冗余字段合格数或未合格数
		updatePassOrUnpassNum(checkDetail, status);
		
		updateCheckDetailRecordStatus(checkDetail);
		
		return true;
	}
	
	private void updateCheckDetailRecordStatus(DedupCheckDetail checkDetail) {
		long uncheckCount = getCheckDetailUncheckCount(checkDetail.getParentId());
		if(uncheckCount == 0) {
			DedupCheckDetailRecord checkDetailRecord = dedupCheckDetailRecordDao.getById(checkDetail.getParentId());
			checkDetailRecord.setStatus(TranOpsParentCheckStatus.CHECK.getId());
			dedupCheckDetailRecordDao.updateIfNecessary(checkDetailRecord);
			
			updateCheckRecordStatus(checkDetailRecord.getParentId());
		}
	}
	
	private void updateCheckRecordStatus(Long parentId) {
		long uncheckCount = getCheckDetailRecordUncheckCount(parentId);
		if(uncheckCount == 0) {
			DedupCheckRecord checkRecord = dedupCheckRecordDao.getById(parentId);
			checkRecord.setStatus(TranOpsParentCheckStatus.CHECK.getId());
			dedupCheckRecordDao.updateIfNecessary(checkRecord);
			
		}
	}
	
	private int updateCheckDetail(DedupCheckDetail checkDetail, String approvor, Integer status) {
		checkDetail.setCstatus(status);
		checkDetail.setChecker(approvor);
		checkDetail.setCheckTime(new Date());
		return dedupCheckDetailDao.updateIfNecessary(checkDetail);
	}
	
	private int updatePassOrUnpassNum(DedupCheckDetail checkDetail, Integer status) {
		Long parentId = checkDetail.getParentId();
		DedupCheckDetailRecord  checkDetailRecord= dedupCheckDetailRecordDao.getById(parentId);
		
		if(Integer.valueOf(DedupCheckStatus.ELIGIBLE.getId()).equals(status)) {
			int passCount = dedupCheckDetailDao.getPassCountById(checkDetailRecord.getId());
			checkDetailRecord.setPassNum(passCount);
		} else if(Integer.valueOf(DedupCheckStatus.UNELIGIBLE.getId()).equals(status)) {
			int unpassCount = dedupCheckDetailDao.getUnPassCountById(checkDetailRecord.getId());
			
			checkDetailRecord.setUnpassNum(unpassCount);
		}
		return dedupCheckDetailRecordDao.updateIfNecessary(checkDetailRecord);
	}
	
	private long getCheckDetailUncheckCount(Long parentId) {
		return dedupCheckDetailDao.getUncheckCount(parentId);
	}
	
	private long getCheckDetailRecordUncheckCount(Long parentId) {
		return dedupCheckDetailRecordDao.getUncheckCount(parentId);
	}
	

	public DedupCheckDetail getCheckDetailById(Long questionId) {
		DedupCheckDetail checkDetail = dedupCheckDetailDao.getById(questionId);

		return checkDetail;
	}
	
	public DedupCheckDetail getCheckDetailByParentId(Long parentId) {
		DedupCheckDetail checkDetail = dedupCheckDetailDao.getCheckDetailByParentId(parentId);
		return checkDetail;
	}
	
	

	private void setCstatus(DedupCheckDetail audio) {
		Integer status = audio.getCstatus();
    	if(status != null) {
    		for(DedupCheckStatus checkStatus : DedupCheckStatus.values()) {
    			if(status.equals(checkStatus.getId())) {
    				audio.setStatusForShow(checkStatus.getDesc());
    				break;
    			}
    		}
    	}
    }
}
