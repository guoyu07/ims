package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.DedupBaseExam;

public interface IDedupBaseExamDao extends IEntityDao<DedupBaseExam>{
	public List<DedupBaseExam> searchList(int limit,Integer status, Integer phase);
	
	public DedupBaseExam selectByKey(Integer groupId);
	
	public void updateByGroupId(Integer groupId, String user_key, Integer status,Integer phase, Integer round,Integer tinyPhase);
	
	public void updateByProblemData(Integer phase);
}
