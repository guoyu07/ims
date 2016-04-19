package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.constant.RoleNameConstant;
import com.xuexibao.ops.dao.ITranOpsDao;
import com.xuexibao.ops.dao.IUserInfoDao;
import com.xuexibao.ops.enumeration.EnumTiKuPersonStatus;
import com.xuexibao.ops.model.UserInfo;

@Service
public class UserInfoService {
	
	@Resource
	IUserInfoDao userInfoDao;
	@Resource
	ITranOpsDao tranOpsDao;

	public UserInfo login(String userKey, String password) {
		UserInfo userInfo = userInfoDao.selectByUserAndPassword(userKey,password);
		return userInfo;
	}
	
	public long searchCount(String username, String role, String groupName, String teamId,
			String status, Date startDate, Date endDate) {
		return userInfoDao.searchCount(username, role, groupName, teamId,
				status, startDate, endDate);
	}
	
	public long searchTeamIdCount(long teamId) {
		return userInfoDao.searchTeamIdCount(teamId);
	}
	
	public List<UserInfo> searchList(String username, String role,
			String groupName, String teamId, String status, Date startDate, Date endDate,
			Long page, int limit) {
		List<UserInfo> userInfoList = userInfoDao.searchList(username, role,
				groupName, teamId, status, startDate, endDate,
				page, limit);
		for(UserInfo userInfo : userInfoList) {
//			setTeamId(userInfo);	
			setStatus(userInfo);
		}
		
		return userInfoList;
	}
	
	public List<UserInfo> searchList(String username, String role,
			String groupName, String teamId, String status) {
		List<UserInfo> userInfoList = userInfoDao.searchList(username, role,
				groupName, teamId, status);
		for(UserInfo userInfo : userInfoList) {
//			setTeamId(userInfo);	
			setStatus(userInfo);
		}
		
		return userInfoList;
	}

	public List<UserInfo> searchList() {
		List<UserInfo> userInfoList = userInfoDao.searchList();
		for(UserInfo userInfo : userInfoList) {
			setStatus(userInfo);
		}	
		return userInfoList;
	}
	
	public List<UserInfo> searchLimitList(long id, int limit) {
		List<UserInfo> userInfoList = userInfoDao.searchLimitList(id, limit);
		for(UserInfo userInfo : userInfoList) {
			setStatus(userInfo);
		}	
		return userInfoList;
	}
	
	public UserInfo insertUserInfo(UserInfo userinfo){
	   UserInfo userinfos= userInfoDao.addUserInfo(userinfo);
	   return userinfos;
   }
   
	
   public UserInfo	getUserInfoByKey(String userKey) {
	   UserInfo userinfo =userInfoDao.selectByUserKey(userKey);
	   if(userinfo !=null)
	   {
	       setStatus(userinfo);
	   }
	   return userinfo;
   }
   
   public UserInfo	getOneUserInfoByKey(String userKey) {
	   UserInfo userinfo =userInfoDao.selectOneByUserKey(userKey);
	   if(userinfo !=null)
	   {
	       setStatus(userinfo);
	   }
	   return userinfo;
   }
   
   public UserInfo	getUserInfoById(Integer id) {
	   UserInfo userinfo =userInfoDao.selectByUserId(id);
	   if(userinfo !=null)
	   {
	       setStatus(userinfo);
	   }
	   return userinfo;
   }
   
   @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
   public int updateIfNecessary(UserInfo userinfo) {
	   int count= userInfoDao.updateUserInfo(userinfo);	 
	   tranOpsDao.updateTranOpsOperatorByUserKey(userinfo.getTeamId(), userinfo.getUserKey());
	   tranOpsDao.updateTranOpsContentOperatorByUserKey(userinfo.getTeamId(), userinfo.getUserKey());	   
	   return count;
   }
   
   public int updatePassword(Integer id, String password, String operatorName) {
	   int count= userInfoDao.updateUserInfoPassword(id, password,operatorName);	 
       return count;
   }
   public int updateUserInfoByUserKey(UserInfo userinfo) {
	   int count= userInfoDao.updateUserInfoByUserKey(userinfo);	 
       return count;
   }
   
   public boolean isExistUserMobile(String userMobile) 
   {
	   UserInfo userInfo = userInfoDao.selectByUserMobile(userMobile);
	   if(userInfo == null)
		   return false;
	   return true;
   }
   
   public boolean isExistUserKey(String userKey) 
   {
	   UserInfo userInfo = userInfoDao.selectByUserKey(userKey);
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
		return userInfoDao.getCanCaptainList();
	}
	
	public List<UserInfo> getUsersListByTeamId(Long teamId){
		return userInfoDao.getUsersListByTeamId(teamId);
	}
	
	public int updateUserNotCaptain(String oldCaptain){
		try{
			UserInfo userInfo = getOneUserInfoByKey(oldCaptain);
			
			if(userInfo != null){
				if(userInfo.getRole().equals(RoleNameConstant.TEACHER)){
					userInfo.setGroupname(GroupNameConstant.T_EDITOR);
				}else if(userInfo.getRole().equals(RoleNameConstant.STUDENT)){
					userInfo.setGroupname(GroupNameConstant.EDITOR);
				}				
			}	
			return userInfoDao.updateIfNecessary(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		
		}
	}
	
	public int updateUserCaptain(int teamId, String captain){
		try{
			UserInfo userInfo = getOneUserInfoByKey(captain);
			
			if(userInfo != null){
				if(userInfo.getRole().equals(RoleNameConstant.TEACHER)){
					userInfo.setGroupname(GroupNameConstant.T_SHEN_HE);
				}else if(userInfo.getRole().equals(RoleNameConstant.STUDENT)){
					userInfo.setGroupname(GroupNameConstant.SHEN_HE);
				}				
			}	
			userInfo.setTeamId(teamId);
			return userInfoDao.updateIfNecessary(userInfo);	
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		
		}
	}
	public int updateUserReadId(String userKey , Integer readid){
		try{
			UserInfo userInfo = getOneUserInfoByKey(userKey);
			userInfo.setReadid(readid);
			return userInfoDao.updateIfNecessary(userInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		
		}
	}
	
	
	public int newUserCaptain(int teamId, String captain){
		try{
			UserInfo userInfo = getOneUserInfoByKey(captain);
			if(userInfo != null){
				if(userInfo.getRole().equals(RoleNameConstant.TEACHER)){
					userInfo.setGroupname(GroupNameConstant.T_SHEN_HE);
				}else if(userInfo.getRole().equals(RoleNameConstant.STUDENT)){
					userInfo.setGroupname(GroupNameConstant.SHEN_HE);
				}	
				userInfo.setTeamId(teamId);
				return updateIfNecessary(userInfo);			
			}			
			return 0;				
		} catch (Exception e) {
			e.printStackTrace();
			return 0;		
		}
	}
	
	public Workbook save2Excel_BankInfo(List<UserInfo> userInfoList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		sheet.setDefaultColumnWidth(20);

		int rows = userInfoList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("账号");
		cell = row.createCell(1);
		cell.setCellValue("手机号");
		cell = row.createCell(2);
		cell.setCellValue("姓名");
		cell = row.createCell(3);
		cell.setCellValue("身份证号");
		cell = row.createCell(4);
		cell.setCellValue("开户银行");
		cell = row.createCell(5);
		cell.setCellValue("开卡地");
		cell = row.createCell(6);
		cell.setCellValue("支行名称");
		cell = row.createCell(7);
		cell.setCellValue("银行卡号");
	

		for (int i = 1; i <= rows; i++) {
			UserInfo userInfo = userInfoList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);

			cell.setCellValue(userInfo.getUserKey());
			cell = row.createCell(1);
			cell.setCellValue(userInfo.getUserMobile());
		    cell = row.createCell(2);	
		    cell.setCellValue(userInfo.getUserName());
		    cell = row.createCell(3);
			cell.setCellValue(userInfo.getIdNumber());
			cell = row.createCell(4);
			cell.setCellValue(userInfo.getBank());
			cell = row.createCell(5);
			cell.setCellValue(userInfo.getProvince()+" "+userInfo.getCity() +" " +userInfo.getCounty());
			cell = row.createCell(6);
			cell.setCellValue(userInfo.getBankSubbranch());
			cell = row.createCell(7);
			cell.setCellValue(userInfo.getBankCard());

		}

		return workBook;
	}
	public Workbook save2Excel(List<UserInfo> userInfoList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		sheet.setDefaultColumnWidth(20);

		int rows = userInfoList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("小组");
		cell = row.createCell(1);
		cell.setCellValue("角色组");
		cell = row.createCell(2);
		cell.setCellValue("账号");
		cell = row.createCell(3);
		cell.setCellValue("姓名");
		cell = row.createCell(4);
		cell.setCellValue("手机号");
		cell = row.createCell(5);
		cell.setCellValue("身份");
		cell = row.createCell(6);
		cell.setCellValue("账号状态");

		for (int i = 1; i <= rows; i++) {
			UserInfo userInfo = userInfoList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);
			cell.setCellValue(userInfo.getTikuTeam().getName());
			cell = row.createCell(1);
			cell.setCellValue(userInfo.getGroupname());
			cell = row.createCell(2);
			cell.setCellValue(userInfo.getUserKey());
			cell = row.createCell(3);
			cell.setCellValue(userInfo.getUserName());
			cell = row.createCell(4);
			cell.setCellValue(userInfo.getUserMobile());
			cell = row.createCell(5);
			cell.setCellValue(userInfo.getRole());
			cell = row.createCell(6);
			cell.setCellValue(userInfo.getStatusForShow());

		}

		return workBook;
	}

}