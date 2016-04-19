package com.xuexibao.ops.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuexibao.ops.dao.impl.DedupStatisticsInfoDao;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.model.DedupStatisticsInfo;
import com.xuexibao.ops.service.DedupStatisticsInfoService;
import com.xuexibao.ops.util.DateUtils;

/**
 * 
 * @author 赵超群
 * 
 */
@Controller
@RequestMapping(value = "/markDupPic")
public class DedupStatisticsInfoController extends AbstractController {
	
	private static Logger logger = LoggerFactory.getLogger("dupQuestion_check_log");
	
	private static final int limit = 50;
	
	@Resource
	DedupStatisticsInfoService dedupStatisticsInfoService;
	@Resource
	DedupStatisticsInfoDao dedupStatisticsInfoDao;
	
	@RequestMapping(value = "/dedupStatisticsInfo")
	public String getDedupStatisticsInfo(ModelMap model, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			String startDate = DateUtils.formatDate(DateUtils.parseDate(startTime, "yyyy-MM-dd"), "yyyyMMdd");
			String endDate = DateUtils.formatDate(DateUtils.parseDate(endTime, "yyyy-MM-dd"), "yyyyMMdd");
			long totalNum = dedupStatisticsInfoDao.countByDate(startDate, endDate);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<DedupStatisticsInfo> dedupStatisticsList = dedupStatisticsInfoDao.searchByDate(startDate, endDate, page, limit);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("dedupStatisticsList", dedupStatisticsList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "mark/dedupStatisticsInfoList";
		} catch (Exception e) {
			logger.error("查询错误", e);
			return null;
		}
	}
	
	@RequestMapping(value = "/prepareDedupStatisticsInfo")
	public @ResponseBody ResponseResult prepareDedupStatisticsInfo() {
		Calendar calendar = Calendar.getInstance();
		for(int i = 14; i <= 18; i++) {
			calendar.set(Calendar.DAY_OF_MONTH, i);
			dedupStatisticsInfoService.generateDedupStatistics(calendar.getTime());
		}
		return successJson();
	}
	
	@RequestMapping(value = "/exportDedupStatisticsInfo")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response, String startTime, String endTime) {
		try {
			String startDate = DateUtils.formatDate(DateUtils.parseDate(startTime, "yyyy-MM-dd"), "yyyyMMdd");
			String endDate = DateUtils.formatDate(DateUtils.parseDate(endTime, "yyyy-MM-dd"), "yyyyMMdd");
			List<DedupStatisticsInfo> dedupStatisticsList = dedupStatisticsInfoDao.searchByDate(startDate, endDate, null, null);
			String fileName = "去重题目统计.xls";
			Workbook workBook = dedupStatisticsInfoService.save2Excel(dedupStatisticsList);
	
			response.setContentType("application/x-msdownload");
			response.setHeader(
					"Content-Disposition",
					new String(("attachment; filename=\"" + fileName + "\"")
							.getBytes("GBK"), "ISO-8859-1"));
			workBook.write(response.getOutputStream());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
