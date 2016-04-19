package com.xuexibao.ops.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuexibao.ops.model.RecognitionAnalysisBydate;
import com.xuexibao.ops.service.RecognitionAnalysisService;
import com.xuexibao.ops.service.RecognitionPictureService;
import com.xuexibao.ops.util.DateUtils;

@Controller
@RequestMapping(value = "/recognition")
public class RecognitionAnalysisController extends AbstractController {
	private static Logger logger = LoggerFactory.getLogger("artificial_recognition_log");
	private static final int limit = 30;
	public static final String UPLOAD_PATH = "upload";
	@Autowired
	protected RecognitionAnalysisService recognitionAnalysisService;
	@Autowired
	protected RecognitionPictureService recognitionPictureService;
	
	@RequestMapping(value = "/recognitionAnalysis")
	public String recognitionAnalysisSearch(ModelMap model, String operator, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			long totalNum = recognitionPictureService.searchUserCount();
			long totalPageNum = totalNum / limit;
			if (totalNum > totalPageNum * limit)
				totalPageNum++;
			if (page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<RecognitionAnalysisBydate> recognitionList = recognitionPictureService.searchCountList(operator, startDate, endDate, page * limit, limit);
			totalNum = recognitionList.size();
			model.addAttribute("operator", operator);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("recognitionList", recognitionList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "recognition/recognitionAnalysis";
		} catch (Exception e) {
			logger.error("用户识别统计页面打开失败", e);
			return null;
		}
	}
	
	@RequestMapping(value = "/recognitionAnalysisExport2Excel")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response, String operator, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			List<RecognitionAnalysisBydate> recognitionList = recognitionPictureService.searchCountList(operator, startDate, endDate);
			String fileName = "startTime"+startTime+"__"+"endTime"+endTime+".xls";
			Workbook workBook = recognitionPictureService.save2Excel(recognitionList);
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition",new String(("attachment; filename=\"" + fileName + "\"").getBytes("GBK"), "ISO-8859-1"));
			workBook.write(response.getOutputStream());
		} catch (UnsupportedEncodingException e) {
			logger.error("导出统计结果失败", e);
		} catch (IOException e) {
			logger.error("导出统计结果失败", e);
		}
	}
}
