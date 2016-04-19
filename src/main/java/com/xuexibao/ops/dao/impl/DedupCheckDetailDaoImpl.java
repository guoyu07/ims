package com.xuexibao.ops.dao.impl;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IDedupCheckDetailDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.DedupCheckDetail;


@Repository
public class DedupCheckDetailDaoImpl extends EntityDaoImpl<DedupCheckDetail> implements IDedupCheckDetailDao{

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
	public long getUncheckCount(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".getUncheckCount", para);
		return count;
	}
	
	@Override
	public int getPassCountById(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		int count = getSqlSessionTemplate().selectOne(getNameSpace() + ".getPassCountById", para);
		return count;
	}
	
	@Override
	public int getUnPassCountById(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		int count = getSqlSessionTemplate().selectOne(getNameSpace() + ".getUnPassCountById", para);
		return count;
	}
	
	@Override
	public DedupCheckDetail getCheckDetailByParentId(Long parentId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("parentId", parentId);
		DedupCheckDetail DedupCheckDetail= getSqlSessionTemplate().selectOne(getNameSpace() + ".getDedupCheckDetailByParentId", para);
		return DedupCheckDetail;
	}
	
	@Override
	public List<DedupCheckDetail> searchList(Long questionId, String teacher, Integer status , Integer teamid , Long parent_id , Long grand_parent_id,
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
		List<DedupCheckDetail> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}


}
