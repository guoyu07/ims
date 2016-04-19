package com.xuexibao.ops.service;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.IDedupCheckDetailRecordDao;
import com.xuexibao.ops.model.DedupCheckDetailRecord;
import com.xuexibao.ops.model.DedupCheckList;

@Service
public class DedupCheckDetailRecordService {
	
	@Resource
	IDedupCheckDetailRecordDao dedupCheckDetailRecordDao;


	public List<DedupCheckList> searchList(Integer status, String userKey ,String name, Long page, int limit) {

		List<DedupCheckList> checkDetailRecordList = dedupCheckDetailRecordDao.searchCheckList(status,userKey,name, page, limit);
		for(DedupCheckList record : checkDetailRecordList){
			DecimalFormat df = new DecimalFormat("0.00%");
			if(record.getNum() == 0) {
				record.setRatioStr("0.00%");
			} else {
				double ratio = (double)record.getPassNum() / (double)record.getNum();
				String ratioStr = df.format(ratio);
				record.setRatioStr(ratioStr);
				if(record.getStatus() == 0){
					record.setStatusStr("待检查");
				}else if(record.getStatus() == 1){
					record.setStatusStr("已检查");
				}else if(record.getStatus() == 2){
					record.setStatusStr("已回滚");
				}
			}
		}
		return checkDetailRecordList;
	}
	
	public List<DedupCheckDetailRecord> searchList(Long parentId) {
		List<DedupCheckDetailRecord> checkDetailRecordList = dedupCheckDetailRecordDao.searchList(parentId);
		for(DedupCheckDetailRecord record : checkDetailRecordList){
			DecimalFormat df = new DecimalFormat("0.00%");
			if(record.getNum() == 0) {
				record.setRatioStr("0.00%");
			} else {
				double ratio = (double)record.getPassNum() / (double)record.getNum();
				String ratioStr = df.format(ratio);
				record.setRatioStr(ratioStr);
			}
		}
		return checkDetailRecordList;
	}
	
	public long searchCount(Integer status, String userKey ,String name) {
		
		return dedupCheckDetailRecordDao.searchCount(status,userKey,name);
	}

}
