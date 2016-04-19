package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.RecognitionAnalysis;

public interface IRecognitionAnalysisDao extends IEntityDao<RecognitionAnalysis>   {


	long searchCount(String operater, Date startDate, Date endDate);

	List<RecognitionAnalysis> searchList(String operater, Date startDate, Date endDate, Long page, int limit);

	RecognitionAnalysis getByOperatorName(String operater);
}
