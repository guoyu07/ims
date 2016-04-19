package com.xuexibao.ops.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.IQuestionKnowledgeDao;
import com.xuexibao.ops.model.QuestionKnowledge;

@Service
public class QuestionKnowledgeService {
	
	@Resource
	IQuestionKnowledgeDao questionKnowledgeDao;
	public void saveQuesKnow(QuestionKnowledge[] quesKnow){
		questionKnowledgeDao.delByTransId(quesKnow[0].getTranOpsId());
		questionKnowledgeDao.insertSelective(quesKnow);
	}
}