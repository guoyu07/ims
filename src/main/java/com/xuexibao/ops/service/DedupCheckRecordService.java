package com.xuexibao.ops.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.constant.CheckStatusConstant;
import com.xuexibao.ops.dao.ICheckRecordDao;
import com.xuexibao.ops.model.CheckRecord;

@Service
public class DedupCheckRecordService {
	
	@Resource
	ICheckRecordDao checkRecordDao;

	public List<CheckRecord> searchList(Integer month, Long page, int limit) {
		String month_str = "";
		if(month != null){
			month_str = month < 10 ? ("0" + month):  Integer.toString(month);
		}
		List<CheckRecord> checkRecordList = checkRecordDao.searchList(month_str, page, limit);
		for(CheckRecord checkRecord : checkRecordList) {
			String statusString = checkRecord.getStatus() == 0 ?
					CheckStatusConstant.UNCHECK:CheckStatusConstant.CHECKED;
			checkRecord.setStatusString(statusString);
		}
		
		return checkRecordList;
	}
	
	public long searchCount(Integer month) {
		String month_str = "";
		if(month != null){
			month_str = month < 10 ? ("0" + month):  Integer.toString(month);
		}
		return checkRecordDao.searchCount(month_str);
	}
}
