package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.dao.IRecognitionUserInfoDao;
import com.xuexibao.ops.enumeration.EnumTiKuPersonStatus;
import com.xuexibao.ops.model.UserInfo;

@Service
public class RecognitionUserInfoService {
	
	@Resource
	IRecognitionUserInfoDao recognitionUserInfoDao;

	public UserInfo login(String userKey, String password) throws Exception {
		UserInfo userInfo = recognitionUserInfoDao.selectByUserAndPassword(userKey,password);
		return userInfo;
	}
	
	public long searchCount(String username, String role, String groupName, String teamId,
			String status, Date startDate, Date endDate) {
		return recognitionUserInfoDao.searchCount(username, role, groupName, teamId,
				status, startDate, endDate);
	}
	
	public long searchTeamIdCount(long teamId) {
		return recognitionUserInfoDao.searchTeamIdCount(teamId);
	}
	
	public List<UserInfo> searchList(String username, String role,
			String groupName, String teamId, String status, Date startDate, Date endDate,
			Long page, int limit) {
		List<UserInfo> userInfoList = recognitionUserInfoDao.searchList(username, role,
				groupName, teamId, status, startDate, endDate,
				page, limit);
		for(UserInfo userInfo : userInfoList) {
//			setTeamId(userInfo);	
			setStatus(userInfo);
		}
		
		return userInfoList;
	}

	public UserInfo  insertUserInfo(UserInfo userinfo){
	   UserInfo userinfos= recognitionUserInfoDao.addUserInfo(userinfo);
	   return userinfos;
   }
   
   public UserInfo	getUserInfoByKey(String userKey) throws Exception{
	   UserInfo userinfo =recognitionUserInfoDao.selectByUserKey(userKey);
	   if(userinfo !=null)
	   {
	       setStatus(userinfo);
	   }
	   return userinfo;
   }
   
   public UserInfo	getOneUserInfoByKey(String userKey) throws Exception{
	   UserInfo userinfo =recognitionUserInfoDao.selectOneByUserKey(userKey);
	   if(userinfo !=null)
	   {
	       setStatus(userinfo);
	   }
	   return userinfo;
   }
   
   public UserInfo	getUserInfoById(Integer id) throws Exception{
	   UserInfo userinfo =recognitionUserInfoDao.selectByUserId(id);
	   if(userinfo !=null)
	   {
	       setStatus(userinfo);
	   }
	   return userinfo;
   }
   
   
   public int updatePassword(Integer id, String password, String operatorName) throws Exception{
	   int count= recognitionUserInfoDao.updateUserInfoPassword(id, password,operatorName);	 
       return count;
   }
   
   public boolean isExistUserMobile(String userMobile) throws Exception
   {
	   UserInfo userInfo = recognitionUserInfoDao.selectByUserMobile(userMobile);
	   if(userInfo == null)
		   return false;
	   return true;
   }
   
   public boolean isExistUserKey(String userKey) throws Exception
   {
	   UserInfo userInfo = recognitionUserInfoDao.selectByUserKey(userKey);
	   if(userInfo == null)
		   return false;
	   return true;
   }
   
	private void setStatus(UserInfo userinfo) {
		Byte status = userinfo.getStatus();
    	if( status != null) {
    		for(EnumTiKuPersonStatus Status : EnumTiKuPersonStatus.values()) {
    			if(status.toString().equals(Status.getId())) {
    				userinfo.setStatusForShow(Status.getDesc());
    				break;
    			}
    		}
    	}
    }
	
	public List<UserInfo> getCanCaptainList(){
		return recognitionUserInfoDao.getCanCaptainList();
	}
	
	public List<UserInfo> getUsersListByTeamId(Long teamId){
		return recognitionUserInfoDao.getUsersListByTeamId(teamId);
	}
	
	public int updateUserNotCaptain(String oldCaptain){
		try{
			UserInfo userInfo = getOneUserInfoByKey(oldCaptain);
			userInfo.setGroupname(GroupNameConstant.EDITOR);
			return recognitionUserInfoDao.updateIfNecessary(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		
		}
	}
	
	public int updateUserCaptain(int teamId, String captain){
		try{
			UserInfo userInfo = getOneUserInfoByKey(captain);
			userInfo.setGroupname(GroupNameConstant.SHEN_HE);
			userInfo.setTeamId(teamId);
			return recognitionUserInfoDao.updateIfNecessary(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		
		}
	}
}