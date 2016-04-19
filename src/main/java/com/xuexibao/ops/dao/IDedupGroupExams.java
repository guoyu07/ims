package com.xuexibao.ops.dao;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.DedupGroupExams;

public interface IDedupGroupExams extends IEntityDao<DedupGroupExams>{

	Long searchListByQuestionId(long questionId);
	
	Integer searchListByGroupId(Integer groupId);
	
	void updateByQuestionId(Integer groupId, long questionId);
}
