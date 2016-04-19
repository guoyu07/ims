package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.Knowledge;

@Repository
public class KnowledgeDao extends EntityDaoImpl<Knowledge> {

	public List<Knowledge> getKnowledgeTree(Integer learnPhase, Integer subject, String keyWord) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("learnPhase", learnPhase);
		para.put("subject", subject);
		para.put("keyWord", keyWord);
		List<Knowledge> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getKnowledgeTree", para);
		return results;
	};
	
}

