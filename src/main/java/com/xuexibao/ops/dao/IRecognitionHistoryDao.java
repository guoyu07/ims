package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.RecognitionHistory;

public interface IRecognitionHistoryDao extends IEntityDao<RecognitionHistory>   {


	long searchCount(String operator, Long pictureId, Date startDate, Date endDate);
	
	List<RecognitionHistory> searchList(String operater, Long pictureId,
			Date startDate, Date endDate, Long page, int limit);

}
