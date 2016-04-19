package com.xuexibao.ops.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.IDedupGroupSelectedDao;
import com.xuexibao.ops.dao.impl.DedupStatisticsInfoDao;
import com.xuexibao.ops.dto.NodupClickCntDto;
import com.xuexibao.ops.dto.ValidQstnLenDto;
import com.xuexibao.ops.model.DedupStatisticsInfo;
import com.xuexibao.ops.util.DateUtils;

@Service
public class DedupStatisticsInfoService {
	
	@Resource
	IDedupGroupSelectedDao dedupGroupSelectedDao;
	@Resource
	DedupStatisticsInfoDao dedupStatisticsInfoDao;
	
	private static Logger logger = LoggerFactory.getLogger("dupQuestion_check_log");

	public void generateDedupStatistics(Date date) {
		try {
			long startTime = System.currentTimeMillis();
			logger.info("[BEGIN] DedupStatisticsInfoTask.getDailyDedupStatisticsInfo()");
			String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
			List<DedupStatisticsInfo> dedupStatisticList = dedupGroupSelectedDao.getDedupStatistics(dateStr);
			Map<String, DedupStatisticsInfo> dedupStatisticMap = new HashMap<>(dedupStatisticList.size());
			for(DedupStatisticsInfo info : dedupStatisticList) {
				info.setDateStr(dateStr);
				dedupStatisticMap.put(info.getUserKey(), info);
			}
			// 释放内存，让JVM尽早回收
			dedupStatisticList = null;
			List<NodupClickCntDto> nodupClickList = dedupGroupSelectedDao.getNodupClickCnt(dateStr);
			String userKey = null;
			DedupStatisticsInfo info = null;
			for(NodupClickCntDto nodupClickCnt : nodupClickList) {
				userKey = nodupClickCnt.getUserKey();
				if(dedupStatisticMap.containsKey(userKey)) {
					info = dedupStatisticMap.get(userKey);
					info.setNodupClickCnt(nodupClickCnt.getNodupClick());
				} else {
					dedupStatisticMap.put(userKey, new DedupStatisticsInfo(dateStr, userKey, nodupClickCnt.getNodupClick(), new Date()));
				}
			}
			
			List<ValidQstnLenDto> validQstnLenList = dedupGroupSelectedDao.getValidQstnLen(dateStr);
			for(ValidQstnLenDto validQstnLen : validQstnLenList) {
				userKey = validQstnLen.getUserKey();
				if(dedupStatisticMap.containsKey(userKey)) {
					info = dedupStatisticMap.get(userKey);
					info.setValidCnt(validQstnLen.getValidCnt());
				} else {
					dedupStatisticMap.put(userKey, new DedupStatisticsInfo(validQstnLen.getValidCnt(), dateStr, userKey, new Date()));
				}
			}
			dedupStatisticList = new ArrayList<>(dedupStatisticMap.size());
			for(Iterator<DedupStatisticsInfo> iterator = dedupStatisticMap.values().iterator(); iterator.hasNext();) {
				dedupStatisticList.add(iterator.next());
			}
			if(dedupStatisticList.size() > 0)
				dedupStatisticsInfoDao.insertBatch(dedupStatisticList);
			logger.info("[E N D] DedupStatisticsInfoTask.getDailyDedupStatisticsInfo(), 执行{}毫秒", System.currentTimeMillis() - startTime);
		} catch (Exception e) {
			logger.error("GenerateDedupStatistics Error", e);
		}
	}
	
	public Workbook save2Excel(List<DedupStatisticsInfo> dedupStatisticsList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		int rows = dedupStatisticsList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("去重人");
		cell = row.createCell(1);
		cell.setCellValue("去重题组数");
		cell = row.createCell(2);
		cell.setCellValue("去重题备选题数");
		cell = row.createCell(3);
		cell.setCellValue("重复题目选择数");
		cell = row.createCell(4);
		cell.setCellValue("选择无重复组数");
		cell = row.createCell(5);
		cell.setCellValue("有效去重题总数");
		cell = row.createCell(6);
		cell.setCellValue("去重时间");
		for (int i = 1; i <= rows; i++) {
			DedupStatisticsInfo dedupStatistics = dedupStatisticsList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);
			cell.setCellValue(dedupStatistics.getUserKey() != null ? dedupStatistics.getUserKey() : "");
			cell = row.createCell(1);
			cell.setCellValue(dedupStatistics.getOperatCnt() != null ? dedupStatistics.getOperatCnt().toString() : "");
			cell = row.createCell(2);
			cell.setCellValue(dedupStatistics.getAnalyzeCnt() != null ? dedupStatistics.getAnalyzeCnt().toString() : "");
			cell = row.createCell(3);
			cell.setCellValue(dedupStatistics.getClickCnt() != null ? dedupStatistics.getClickCnt().toString() : "");		
			cell = row.createCell(4);
			cell.setCellValue(dedupStatistics.getNodupClickCnt() != null ? dedupStatistics.getNodupClickCnt().toString() : "");
			cell = row.createCell(5);
			cell.setCellValue(dedupStatistics.getValidCnt() != null ? dedupStatistics.getValidCnt().toString() : "");
			cell = row.createCell(6);
			cell.setCellValue(dedupStatistics.getDateStr() != null ? dedupStatistics.getDateStr() : "");
		}

		return workBook;
	}
}	