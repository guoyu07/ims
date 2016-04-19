package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.CheckRecord;

public interface ICheckRecordDao extends IEntityDao<CheckRecord> {
	
	public List<CheckRecord> searchList(String month, Long page, int limit);
	
	public long searchCount(String month);

}
