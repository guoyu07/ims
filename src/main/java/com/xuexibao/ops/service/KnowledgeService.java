package com.xuexibao.ops.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.impl.KnowledgeDao;
import com.xuexibao.ops.model.Knowledge;

@Service
public class KnowledgeService {
	
	@Resource
	KnowledgeDao knowledgeDao;

	public List<Knowledge> getKnowledgeTree(Integer learnPhase, Integer subject, String keyWord) {
		return knowledgeDao.getKnowledgeTree(learnPhase, subject, keyWord);
	}
	
}