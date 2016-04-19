package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.IOrganizationSourcesDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.OrganizationSources;

@Repository
public class OrganizationSourcesDaoImpl extends EntityDaoImpl<OrganizationSources> implements IOrganizationSourcesDao {
	
	@Override
	public long searchCount(String name,Integer status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("name", name);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	}
	
	@Override
	public List<OrganizationSources> searchList(String name,Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("name", name);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<OrganizationSources> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);		
		return results;
	}
	@Override
	public List<OrganizationSources> searchList(String name) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("name", name);
		List<OrganizationSources> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getListbyName", para);		
		return results;
	}
	
	@Override
	public Long getIdByName(String name){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("name", name);
		Long id = getSqlSessionTemplate().selectOne(getNameSpace() + ".getIdByName", para);
		return id;

	}
	@Override
	public OrganizationSources getById(Long Id){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("Id", Id);
		OrganizationSources organizationSources = getSqlSessionTemplate().selectOne(getNameSpace() + ".getOrganizationSourcesById", para);
		return organizationSources;
	}
	@Override
	public OrganizationSources getByName(String name){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("name", name);
		OrganizationSources organizationSources = getSqlSessionTemplate().selectOne(getNameSpace() + ".getOrganizationSourcesByName", para);
		return organizationSources;
	}
	
	@Override
	public OrganizationSources addOrganizationSourcesInfo(OrganizationSources organizationsources){
		//插入
		OrganizationSources organizationSourcesInfo=insertSelective(organizationsources);
		return organizationSourcesInfo;
	}
}
