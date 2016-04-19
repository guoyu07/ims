package com.xuexibao.ops.dao;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.Question;

public interface IQuestionDao extends IEntityDao<Question>{
	
	public Question getById(Long Id);
	public Question getByRealId(Long RealId);
	public Question addQuestionInfo(Question questionInfo);
	
}
