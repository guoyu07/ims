package com.xuexibao.ops.dao;

import java.util.List;



import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.TikuTeam;

public interface ITikuTeamDao extends IEntityDao<TikuTeam>   {

	long searchCount(Long teamId, String captain);
	long searchCount(Long teamId, String captain, String role);
	List<TikuTeam> searchList(Long teamId, String captain, Long page, Integer limit);
	List<TikuTeam> searchList(Long teamId, String captain, String role, Long page, Integer limit);
	List<TikuTeam> searchList(Long teamId, String captain);


	List<TikuTeam> getAllList(String role);
	
	Integer getTeamIdByCaptain(String captain);
	
	TikuTeam getTeamById(Integer teamId);
	
	int getIdByTeamName(String teamName);
}
