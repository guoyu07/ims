package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.CheckDetail;


public interface ICheckDetailDao extends IEntityDao<CheckDetail> {

	long searchCount(Long questionId, String teacher,
			Integer status , Integer teamid , Long parent_id , Long grand_parent_id);
	List<CheckDetail> searchList(Long questionId, String teacher,
			Integer status , Integer teamid, Long parent_id , Long grand_parent_id,  Long page, int limit);
	
	long searchCaptainCount(Long questionId, String teacher,
			Integer status , Integer teamid , Long parent_id , Long grand_parent_id);
	List<CheckDetail> searchCaptainList(Long questionId, String teacher,
			Integer status , Integer teamid, Long parent_id , Long grand_parent_id,  Long page, int limit);
	
	long getUncheckCount(Long parentId);
	
	CheckDetail getCheckDetailBygrandParentId(Long grandParentId);
}
