package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IDedupMarkDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.DedupMark;


@Repository
public class DedupMarkDaoImpl extends EntityDaoImpl<DedupMark> implements IDedupMarkDao  {

	@Override
	public List<DedupMark> getBlockIds() {
		Map<String, Object> para = new HashMap<String, Object>();
		List<DedupMark> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchBlocksList", para);
		return results;
	}
	
	@Override
	public List<DedupMark> searchIds(Integer teamId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamid", teamId);
		List<DedupMark> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchIds", para);
		return results;
	}
	
}
