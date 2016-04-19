package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IDedupGroupExams;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.DedupGroupExams;

@Repository
public class DedupGroupExamsDaoImpl extends EntityDaoImpl<DedupGroupExams> implements IDedupGroupExams  {

	public Long searchListByQuestionId(long questionId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		Long result = getSqlSessionTemplate().selectOne(getNameSpace() + ".selectByQuestionId", para);
		return result;
	}

	@Override
	public Integer searchListByGroupId(Integer groupId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		Integer result = getSqlSessionTemplate().selectOne(getNameSpace() + ".selectByGroupId", para);
		return result;
	}
	
	@Override
	public void updateByQuestionId(Integer groupId, long questionId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		para.put("groupId", groupId);
		getSqlSessionTemplate().update(getNameSpace() + ".updateByQuestionId", para);
	}	
}
