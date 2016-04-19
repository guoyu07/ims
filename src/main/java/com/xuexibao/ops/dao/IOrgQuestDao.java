package com.xuexibao.ops.dao;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.OrgQuest;



public interface IOrgQuestDao extends IEntityDao<OrgQuest>{
	public OrgQuest getByIdKey(Integer orgId,Long realquestId);
	public OrgQuest getById(Long Id);
	public OrgQuest addOrgQuestInfo(OrgQuest orgQuestInfo);
	
}
