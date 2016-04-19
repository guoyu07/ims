package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IDedupCheckDetailRecordDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.DedupCheckDetailRecord;
import com.xuexibao.ops.model.DedupCheckList;

@Repository
public class DedupCheckDetailRecordDaoImpl extends EntityDaoImpl<DedupCheckDetailRecord> implements IDedupCheckDetailRecordDao {
	
	@Override
	public long searchCount(Integer status, String userKey ,String name) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("status", status);
		para.put("userKey", userKey);
		para.put("name", name);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCheckCount", para);
		return count;
	}
	
	@Override
	public List<DedupCheckList> searchCheckList(Integer status, String userKey ,String name, Long page, int limit){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("status", status);
		para.put("operator", userKey);
		para.put("name", name);
		para.put("offset", page);
		para.put("limit", limit);
		List<DedupCheckList> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchCheckList", para);
		return results;
	}
	
	@Override
	public List<DedupCheckDetailRecord> searchList(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		List<DedupCheckDetailRecord> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	@Override
	public long getUncheckCount(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".getUncheckCount", para);
		return count;
	}

	public void updateStatus(Integer block, Integer status) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", block);
		para.put("status", status);
		getSqlSessionTemplate().update(getNameSpace() + ".updatestatus", para);
	}	
}
