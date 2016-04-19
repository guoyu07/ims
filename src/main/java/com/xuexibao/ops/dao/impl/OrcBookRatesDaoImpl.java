package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IOrcBookRatesDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.OrcBookRates;


@Repository
public class OrcBookRatesDaoImpl extends EntityDaoImpl<OrcBookRates> implements IOrcBookRatesDao {
	
	@Override
	public long searchCount(String name, String isbn,
			String operator, String teamName, Integer teamId, Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("name", name);
		para.put("isbn", isbn);
		para.put("operatorName", operator);
		para.put("teamName", teamName);
		para.put("teamId", teamId);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<OrcBookRates> searchList(String name, String isbn,
			String operator, String teamName, Integer teamId, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("name", name);
		para.put("isbn", isbn);
		para.put("operatorName", operator);
		para.put("teamName", teamName);
		para.put("teamId", teamId);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcBookRates> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);		
		return results;
	}
	@Override
	public OrcBookRates getById(Long id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", id);
		OrcBookRates orcBookRates = getSqlSessionTemplate().selectOne(getNameSpace() + ".getById", para);		
		return orcBookRates;
	}
	@Override
	public OrcBookRates getByBookId(Long bookid) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("bookid", bookid);
		OrcBookRates orcBookRates = getSqlSessionTemplate().selectOne(getNameSpace() + ".getByBookId", para);		
		return orcBookRates;
	}
}
