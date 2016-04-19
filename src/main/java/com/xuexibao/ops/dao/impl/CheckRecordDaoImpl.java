package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.ICheckRecordDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.CheckRecord;

@Repository
public class CheckRecordDaoImpl extends EntityDaoImpl<CheckRecord> implements ICheckRecordDao{
	
	@Override
	public long searchCount(String month) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("month", month);

		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<CheckRecord> searchList(String month, Long page, int limit){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("month", month);
		para.put("offset", page);
		para.put("limit", limit);
		List<CheckRecord> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}


}
