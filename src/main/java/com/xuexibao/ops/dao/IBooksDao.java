package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.Books;



public interface IBooksDao extends IEntityDao<Books>{

	long searchCount(String name, String isbn, String sourceName, 
			Integer status, Date startDate, Date endDate);

	List<Books> searchList(String name, String isbn,
			String sourceName, Integer status, Date startDate, Date endDate,
			Long page, int limit);
	List<Books> searchList(String nameisbn);
	
	public Books getById(Long Id);
	public Long getIdByName(String name);
	public Books getByName(String name);
	public Books addBooksInfo(Books booksInfo);
	
	public int updateBookInfoById(Books book);

	Integer getBestByTranId(Long tranId);
}
