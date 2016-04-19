package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IRecognitionAnalysisDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.RecognitionAnalysis;

@Repository
public class RecognitionAnalysisDaoImpl extends EntityDaoImpl<RecognitionAnalysis> implements IRecognitionAnalysisDao  {

	@Override
	public long searchCount(String operater, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operater", operater);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<RecognitionAnalysis> searchList(String operater, Date startDate, Date endDate, Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("operater", operater);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<RecognitionAnalysis> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}

	@Override
	public RecognitionAnalysis getByOperatorName(String operator) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operator", operator);
		RecognitionAnalysis result = getSqlSessionTemplate().selectOne(getNameSpace() + ".getByOperatorName", para);
		return result;
	}
}
