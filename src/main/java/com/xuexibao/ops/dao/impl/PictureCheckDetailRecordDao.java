package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.PictureCheckDetailRecord;

@Repository
public class PictureCheckDetailRecordDao extends EntityDaoImpl<PictureCheckDetailRecord> {

	public long searchCount(String month, Integer teamid) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("month", month);
		para.put("teamid", teamid);

		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	public List<PictureCheckDetailRecord> searchList(String month, Integer teamid, Long page, int limit){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("month", month);
		para.put("offset", page);
		para.put("limit", limit);
		para.put("teamid", teamid);
		List<PictureCheckDetailRecord> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	public List<PictureCheckDetailRecord> searchList(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		List<PictureCheckDetailRecord> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	public long getUncheckCount(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".getUncheckCount", para);
		return count;
	}

}
