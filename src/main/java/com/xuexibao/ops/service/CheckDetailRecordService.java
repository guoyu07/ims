package com.xuexibao.ops.service;

import java.text.DecimalFormat;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.ICheckDetailRecordDao;
import com.xuexibao.ops.dao.ITikuTeamDao;
import com.xuexibao.ops.model.CheckDetailRecord;
import com.xuexibao.ops.model.TikuTeam;
import com.xuexibao.ops.util.DateUtils;

@Service
public class CheckDetailRecordService {
	
	@Resource
	ICheckDetailRecordDao checkDetailRecordDao;
	
	@Resource
	ITikuTeamDao tikuTeamDao;

	public List<CheckDetailRecord> searchList(Integer month, Integer teamid, Long page, int limit) {
		String month_str = "";
		if(month != null){
			month_str = month < 10 ? ("0" + month):  Integer.toString(month);
		}
		List<CheckDetailRecord> checkDetailRecordList = checkDetailRecordDao.searchList(month_str, teamid, page, limit);
		for(CheckDetailRecord record : checkDetailRecordList){
			DecimalFormat df = new DecimalFormat("0.00%");
			if(record.getNum() == 0) {
				record.setRatioStr("0.00%");
			} else {
				double ratio = (double)record.getPassNum() / (double)record.getNum();
				String ratioStr = df.format(ratio);
				record.setRatioStr(ratioStr);
			}
		}
		return checkDetailRecordList;
	}
	
	public List<CheckDetailRecord> searchList(Long parentId) {
		List<CheckDetailRecord> checkDetailRecordList = checkDetailRecordDao.searchList(parentId);
		for(CheckDetailRecord record : checkDetailRecordList){
			DecimalFormat df = new DecimalFormat("0.00%");
			if(record.getNum() == 0) {
				record.setRatioStr("0.00%");
			} else {
				double ratio = (double)record.getPassNum() / (double)record.getNum();
				String ratioStr = df.format(ratio);
				record.setRatioStr(ratioStr);
			}
		}
		return checkDetailRecordList;
	}
	
	public long searchCount(Integer month, Integer teamid) {
		String month_str = "";
		if(month != null){
			month_str = month < 10 ? ("0" + month):  Integer.toString(month);
		}
		return checkDetailRecordDao.searchCount(month_str, teamid);
	}
	
	public Workbook save2Excel(List<CheckDetailRecord> checkDetailRecordList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		int rows = checkDetailRecordList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("抽检时间");
		cell = row.createCell(1);
		cell.setCellValue("抽检日期范围");
		cell = row.createCell(2);
		cell.setCellValue("录题小组组长");
		cell = row.createCell(3);
		cell.setCellValue("抽检题目");
		cell = row.createCell(4);
		cell.setCellValue("合格");
		cell = row.createCell(5);
		cell.setCellValue("不合格");
		cell = row.createCell(6);
		cell.setCellValue("抽检合格率");

		for (int i = 1; i <= rows; i++) {
			CheckDetailRecord checkDetailRecord = checkDetailRecordList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);
			cell.setCellValue(DateUtils.formatDate(checkDetailRecord.getCreateTime()));
			cell = row.createCell(1);
			cell.setCellValue(DateUtils.formatDate(checkDetailRecord.getStartTime()) + "--" + DateUtils.formatDate(checkDetailRecord.getEndTime()));
			cell = row.createCell(2);
			TikuTeam team = tikuTeamDao.getTeamById(checkDetailRecord.getTeamId());
			cell.setCellValue(team.getCaptainName());				
			cell = row.createCell(3);
			cell.setCellValue(checkDetailRecord.getNum());
			cell = row.createCell(4);
			cell.setCellValue(checkDetailRecord.getPassNum());
			cell = row.createCell(5);
			cell.setCellValue(checkDetailRecord.getUnpassNum());
			cell = row.createCell(6);
			cell.setCellValue(checkDetailRecord.getRatioStr());
		}

		return workBook;
	}
}
