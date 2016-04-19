package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IOrgQuestDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.OrgQuest;


@Repository
public class OrgQuestDaoImpl extends EntityDaoImpl<OrgQuest> implements IOrgQuestDao {
	
	
	@Override
	public OrgQuest getById(Long Id) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("Id", Id);
		OrgQuest orgquest = getSqlSessionTemplate().selectOne(getNameSpace() + ".getById", para);		
		return orgquest;
	}
	@Override
	public OrgQuest getByIdKey(Integer orgId,Long realquestId) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("orgId", orgId);
		para.put("realquestId", realquestId);
		OrgQuest orgquest = getSqlSessionTemplate().selectOne(getNameSpace() + ".getByIdKey", para);		
		return orgquest;
	}
	
	@Override
	public OrgQuest addOrgQuestInfo(OrgQuest orgquest){
		//插入
		OrgQuest orgquestInfo=insertSelective(orgquest);
		return orgquestInfo;
	}
}
