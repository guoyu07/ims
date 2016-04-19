package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IDedupGroupCandidatesDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.DedupGroupCandidates;

@Repository
public class DedupGroupCandidatesDaoImpl extends EntityDaoImpl<DedupGroupCandidates> implements IDedupGroupCandidatesDao  {
	
	@Override
	public List<DedupGroupCandidates> searchListByGroupId(Integer groupId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		List<DedupGroupCandidates> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchListByGroupId", para);
		return results;
	}
	
	@Override
	public List<DedupGroupCandidates> searchListByGroupIdExclude(Integer groupId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		List<DedupGroupCandidates> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchListByGroupIdExclude", para);
		return results;
	}
	
	@Override
	public List<DedupGroupCandidates> searchListByGroupIdInclude(Integer groupId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		List<DedupGroupCandidates> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchListByGroupIdInclude", para);
		return results;
	}
	
	@Override
	public DedupGroupCandidates searchListByQuestionId(Integer groupId, Long questionId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		para.put("questionId", questionId);
		DedupGroupCandidates results = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchListByQuestionId", para);
		return results;
	}
	
	@Override
	public List<DedupGroupCandidates> searchDuplicateList(Integer groupId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		List<DedupGroupCandidates> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchDuplicateList", para);
		return results;
	}	
}
