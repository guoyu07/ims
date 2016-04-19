package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IOrcPictureBatchDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.OrcPictureBatch;
import com.xuexibao.ops.model.OrcPictureResultCount;

@Repository
public class OrcPictureBatchDaoImpl extends EntityDaoImpl<OrcPictureBatch> implements IOrcPictureBatchDao  {

	@Override
	public long searchCount(String target,String pictureId, String userKey,
			Integer status, Date startDate, Date endDate, String fileName) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("target", target);
		para.put("originalFileName", fileName);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<OrcPictureBatch> searchList(String target,String pictureId, String userKey,
			Integer status,  Date startDate, Date endDate, Long page, Long limit, String fileName) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("pictureId", pictureId);
		para.put("target", target);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("originalFileName", fileName);
		List<OrcPictureBatch> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	@Override
	public List<OrcPictureBatch> searchIds(Date startDate, Date endDate, Integer teamId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("teamid", teamId);
		List<OrcPictureBatch> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchIds", para);
		return results;
	}
	
	@Override	
	public long getHasAnswerCount(Long questionId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".getHasAnswerCount", para);
		return count;
	}
	
	@Override	
	public List<OrcPictureBatch>  searchHasAnswerList(Long questionId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		List<OrcPictureBatch> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchHasAnswerList", para);
		return results;
	}

	@Override
	public OrcPictureBatch getNextById(String target,Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate, String batchId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("batchId", batchId);
		para.put("target", target);
		List<OrcPictureBatch> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getNextById", para);
		 if(results != null && results.size() == 1)
		 {
			 OrcPictureBatch orcPicture=results.get(0);
			 return orcPicture;
		 }
		return null;
	}
	@Override
	public OrcPictureBatch getLastById(String target,Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate, String batchId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("batchId", batchId);
		para.put("target", target);
		List<OrcPictureBatch> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getLastById", para);
		 if(results != null && results.size() == 1)
		 {
			 OrcPictureBatch orcPicture=results.get(0);
			 return orcPicture;
		 }
		return null;
	}
	
	@Override
	public List<OrcPictureBatch> computeRecPercent(String target,String batchId, String userKey,
			Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("batchId", batchId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("target", target);
		List<OrcPictureBatch> counts = getSqlSessionTemplate().selectList(getNameSpace() + ".computeRecPercent", para);
		return counts;
	}
	
	@Override
	public List<OrcPictureBatch> getPictureIdsByBatchId(String batchId, String status) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("batchId", batchId);
		para.put("status", status);
		List<OrcPictureBatch> counts = getSqlSessionTemplate().selectList(getNameSpace() + ".getByBatchId", para);
		return counts;
	}
	
	@Override
	public List<OrcPictureResultCount> computeRecPercentPerMonth(String target, String userKey, String searchMonth){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("target", target);
		para.put("userKey", userKey);
		para.put("searchMonth", searchMonth);
		List<OrcPictureResultCount> countsList = getSqlSessionTemplate().selectList(getNameSpace() + ".computeRecPercentPerMonth", para);
		return countsList;
	}	
}
