package com.xuexibao.ops.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.ITikuTeamDao;
import com.xuexibao.ops.model.TikuTeam;
import com.xuexibao.ops.model.UserInfo;


@Service
public class TikuTeamService {
	
	@Resource
	ITikuTeamDao tikuTeamDao;
	
	@Autowired
	protected TranOpsService tranOpsService;
	
	@Autowired
	protected UserInfoService userInfoService;
	
	public long searchCount(Long teamId, String captain) {
		return tikuTeamDao.searchCount(teamId, captain);
	}
	
	public long searchCount(Long teamId, String captain , String role) {
		return tikuTeamDao.searchCount(teamId, captain , role);
	}
	
	public long searchCount() {
		return this.searchCount(null, null);
	}

	public List<TikuTeam> searchList(Long teamId, String captain, Long page, Integer limit) {
		List<TikuTeam> tikuTeamList = tikuTeamDao.searchList(teamId, captain, page, limit);
		return tikuTeamList;
	}
	public List<TikuTeam> searchList(Long teamId, String captain, String role, Long page, Integer limit) {
		List<TikuTeam> tikuTeamList = tikuTeamDao.searchList(teamId, captain, role, page, limit);
		return tikuTeamList;
	}
	
	public List<TikuTeam> searchList(String role, Long page, Integer limit) {
		return this.searchList(null, null, role, null, null);
	}
	
	public List<TikuTeam> searchList(Long teamId, String captain) {
		List<TikuTeam> tikuTeamList = tikuTeamDao.searchList(teamId, captain);
		return tikuTeamList;
	}
	
	public TikuTeam getTeamById(Integer teamId) {
		TikuTeam tikuTeam = tikuTeamDao.getTeamById(teamId);
		return tikuTeam;
	}
	
	public Integer getTeamIdByCaptain(String captain){
		Integer id = tikuTeamDao.getTeamIdByCaptain(captain);
		return id;
	}
	
	public void newTikuTeam(String team_name, String captain_name){
		return;
		
	}
	
	public void editTikuTeam(String team_name, String captain_name){
		return;
		
	}	
	public List<TikuTeam> getTikuTeamList(String role) {
		List<TikuTeam> tikuTeamList = tikuTeamDao.getAllList(role);
		return tikuTeamList;
	}
	
	public Workbook save2Excel(List<TikuTeam> tikuTeamList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		int rows = tikuTeamList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("小组名称");
		cell = row.createCell(1);
		cell.setCellValue("组长");
		cell = row.createCell(2);
		cell.setCellValue("组员数量");
		cell = row.createCell(3);
		cell.setCellValue("仅录入题干数");
		cell = row.createCell(4);
		cell.setCellValue("录入完整题目数");
		cell = row.createCell(5);
		cell.setCellValue("待审核数");
		cell = row.createCell(6);
		cell.setCellValue("审核通过数");

		for (int i = 1; i <= rows; i++) {
			TikuTeam tikuTeam = tikuTeamList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);
			cell.setCellValue(tikuTeam.getName());
			cell = row.createCell(1);
			cell.setCellValue(tikuTeam.getCaptain());
			cell = row.createCell(2);
			cell.setCellValue(tikuTeam.getUsersNum());
			cell = row.createCell(3);
			cell.setCellValue(tikuTeam.getTransContentNum());
			cell = row.createCell(4);
			cell.setCellValue(tikuTeam.getTransCompleteNum());
			cell = row.createCell(5);
			cell.setCellValue(tikuTeam.getTransUnCheckNum());
			cell = row.createCell(6);
			cell.setCellValue(tikuTeam.getTransCheckedNum());
		}

		return workBook;
	}
	
	public void setTeamNum(TikuTeam team, String transStartDate, String transEndDate,
			   String checkStartDate, String checkEndDate){
		long id = team.getId();
		long transContentNum = tranOpsService.searchContentCount(id, transStartDate, 
				transEndDate);
		team.setTransContentNum(transContentNum);
		
		long transCompleteNum = tranOpsService.searchCompleteCount(id, transStartDate, 
				transEndDate);
		team.setTransCompleteNum(transCompleteNum);
		
		long transUnCheckNum = tranOpsService.searchCheckCount(id, 0, transStartDate, 
				transEndDate);
		team.setTransUnCheckNum(transUnCheckNum);
		
		long transCheckedNum = tranOpsService.searchCheckCount(id, 2, checkStartDate, 
				checkEndDate);
		team.setTransCheckedNum(transCheckedNum);
		
		long usersNum = userInfoService.searchTeamIdCount(id);
		team.setUsersNum(usersNum);	
		
	}
	
	public void setTeamNum(TikuTeam team){

		long usersNum = userInfoService.searchTeamIdCount(team.getId());
		team.setUsersNum(usersNum);	
		
	}
	
	public int updateTeamCaptain(Integer teamId, String newCaptain){
		TikuTeam team = getTeamById(teamId);
		try{
			UserInfo userInfo = userInfoService.getOneUserInfoByKey(newCaptain);
			team.setCaptain(newCaptain);
			team.setCaptainName(userInfo.getUserName());
			
			return updateTeamIfNecessary(team);	
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
			
	}
	
	public int updateTeamIfNecessary(TikuTeam team){
		   return tikuTeamDao.updateIfNecessary(team);
	   }
	
	public TikuTeam	insertTikuTeam(TikuTeam tikuTeam){
		   return tikuTeamDao.insertSelective(tikuTeam);
	   }
	
	public int getIdByTeamName(String teamName){
		return tikuTeamDao.getIdByTeamName(teamName);
	}
	
	public int updateTeamName(Integer teamId, String newTeamName){
		TikuTeam team = getTeamById(teamId);
		team.setName(newTeamName);		
		return updateTeamIfNecessary(team);	
	}
	
}


