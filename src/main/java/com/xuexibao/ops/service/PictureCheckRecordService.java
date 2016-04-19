package com.xuexibao.ops.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import com.xuexibao.ops.constant.CheckStatusConstant;
import com.xuexibao.ops.dao.impl.PictureCheckRecordDao;
import com.xuexibao.ops.model.PictureCheckRecord;

@Service
public class PictureCheckRecordService {
	
	@Resource
	PictureCheckRecordDao pictureCheckRecordDao;

	public List<PictureCheckRecord> searchList(Integer month, Long page, int limit) {
		String month_str = "";
		if(month != null){
			month_str = month < 10 ? ("0" + month):  Integer.toString(month);
		}
		List<PictureCheckRecord> checkRecordList = pictureCheckRecordDao.searchList(month_str, page, limit);
		for(PictureCheckRecord checkRecord : checkRecordList) {
			String statusString = checkRecord.getStatus() == 0 ? CheckStatusConstant.UNCHECK:CheckStatusConstant.CHECKED;
			checkRecord.setStatusString(statusString);
		}
		
		return checkRecordList;
	}
	
	public long searchCount(Integer month) {
		String month_str = "";
		if(month != null){
			month_str = month < 10 ? ("0" + month):  Integer.toString(month);
		}
		return pictureCheckRecordDao.searchCount(month_str);
	}
}
