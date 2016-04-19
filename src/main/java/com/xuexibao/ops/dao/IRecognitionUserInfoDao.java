package com.xuexibao.ops.dao;

import java.util.Date;
import java.util.List;

import com.xuexibao.ops.dao.base.IEntityDao;
import com.xuexibao.ops.model.UserInfo;



public interface IRecognitionUserInfoDao extends IEntityDao<UserInfo>{
	public static final String USER_KEY = "userKey";
	public static final String USER_MOBILE = "userMobile";

	/**
	 * 用户登录检验
	 */
	public UserInfo selectByUserAndPassword(String username, String password) throws Exception;
	
	long searchCount(String username, String role, String groupName, String teamId,
			String status, Date startDate, Date endDate);

	long searchTeamIdCount(long teamId);
	
	List<UserInfo> searchList(String username, String role, String groupName, String teamId,
					String status, Date startDate, Date endDate, Long page, int limit);
	
	
	/**
	 * 更新用户信息
	 * @param id
	 * @param userKey
	 * @param mobile
	 * @param password
	 * @return
	 */
	
	public UserInfo selectByUserMobile(String UserMobile) throws Exception;
	
	public UserInfo selectOneByUserKey(String UserKey) throws Exception;
	
	public UserInfo selectByUserKey(String UserKey) throws Exception;
	
	public UserInfo selectByUserId(Integer id) throws Exception;
	
	public List<UserInfo> getCanCaptainList();
	
	public List<UserInfo> getUsersListByTeamId(Long teamId);
	
	public UserInfo addUserInfo(UserInfo userinfo);
	
	public int updateUserInfo(UserInfo userinfo);
	
	public int updateUserInfoPassword(Integer id, String password, String operatorName);
}
