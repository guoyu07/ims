package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.DedupGroupCandidates;

public interface IDedupGroupCandidatesDao extends IEntityDao<DedupGroupCandidates>{
	List<DedupGroupCandidates> searchListByGroupId(Integer groupId);
	
	List<DedupGroupCandidates> searchListByGroupIdExclude(Integer groupId);
	
	List<DedupGroupCandidates> searchListByGroupIdInclude(Integer groupId);
	
	DedupGroupCandidates searchListByQuestionId(Integer groupId, Long questionId);
	
	List<DedupGroupCandidates> searchDuplicateList(Integer groupId);
}
