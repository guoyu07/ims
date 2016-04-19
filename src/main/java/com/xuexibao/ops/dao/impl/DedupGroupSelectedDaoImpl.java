package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IDedupGroupSelectedDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.dto.NodupClickCntDto;
import com.xuexibao.ops.dto.ValidQstnLenDto;
import com.xuexibao.ops.model.DedupGroupSelected;
import com.xuexibao.ops.model.DedupStatisticsInfo;

@Repository
public class DedupGroupSelectedDaoImpl extends EntityDaoImpl<DedupGroupSelected> implements IDedupGroupSelectedDao  {
	
	@Override
	public List<DedupGroupSelected> searchByNecessary(String questionIds, Integer groupId, Integer round, Integer tinyPhase, String userKey, String dateStr){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionIds", questionIds);
		para.put("groupId", groupId);
		para.put("round", round);
		para.put("tinyPhase", tinyPhase);
		para.put("userKey", userKey);
		para.put("dateStr", dateStr);
		List<DedupGroupSelected> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchByNecessary", para);
		return results;
	}
	
	@Override
	public void updateByRound(Integer groupId, Integer round, Integer validQstnLen, String finishDateStr){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		para.put("round", round);
		para.put("validQstnLen", validQstnLen);
		para.put("finishDateStr", finishDateStr);
		getSqlSessionTemplate().update(getNameSpace() + ".updatePerRound", para);
	}	
	public List<DedupStatisticsInfo> getDedupStatistics(String dateStr) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("dateStr", dateStr);
		List<DedupStatisticsInfo> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getDedupStatistics", para);
		return results;
	}
	
	@Override
	public List<NodupClickCntDto> getNodupClickCnt(String dateStr) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("dateStr", dateStr);
		List<NodupClickCntDto> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getNodupClickCnt", para);
		return results;
	}
	
	@Override
	public List<ValidQstnLenDto> getValidQstnLen(String dateStr) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("dateStr", dateStr);
		List<ValidQstnLenDto> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getValidQstnLen", para);
		return results;
	}
}
