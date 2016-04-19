package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.DedupStatisticsInfo;


@Repository
public class DedupStatisticsInfoDao extends EntityDaoImpl<DedupStatisticsInfo> {

	public List<DedupStatisticsInfo> searchByDate(String startDate, String endDate, Long page, Integer limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		if(page != null && limit != null) {
			para.put("offset", page * limit);
			para.put("limit", limit);
		}
		List<DedupStatisticsInfo> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchByDate", para);
		return results;
	}
	
	public Long countByDate(String startDate, String endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		Long results = getSqlSessionTemplate().selectOne(getNameSpace() + ".countByDate", para);
		return results;
	}
	
}
