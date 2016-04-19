package com.xuexibao.ops.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.dao.IRecognitionUserInfoDao;
import com.xuexibao.ops.dao.base.EntityDaoImpl;
import com.xuexibao.ops.model.UserInfo;
import com.xuexibao.ops.util.Md5PasswordEncoder;


@Repository
public class RecognitionUserInfoDaoImpl extends EntityDaoImpl<UserInfo> implements IRecognitionUserInfoDao {
	
	/**
	 * 用户登录检验
	 * @throws Exception 
	 */
	@Override
	public UserInfo selectByUserAndPassword(String userKey, String password) throws Exception {
		String tmp  = Md5PasswordEncoder.encode(password);
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userKey", userKey);
		para.put("password", tmp);
		UserInfo userinfo = getSqlSessionTemplate().selectOne(getNameSpace() + ".selectByUserAndPassword", para);
		return userinfo;
	}

	/**
	 * 用户登录检验
	 * @throws Exception 
	 */
	@Override
	public UserInfo selectByUserId(Integer id) throws Exception {
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", id);
		UserInfo userinfo = getSqlSessionTemplate().selectOne(getNameSpace() + ".selectByUserId", para);
		return userinfo;
	}
	/**
	 * 用户登录检验
	 * @throws Exception 
	 */
	@Override
	public UserInfo selectByUserKey(String userKey) throws Exception {
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userKey", userKey);
		
		UserInfo userinfo = getSqlSessionTemplate().selectOne(getNameSpace() + ".selectByRecognitionUserKey", para);
		return userinfo;
	}
	
	/**
	 * 用户登录检验
	 * @throws Exception 
	 */
	@Override
	public UserInfo selectByUserMobile(String userMobile) throws Exception {
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userMobile", userMobile);	
		UserInfo userinfo = getSqlSessionTemplate().selectOne(getNameSpace() + ".selectByUserMobile", para);
		return userinfo;
	}
	
	@Override
	public UserInfo selectOneByUserKey(String userKey) throws Exception {
		
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userKey", userKey);	
		UserInfo userinfo = getSqlSessionTemplate().selectOne(getNameSpace() + ".selectOneByUserkey", para);
		return userinfo;
	}
	
	
	@Override
	public long searchCount(String userKey, String role, String groupName, String teamId,
			String status, Date startDate, Date endDate) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("userKey", userKey);
		para.put("role", role);
		para.put("groupName", groupName);
		para.put("teamId", teamId);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchRecognitionCount", para);
		return count;
	}
	
	@Override
	public long searchTeamIdCount(long teamId) {
		Map<String, Object> para = new HashMap<String, Object>();
	
		para.put("teamId", teamId);
	
		long count = getSqlSessionTemplate().selectOne(getNameSpace() + ".searchTeamIdCount", para);
		return count;
	}
	/**
	 * 用户列表
	 * @throws Exception 
	 */
	@Override
	public List<UserInfo> searchList(String userKey, String role,
			String groupName, String teamId, String status, Date startDate, Date endDate,
			Long page, int limit) {
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("offset", page);
		para.put("limit", limit);
		para.put("userKey", userKey);
		para.put("role", role);
		para.put("groupName", groupName);
		para.put("teamId", teamId);
		para.put("status", status);
		para.put("startDate", startDate);
		para.put("endDate", endDate);
		List<UserInfo> results = getSqlSessionTemplate().selectList(getNameSpace() + ".searchRecognitionList", para);		
		return results;
	}
	
	@Override
	public List<UserInfo> getCanCaptainList(){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("datizu", GroupNameConstant.EDITOR);
		para.put("tdatizu", GroupNameConstant.T_EDITOR);
		List<UserInfo> results = getSqlSessionTemplate().selectList(getNameSpace() + ".canCaptainList", para);		
		return results;
	}
	
	@Override
	public List<UserInfo> getUsersListByTeamId(Long teamId){
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("teamId", teamId);
		List<UserInfo> results = getSqlSessionTemplate().selectList(getNameSpace() + ".getUsersListByTeamId", para);
		return results;
	}

	@Override
	public UserInfo addUserInfo(UserInfo userinfo){
		//插入新成员
		UserInfo userinfos=insertSelective(userinfo);
		return userinfos;
	}

	@Override
	public int updateUserInfo(UserInfo userinfo) {
		// TODO Auto-generated method stub
		//更新成员
		int count=updateIfNecessary(userinfo);
		return count;
	}
	@Override
	public int updateUserInfoPassword(Integer id, String password, String operatorName) {
		// TODO Auto-generated method stub
		//重置密码
		Map<String, Object> para = new HashMap<String, Object>();
		para.put("id", id);
		para.put("password", password);
		para.put("updateTime", new Date());
		para.put("operator", operatorName);
		int count = getSqlSessionTemplate().update(getNameSpace() + ".updateUserInfoPassword", para);	
		return count;
	}
}
