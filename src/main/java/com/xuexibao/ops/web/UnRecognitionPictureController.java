package com.xuexibao.ops.web;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuexibao.ops.model.RecognitionPicture;
import com.xuexibao.ops.service.RecognitionPictureService;
import com.xuexibao.ops.util.DateUtils;

@Controller
@RequestMapping(value = "/recognition")
public class UnRecognitionPictureController extends AbstractController {
	private static Logger logger = LoggerFactory.getLogger("artificial_recognition_log");
	private static final int limit = 30;
	public static final String UPLOAD_PATH = "upload";
	@Autowired
	protected RecognitionPictureService recognitionPictureService;

	@RequestMapping(value = "/unRecognitionPicture")
	public String unRecognitionPictureSearch(ModelMap model, String operater, Integer status, Long id, String startTime, String endTime,
			Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");

			long totalNum = recognitionPictureService.searchCount(id, status, startDate, endDate);
			long totalPageNum = totalNum / limit;
			if (totalNum > totalPageNum * limit)
				totalPageNum++;
			if (page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<RecognitionPicture> unRecognitionList = recognitionPictureService.searchList(id, status, startDate, endDate,
					page * limit, limit);
			model.addAttribute("id", id);
			model.addAttribute("status", status);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("unRecognitionList", unRecognitionList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum); 
			model.addAttribute("totalpage", totalPageNum);
			return "recognition/unRecognitionHistory";
		} catch (Exception e) {
			logger.error("未识别图片页面打开失败", e);
			return null;
		}
	}
}
