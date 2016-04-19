package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IQuestionKnowledgeDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.QuestionKnowledge;

@Repository
public class QuestionKnowledgeDaoImpl extends EntityDaoImpl<QuestionKnowledge> implements IQuestionKnowledgeDao  {

	@Override
	public void delByTransId(Long tranOpsId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("tranOpsId", tranOpsId);
		getSqlSessionTemplate().delete(getNameSpace()+".delByTranOpsId", para);
	}


}
