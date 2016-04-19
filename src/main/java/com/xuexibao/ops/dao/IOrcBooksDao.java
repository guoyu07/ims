package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.OrcBooks;



public interface IOrcBooksDao extends IEntityDao<OrcBooks>{

	long searchCount(String name, String isbn, String sourceName, String teamName, Integer teamId, 
			Integer status, Date startDate, Date endDate);

	List<OrcBooks> searchList(String name, String isbn,
			String sourceName, String teamName, Integer teamId, Integer status, Date startDate, Date endDate,
			Long page, int limit);
	List<OrcBooks> searchList(String name, String isbn,
			String sourceName, String teamName, Integer teamId, Integer status, Date startDate, Date endDate);
	public int updateStatus(Long id, Integer status, String operatorName);
	public OrcBooks getById(Long Id);
	public OrcBooks getByBookId(Long bookId);
}
