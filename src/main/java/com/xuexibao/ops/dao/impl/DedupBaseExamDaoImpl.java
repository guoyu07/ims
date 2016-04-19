package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IDedupBaseExamDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.DedupBaseExam;

@Repository
public class DedupBaseExamDaoImpl extends EntityDaoImpl<DedupBaseExam> implements IDedupBaseExamDao  {
	
	@Override
	public List<DedupBaseExam> searchList(int limit, Integer status,Integer phase){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("limit", limit);
		para.put("status", status);
		para.put("phase", phase);
		List<DedupBaseExam> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;		
	}
	
	@Override
	public DedupBaseExam selectByKey(Integer groupId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		DedupBaseExam results = getSqlSessionTemplate().selectOne(getNameSpace() + ".selectByPrimaryKey", para);
		return results;		
	}
	
	@Override
	public void updateByGroupId(Integer groupId, String userKey,Integer status, Integer phase, Integer round,Integer tinyPhase){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("groupId", groupId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("phase", phase);
		para.put("round", round);
		para.put("tinyPhase", tinyPhase);
		getSqlSessionTemplate().update(getNameSpace() + ".updateByGroupId", para);
	}
	
	@Override
	public void updateByProblemData(Integer phase){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("phase", phase);
		getSqlSessionTemplate().update(getNameSpace() + ".updateByProblemData", para);
	}
}


