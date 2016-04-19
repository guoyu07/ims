package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.RecognitionHistory;

public interface IUnRecognitionHistoryDao extends IEntityDao<RecognitionHistory>   {


	long searchCount(String operater, Long pictureId, Date startDate, Date endDate);

	List<RecognitionHistory> searchList(Long pictureId,
			Integer status, Date startDate, Date endDate, Long page, int limit);
}
