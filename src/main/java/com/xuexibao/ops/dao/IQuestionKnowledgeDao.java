package com.xuexibao.ops.dao;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.QuestionKnowledge;

public interface IQuestionKnowledgeDao extends IEntityDao<QuestionKnowledge> {
	public void delByTransId(Long tranOpsId);
}
