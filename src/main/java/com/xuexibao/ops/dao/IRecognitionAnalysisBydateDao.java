package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.RecognitionAnalysisBydate;

public interface IRecognitionAnalysisBydateDao extends IEntityDao<RecognitionAnalysisBydate> {
	
	long searchRecognitionUserCount();
	
	List<RecognitionAnalysisBydate> searchCountList(String operator, Date startDate,Date endDate, Long page,int limit);

	List<RecognitionAnalysisBydate> searchCountList(String operator, Date startDate,Date endDate);
}
