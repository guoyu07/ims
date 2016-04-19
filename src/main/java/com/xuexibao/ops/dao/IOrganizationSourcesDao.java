package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.OrganizationSources;



public interface IOrganizationSourcesDao extends IEntityDao<OrganizationSources>{

	long searchCount(String name, 
			Integer status, Date startDate, Date endDate);

	List<OrganizationSources> searchList(String name, Integer status, Date startDate, Date endDate,
			Long page, int limit);
	List<OrganizationSources> searchList(String name);
	public Long getIdByName(String name);
	public OrganizationSources getById(Long Id);
	public OrganizationSources getByName(String name);
	public OrganizationSources addOrganizationSourcesInfo(OrganizationSources organizationSourcesInfo);
}
