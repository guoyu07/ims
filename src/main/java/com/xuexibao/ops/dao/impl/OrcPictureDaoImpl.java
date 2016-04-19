package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IOrcPictureDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.OrcAnalysisBydate;
import com.xuexibao.ops.model.OrcPicture;

@Repository
public class OrcPictureDaoImpl extends EntityDaoImpl<OrcPicture> implements IOrcPictureDao  {

	@Override
	public long searchCount(Long pictureId, String userKey,
			Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public long searchCount(Long pictureId, Long bookId, String userKey,
			Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		para.put("bookId", bookId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public long searchCount(Long pictureId, List<Long> bookIds, String userKey,
			Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		if(bookIds != null && bookIds.size()>0){
			para.put("bookIds", bookIds);
		}else{
			para.put("bookIds", null);
		}
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCounts", para);
		return count;
	}
	@Override
	public List<OrcPicture> searchList(Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate, Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("pictureId", pictureId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	@Override
	public List<OrcPicture> searchList(Long pictureId, Long bookId, String userKey,
			Integer status,  Date startDate, Date endDate, Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("pictureId", pictureId);
		para.put("bookId", bookId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}
	
	@Override
	public List<OrcPicture> searchList(Long pictureId, List<Long> bookIds, String userKey,
			Integer status,  Date startDate, Date endDate, Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("pictureId", pictureId);
		if(bookIds != null && bookIds.size()>0){
			para.put("bookIds", bookIds);
		}else{
			para.put("bookIds", null);
		}
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchLists", para);
		return results;
	}
	
	@Override
	public List<OrcAnalysisBydate> searchListCount(Date startDate, Date endDate, Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcAnalysisBydate> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchListCount", para);
		return results;
	}
	@Override
	public List<OrcAnalysisBydate> saveSearchListCount(Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcAnalysisBydate> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchListCount", para);
		return results;
	}
	
	@Override
	public long searchOrcCount(Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchOrcCount", para);
		return count;
	}
	@Override
	public List<OrcPicture> searchIds(Date startDate, Date endDate, Integer teamId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		para.put("teamid", teamId);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchIds", para);
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
	public List<OrcPicture>  searchHasAnswerList(Long questionId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("questionId", questionId);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchHasAnswerList", para);
		return results;
	}

	@Override
	public OrcPicture getNextById(Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getNextById", para);
		 if(results != null && results.size() == 1)
		 {
			 OrcPicture orcPicture=results.get(0);
			 return orcPicture;
		 }
		return null;
	}
	@Override
	public OrcPicture getLastById(Long pictureId, String userKey,
			Integer status,  Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("pictureId", pictureId);
		para.put("userKey", userKey);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getLastById", para);
		 if(results != null && results.size() == 1)
		 {
			 OrcPicture orcPicture=results.get(0);
			 return orcPicture;
		 }
		return null;
	}
	@Override
	public List<OrcPicture> orcPictureByDate(Date today,Date yesterday){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", yesterday);
		para.put("endDate", today);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".orcPictureByDate", para);
		return results;
	}
	@Override
	public List<OrcPicture> orcPictureRightByDate(Date today,Date yesterday){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("startDate", yesterday);
		para.put("endDate", today);
		List<OrcPicture> results = getSqlSessionTemplate().selectList(getNameSpace() + ".orcPictureRightByDate", para);
		return results;
	}
	
	@Override
	public int updatePushstatusByBookId(Long bookId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("bookId", bookId);
		int count = getSqlSessionTemplate().update(getNameSpace() + ".updatePushstatusByBookId", para);
		return count;
	}
}
