package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.CheckDetailRecord;

public interface ICheckDetailRecordDao extends IEntityDao<CheckDetailRecord> {
	public List<CheckDetailRecord> searchList(String month, Integer teamid,  Long page, int limit);
	
	public List<CheckDetailRecord> searchList(Long parentId);
	
	public long searchCount(String month,  Integer teamid);

	long getUncheckCount(Long parentId);
}
