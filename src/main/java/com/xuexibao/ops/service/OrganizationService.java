package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.IOrganizationSourcesDao;
import com.xuexibao.ops.model.OrganizationSources;

@Service
public class OrganizationService {
	@Resource
	IOrganizationSourcesDao organizationSourcesDao;
	public List<OrganizationSources> searchList(String name, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		List<OrganizationSources> organizationList = organizationSourcesDao.searchList(name, status,startDate,endDate,
				page, limit);	
		return organizationList;
	}
	public List<OrganizationSources> searchList(String name) {
		List<OrganizationSources> organizationList = organizationSourcesDao.searchList(name);		
		return organizationList;
	}
	public long searchCount(String name,
			Integer status, Date startDate, Date endDate) {
		return organizationSourcesDao.searchCount(name,
				status, startDate, endDate);
	}
	
	public OrganizationSources getById(long Id) {
		OrganizationSources organizationSources = organizationSourcesDao.getById(Id);
		return organizationSources;
	}
	
	public OrganizationSources getByName(String name) throws Exception{
		OrganizationSources organizationSources = organizationSourcesDao.getByName(name);
		return organizationSources;
	}
	
	public Long getIdByName(String name){
		Long id = organizationSourcesDao.getIdByName(name);
		return id;
	}
	public boolean isExistId(long Id) throws Exception
	{
		OrganizationSources organizationSources = organizationSourcesDao.getById(Id);
		if(organizationSources == null)
			   return false;
		   return true;
	}
	
	public boolean isExistName(String name) throws Exception
	{
		Long id = organizationSourcesDao.getIdByName(name);
		if(id == null)
			   return false;
		   return true;
	}
	
	public OrganizationSources insertOrganizationSourcesInfo(OrganizationSources organizationSources){
		   OrganizationSources organizationSourcesInfo= organizationSourcesDao.addOrganizationSourcesInfo(organizationSources);
		   return organizationSourcesInfo;
	}
	   
		
    public OrganizationSources getOrganizationSourcesInfoByName(String name) throws Exception{
    	   OrganizationSources organizationSources =organizationSourcesDao.getByName(name);
		   return organizationSources;
	}
}
