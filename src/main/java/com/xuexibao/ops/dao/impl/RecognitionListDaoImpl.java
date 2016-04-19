package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IRecognitionListDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.RecognitionHistory;

@Repository
public class RecognitionListDaoImpl extends EntityDaoImpl<RecognitionHistory> implements IRecognitionListDao  {

	@Override
	public long searchCount(String operater, Long pictureId, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operater", operater);
		para.put("pictureId", pictureId);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<RecognitionHistory> searchList(String operater, Long pictureId, Date startDate, Date endDate, Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("operater", operater);
		para.put("pictureId", pictureId);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<RecognitionHistory> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
}
