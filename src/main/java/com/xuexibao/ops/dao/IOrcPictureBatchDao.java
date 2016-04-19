package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.OrcPictureBatch;
import com.xuexibao.ops.model.OrcPictureResultCount;

public interface IOrcPictureBatchDao extends IEntityDao<OrcPictureBatch>   {

	
	long searchCount(String target,String pictureId, String userKey,
			Integer status, Date startDate, Date endDate,String fileName);
	
	List<OrcPictureBatch> searchList(String target, String pictureId, String userKey,
			Integer status, Date startDate, Date endDate, Long page, Long limit,String fileName);

	List<OrcPictureBatch> searchIds(Date startDate, Date endDate, Integer teamId);
	
	long getHasAnswerCount(Long questonId);
	
	List<OrcPictureBatch>  searchHasAnswerList(Long questionId);
	
	public OrcPictureBatch getNextById(String target,Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate, String batchId);
	
	public OrcPictureBatch getLastById(String target,Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate, String batchId);
	
	List<OrcPictureBatch> computeRecPercent(String target,String batchId, String userKey,
			Integer status, Date startDate, Date endDate);
	
	public List<OrcPictureBatch> getPictureIdsByBatchId(String batchId, String status);
	
	List<OrcPictureResultCount> computeRecPercentPerMonth(String target, String userKey, String searchMonth);	

}
