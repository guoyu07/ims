package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IBooksDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.Books;


@Repository
public class BooksDaoImpl extends EntityDaoImpl<Books> implements IBooksDao {
	
	@Override
	public long searchCount(String name, String isbn,
			String sourceName, Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("name", name);
		para.put("isbn", isbn);
		para.put("source_id", sourceName);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<Books> searchList(String name, String isbn,
			String sourceName, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("name", name);
		para.put("isbn", isbn);
		para.put("source_id", sourceName);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<Books> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);		
		return results;
	}
	@Override
	public List<Books> searchList(String nameisbn) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("nameisbn", nameisbn);
		List<Books> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getListbyNameOrIsbn", para);		
		return results;
	}
	
	@Override
	public Books getById(Long Id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("Id", Id);
		Books book = getSqlSessionTemplate().selectOne(getNameSpace() + ".getBookById", para);		
		return book;
	}
	@Override
	public Books getByName(String name) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("name", name);
		Books book = getSqlSessionTemplate().selectOne(getNameSpace() + ".getBookByName", para);		
		return book;
	}
	@Override
	public Long getIdByName(String name){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("name", name);
		Long id = getSqlSessionTemplate().selectOne(getNameSpace() + ".getIdByName", para);
		return id;

	}
	
	@Override
	public Books addBooksInfo(Books book){
		//插入
		Books bookInfo=insertSelective(book);
		return bookInfo;
	}
	
	@Override
	public int updateBookInfoById(Books book) {
		//修改书籍来源
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", book.getId());
		book.getId();
		para.put("name", book.getName());
		para.put("subject", book.getSubject());
		para.put("grade", book.getGrade());
		para.put("publishingHouse", book.getPublishingHouse());
		para.put("sourceId",book.getSourceId());
		para.put("status", book.getStatus());
		para.put("createTime", book.getCreateTime());
		para.put("updateTime", new Date());
		para.put("operator", book.getOperator());
		int count = getSqlSessionTemplate().update(getNameSpace() + ".updateBookInfoById", para);	
		return count;
	}
	@Override
	public Integer getBestByTranId(Long tranId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("tranId", tranId);
		return getSqlSessionTemplate().selectOne(getNameSpace() + ".getBestByTranId", para);	
	}
}
