package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.ICheckDetailRecordDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.CheckDetailRecord;

@Repository
public class CheckDetailRecordDaoImpl extends EntityDaoImpl<CheckDetailRecord> implements ICheckDetailRecordDao {
	
	@Override
	public long searchCount(String month, Integer teamid) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("month", month);
		para.put("teamid", teamid);

		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<CheckDetailRecord> searchList(String month, Integer teamid, Long page, int limit){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("month", month);
		para.put("offset", page);
		para.put("limit", limit);
		para.put("teamid", teamid);
		List<CheckDetailRecord> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	@Override
	public List<CheckDetailRecord> searchList(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		List<CheckDetailRecord> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	@Override
	public long getUncheckCount(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".getUncheckCount", para);
		return count;
	}

}
