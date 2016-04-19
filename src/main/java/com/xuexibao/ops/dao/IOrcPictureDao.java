package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.OrcAnalysisBydate;
import com.xuexibao.ops.model.OrcPicture;

public interface IOrcPictureDao extends IEntityDao<OrcPicture>   {

	
	long searchCount(Long pictureId, String userKey,
			Integer status, Date startDate, Date endDate);
	long searchCount(Long pictureId, Long bookId, String userKey,
			Integer status, Date startDate, Date endDate);
	long searchCount(Long pictureId, List<Long> bookIds, String userKey,
			Integer status, Date startDate, Date endDate);
	
	List<OrcPicture> searchList(Long pictureId, String userKey,
			Integer status, Date startDate, Date endDate, Long page, int limit);

	List<OrcPicture> searchList(Long pictureId, Long bookId, String userKey,
			Integer status, Date startDate, Date endDate, Long page, int limit);

	List<OrcPicture> searchList(Long pictureId, List<Long> bookIds, String userKey,
			Integer status, Date startDate, Date endDate, Long page, int limit);
	
	long searchOrcCount(Date startDate, Date endDate);
	
	List<OrcAnalysisBydate> searchListCount(Date startDate, Date endDate, Long page, int limit);
	List<OrcAnalysisBydate> saveSearchListCount(Date startDate, Date endDate);
	
	List<OrcPicture> searchIds(Date startDate, Date endDate, Integer teamId);
	
	long getHasAnswerCount(Long questonId);
	
	List<OrcPicture>  searchHasAnswerList(Long questionId);
	
	public OrcPicture getNextById(Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate);
	public OrcPicture getLastById(Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate);

	List<OrcPicture> orcPictureByDate(Date todayDawn,Date yesterdayDawn);
	List<OrcPicture> orcPictureRightByDate(Date todayDawn,Date yesterdayDawn);
	
	public int updatePushstatusByBookId(Long bookId);
}
