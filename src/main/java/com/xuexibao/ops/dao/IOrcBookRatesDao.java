package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.OrcBookRates;



public interface IOrcBookRatesDao extends IEntityDao<OrcBookRates>{

	long searchCount(String name, String isbn, String sourceName, String teamName, Integer teamId, 
			Integer status, Date startDate, Date endDate);

	List<OrcBookRates> searchList(String name, String isbn,
			String sourceName, String teamName, Integer teamId, Integer status, Date startDate, Date endDate,
			Long page, int limit);
	public OrcBookRates getByBookId(Long bookid);
	public OrcBookRates getById(Long id);
}
