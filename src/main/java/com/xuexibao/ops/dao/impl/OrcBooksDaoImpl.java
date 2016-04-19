package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IOrcBooksDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.OrcBooks;


@Repository
public class OrcBooksDaoImpl extends EntityDaoImpl<OrcBooks> implements IOrcBooksDao {
	
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
	public List<OrcBooks> searchList(String name, String isbn,
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
		List<OrcBooks> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);		
		return results;
	}
	@Override
	public List<OrcBooks> searchList(String name, String isbn,
			String operator, String teamName, Integer teamId, Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", null);
		para.put("limit", null);
		para.put("name", name);
		para.put("isbn", isbn);
		para.put("operatorName", operator);
		para.put("teamName", teamName);
		para.put("teamId", teamId);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcBooks> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);		
		return results;
	}
	public int updateStatus(Long id, Integer status, String operatorName){
		//重置密码
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", id);
		para.put("status", status);
		para.put("updateTime", new Date());
		para.put("operatorEndtime", new Date());
		para.put("operator", operatorName);
		int count = getSqlSessionTemplate().update(getNameSpace() + ".updateStatus", para);	
		return count;
	}
	@Override
	public OrcBooks getById(Long Id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("Id", Id);
		OrcBooks orcBooks = getSqlSessionTemplate().selectOne(getNameSpace() + ".getById", para);		
		return orcBooks;
	}
	@Override
	public OrcBooks getByBookId(Long bookId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("bookId", bookId);
		OrcBooks orcBooks = getSqlSessionTemplate().selectOne(getNameSpace() + ".getByBookId", para);		
		return orcBooks;
	}

}
