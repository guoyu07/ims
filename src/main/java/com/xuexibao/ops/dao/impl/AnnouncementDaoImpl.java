package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IAnnouncementDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.Announcement;

@Repository
public class AnnouncementDaoImpl extends EntityDaoImpl<Announcement> implements IAnnouncementDao{
	
	@Override
	public long searchCount(Integer status) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("status", status);

		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<Announcement> searchList(Integer status, Long page, int limit){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("status", status);
		para.put("offset", page);
		para.put("limit", limit);
		List<Announcement> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	}

	@Override
	public Announcement getNewAnnouncement(){
		Map<String, Object> para = new HashMap<String, Object>();
		Announcement result = getSqlSessionTemplate().selectOne(getNameSpace() + ".getNewAnnouncement", para);
		return result;
	}
}
