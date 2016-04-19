package com.xuexibao.ops.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.DedupMark;


@Repository
public class DedupMarkDao extends EntityDaoImpl<DedupMark> {

	public DedupMark selectOne(String userKey, Integer status, String baseId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operator", userKey);
		para.put("status", status);
		para.put("baseId", baseId);
		DedupMark results = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchOne", para);
		return results;		
	}
	
	public void updateDedupMarkStatus(String userKey, String baseId, Integer status, Integer block, String result){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operator", userKey);
		para.put("baseId", baseId);
		para.put("status", status);
		para.put("block", block);
		para.put("result", result);
		getSqlSessionTemplate().update(getNameSpace() + ".updateStatus", para);
	}
	
	public void updateDedupMarkFinished(Integer block){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("block", block);
		getSqlSessionTemplate().update(getNameSpace() + ".updateFinishedByBlock", para);
	}
	
	public void updateDedupMarkChecked(Integer block){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("block", block);
		getSqlSessionTemplate().update(getNameSpace() + ".updateCheckedByBlock", para);
	}
	
	public List<DedupMark> searchBlockList(Integer block){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("block", block);		
		List<DedupMark> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchblocklist", para);
		return results;		
	}
	
	public Integer getBiggestBlockId(){
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".searchBiggestBlockId");
	}
	
	public Integer minBlockNotAssigned() {
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".minBlockNotAssigned");
	}
	
	public int assignNewBlock(String operator, Integer block) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operator", operator);
		para.put("block", block);
		return getSqlSessionTemplate().update(getNameSpace() + ".assignNewBlock", para);
	}
	
	public int getUnfinishedCount(String operator) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operator", operator);
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".getUnfinishedCount", para);

	}
}
