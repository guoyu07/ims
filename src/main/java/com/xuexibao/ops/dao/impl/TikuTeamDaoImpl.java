package com.xuexibao.ops.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.dao.ITikuTeamDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.TikuTeam;

@Repository
public class TikuTeamDaoImpl extends EntityDaoImpl<TikuTeam> implements ITikuTeamDao {
	protected void initDao() {
		// do nothing
	}

	@Override
	public long searchCount(Long teamId, String captain) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		para.put("captainName", captain);

		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCount", para);
		return count;
	};
	@Override
	public long searchCount(Long teamId, String captain, String role) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		para.put("captain", captain);
		para.put("role", role);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchCounts", para);
		return count;
	};
	@Override
	public List<TikuTeam> searchList(Long teamId, String captain, Long page, Integer limit){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		para.put("captainName", captain);
		para.put("offset", page);
		para.put("limit", limit);
		List<TikuTeam> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	};
	
	@Override
	public List<TikuTeam> searchList(Long teamId, String captain){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		para.put("captain", captain);
		List<TikuTeam> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchList", para);
		return results;
	};
	
	@Override
	public List<TikuTeam> searchList(Long teamId, String captain, String role, Long page, Integer limit){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		para.put("captain", captain);
		para.put("role", role);
		para.put("offset", page);
		para.put("limit", limit);
		List<TikuTeam> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchLists", para);
		return results;
	};
	
	@Override
	public List<TikuTeam> getAllList(String role){
	  Map<String, Object> para = new HashMap<String, Object>();
	  para.put("role", role);
		return getSqlSessionTemplate().selectList(getNameSpace() + ".getAllList", para);
	}
	
	@Override
	public Integer getTeamIdByCaptain(String captain){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("captain", captain);

		Integer id = getSqlSessionTemplate().selectOne(getNameSpace() + ".getIdByCaptain", para);
		return id;
	}
	
	@Override
	public TikuTeam getTeamById(Integer teamId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);

		TikuTeam team = getSqlSessionTemplate().selectOne(getNameSpace() + ".getTeamById", para);
		return team;
	}
	
	@Override
	public int getIdByTeamName(String teamName){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamName", teamName);

		int id = getSqlSessionTemplate().selectOne(getNameSpace() + ".getIdByTeamName", para);
		return id;
	}
}

