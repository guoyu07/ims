package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.RecognitionPicture;

public interface IRecognitionPictureDao extends IEntityDao<RecognitionPicture> {

	List<RecognitionPicture> searchList(Long pictureId, Integer status, Date startDate, Date endDate, Long page, int limit);

	List<RecognitionPicture> selectUnRecList(String operater, Integer limit);

	long searchCount(Long pictureId, Integer status, Date startDate, Date endDate);

	long CountRemainNum(String operator, Date today);

	long CountTodayNum(String operator, Date today, Date tomorrow);

	long CountTotalRecognizedNum();

	List<RecognitionPicture> obtainRemainPictureList(int limit);
	
	List<RecognitionPicture> obtainAlternateRemainPictureList(int limit);
	
	int updateRequestNum(List<RecognitionPicture> results);

	List<Long> needResetRequestIds(Date oneHourEarlier);

	int resetRequestNum(List<Long> needResetRequestIds);

	List<RecognitionPicture> obtainYesterdayData(Date today, Date yesterday);
}
