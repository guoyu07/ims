package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IRecognitionAnalysisBydateDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.RecognitionAnalysisBydate;
@Repository
public class RecognitionAnalysisBydateDaoImpl extends EntityDaoImpl<RecognitionAnalysisBydate> implements IRecognitionAnalysisBydateDao{
	@Override
	public long searchRecognitionUserCount() {
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchRecognitionUserCount");
		return count;
	}

	@Override
	public List<RecognitionAnalysisBydate> searchCountList(String operator, Date startDate, Date endDate, Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("operator", operator);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<RecognitionAnalysisBydate> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchCountList", para);
		return results;
	}

	@Override
	public List<RecognitionAnalysisBydate> searchCountList(String operator, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operator", operator);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<RecognitionAnalysisBydate> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchCountList", para);
		return results;
	}

}
