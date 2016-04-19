package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IQuestionDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.Question;


@Repository
public class QuestionDaoImpl extends EntityDaoImpl<Question> implements IQuestionDao {
	
	
	@Override
	public Question getById(Long Id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("Id", Id);
		Question question = getSqlSessionTemplate().selectOne(getNameSpace() + ".getById", para);		
		return question;
	}
	@Override
	public Question getByRealId(Long RealId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("RealId", RealId);
		Question question = getSqlSessionTemplate().selectOne(getNameSpace() + ".getByRealId", para);		
		return question;
	}
	
	@Override
	public Question addQuestionInfo(Question question){
		//插入
		Question questionInfo=insertSelective(question);
		return questionInfo;
	}
}
