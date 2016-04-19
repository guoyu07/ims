package com.xuexibao.ops.dao;

import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.Announcement;
public interface IAnnouncementDao extends IEntityDao<Announcement> {
	
	public List<Announcement> searchList(Integer status, Long page, int limit);
	
	public long searchCount(Integer status);
	public Announcement insertSelective(Announcement announcement);
	public Announcement getNewAnnouncement();
}
