package com.xuexibao.ops.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.ICheckDetailDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.CheckDetail;


@Repository
public class CheckDetailDaoImpl extends EntityDaoImpl<CheckDetail> implements ICheckDetailDao{

	@Override
	public long searchCount(Long questionId, String teacher, Integer status , Integer teamid , Long parent_id , Long grand_parent_id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("teacher", teacher);
		para.put("teamid", teamid);
		para.put("status", status);
		para.put("parent_id", parent_id);
		para.put("grand_parent_id", grand_parent_id);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public long searchCaptainCount(Long questionId, String teacher, Integer status , Integer teamid , Long parent_id , Long grand_parent_id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("teacher", teacher);
		para.put("teamid", teamid);
		para.put("status", status);
		para.put("parent_id", parent_id);
		para.put("grand_parent_id", grand_parent_id);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCaptainCount", para);
		return count;
	}
	
	@Override
	public long getUncheckCount(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".getUncheckCount", para);
		return count;
	}
	
	@Override
	public CheckDetail getCheckDetailBygrandParentId(Long grandParentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("grandParentId", grandParentId);
		CheckDetail checkDetail= getSqlSessionTemplate().selectOne(getNameSpace() + ".getCheckDetailBygrandParentId", para);
		return checkDetail;
	}
	
	@Override
	public List<CheckDetail> searchList(Long questionId, String teacher, Integer status , Integer teamid , Long parent_id , Long grand_parent_id,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("questionId", questionId);
		para.put("teamid", teamid);
		para.put("teacher", teacher);
		para.put("status", status);
		para.put("parent_id", parent_id);
		para.put("grand_parent_id", grand_parent_id);
		List<CheckDetail> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}

	@Override
	public List<CheckDetail> searchCaptainList(Long questionId, String teacher, Integer status , Integer teamid , Long parent_id , Long grand_parent_id,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("questionId", questionId);
		para.put("teamid", teamid);
		para.put("teacher", teacher);
		para.put("status", status);
		para.put("parent_id", parent_id);
		para.put("grand_parent_id", grand_parent_id);
		List<CheckDetail> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchCaptainList", para);
		return results;
	}
}
