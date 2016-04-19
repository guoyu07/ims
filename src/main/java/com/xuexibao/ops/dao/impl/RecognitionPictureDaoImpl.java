package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IRecognitionPictureDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.RecognitionPicture;

@Repository
public class RecognitionPictureDaoImpl extends EntityDaoImpl<RecognitionPicture> implements IRecognitionPictureDao {

	@Override
	public List<RecognitionPicture> selectUnRecList(String operater, Integer limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operater", operater);
		para.put("limit", limit);
		List<RecognitionPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".selectUnRecList", para);
		return results;
	}

	@Override
	public List<RecognitionPicture> searchList(Long pictureId, Integer status, Date startDate, Date endDate, Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("pictureId", pictureId);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<RecognitionPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchUnRecList", para);
		return results;
	}

	@Override
	public List<RecognitionPicture> obtainRemainPictureList(int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("limit", limit);
		List<RecognitionPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".obtainRemainPictureList", para);
		return results;
	}
	
	@Override
	public List<RecognitionPicture> obtainAlternateRemainPictureList(int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("limit", limit);
		List<RecognitionPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".obtainAlternateRemainPictureList", para);
		return results;
	}
	
	@Override
	public List<RecognitionPicture> obtainYesterdayData(Date today,Date yesterday) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", yesterday);
		para.put("endDate", today);
		List<RecognitionPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".obtainYesterdayData", para);
		return results;
	}
	
	@Override
	public int updateRequestNum(List<RecognitionPicture> pictureList) {
		if(pictureList != null && pictureList.size() > 0)
			return this.getSqlSessionTemplate().update(getNameSpace() + ".updateRequestNum", pictureList);
		return 0;
	}
	
	@Override
	public int resetRequestNum(List<Long> needResetRequestIds) {
		if(needResetRequestIds != null && needResetRequestIds.size() > 0)
			return this.getSqlSessionTemplate().update(getNameSpace() + ".resetRequestNum", needResetRequestIds);
		return 0;
	}
	
	@Override
	public List<Long> needResetRequestIds(Date oneHourEarlier) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("oneHourEarlier", oneHourEarlier);
		return getSqlSessionTemplate().selectList(getNameSpace() + ".needResetRequestIds", para);
	}

	@Override
	public long searchCount(Long pictureId, Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}

	@Override
	public long CountRemainNum(String operator, Date today) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operator", operator);
		para.put("today", today);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".countRemainNum", para);
		return count;
	}

	@Override
	public long CountTodayNum(String operator, Date today,Date tomorrow) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("operator", operator);
		para.put("today", today);
		para.put("tomorrow", tomorrow);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".countTodayNum", para);
		return count;
	}

	@Override
	public long CountTotalRecognizedNum() {
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".countTotalRecognizedNum");
		return count;
	}
}
