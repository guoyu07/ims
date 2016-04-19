package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.DedupCheckDetail;


public interface IDedupCheckDetailDao extends IEntityDao<DedupCheckDetail> {

	long searchCount(Long questionId, String teacher,
			Integer status , Integer teamid , Long parent_id , Long grand_parent_id);
	List<DedupCheckDetail> searchList(Long questionId, String teacher,
			Integer status , Integer teamid, Long parent_id , Long grand_parent_id,  Long page, int limit);
	
	
	long getUncheckCount(Long parentId);
	
	int getPassCountById(Long parentId);
	int getUnPassCountById(Long parentId);
	
	DedupCheckDetail getCheckDetailByParentId(Long parentId);
}
