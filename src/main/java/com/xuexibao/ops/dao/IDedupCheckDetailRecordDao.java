package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.DedupCheckDetailRecord;
import com.xuexibao.ops.model.DedupCheckList;

public interface IDedupCheckDetailRecordDao extends IEntityDao<DedupCheckDetailRecord> {
	public List<DedupCheckList> searchCheckList(Integer status, String userKey , String name,  Long page, int limit);
	
	public List<DedupCheckDetailRecord> searchList(Long parentId);
	
	public long searchCount(Integer status, String userKey ,String name);

	long getUncheckCount(Long parentId);
}
