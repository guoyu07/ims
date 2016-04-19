package com.xuexibao.ops.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.RecognitionHistory;
import com.xuexibao.ops.service.RecognitionHistoryService;
import com.xuexibao.ops.service.RecognitionPictureService;
import com.xuexibao.ops.service.UserInfoService;
import com.xuexibao.ops.util.DateUtils;

@Controller
@RequestMapping(value = "/recognition")
public class RecognitionHistoryController extends AbstractController {
	private static Logger logger = LoggerFactory.getLogger("artificial_recognition_log");
	private static final int limit = 30;
	public static final String UPLOAD_PATH = "upload";
//	public static final String DOWNLOAD_PATH = "download";
	@Autowired
	protected RecognitionHistoryService recognitionHistoryService;
	@Autowired
	protected RecognitionPictureService recognitionPictureService;
	@Autowired
	protected UserInfoService userInfoService;

	@RequestMapping(value = "/recognitionHistory")
	public String recognitionHistorySearch(ModelMap model, String operator, Long fileId, String startTime, String endTime, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Date today = calendar.getTime();
			Date  tomorrow = org.apache.commons.lang3.time.DateUtils.addDays(today, 1);
			long totalNum = recognitionHistoryService.searchCount(operator, fileId, startDate, endDate);
			long totalPageNum = totalNum / limit;
			if (totalNum > totalPageNum * limit)
				totalPageNum++;
			if (page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<RecognitionHistory> recognitionList = recognitionHistoryService.searchList(operator, fileId, startDate, endDate, page * limit, limit);
			Long totalRecognizedNum = null;
			totalRecognizedNum = recognitionPictureService.CountTotalRecognizedNum();
			Long todayRecognitionAmount = null;
			todayRecognitionAmount = recognitionPictureService.CountTodayNum(operator, today, tomorrow);
			Long remainRecognitionAmount = null;
			remainRecognitionAmount = recognitionPictureService.CountRemainNum(operator, startDate);

			model.addAttribute("operator", operator);
			model.addAttribute("fileId", fileId);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("recognitionList", recognitionList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			model.addAttribute("todayRecognitionAmount", todayRecognitionAmount);
			model.addAttribute("remainRecognitionAmount", remainRecognitionAmount);
			model.addAttribute("totalRecognizedNum", totalRecognizedNum);
			return "recognition/recognitionHistory";
		} catch (Exception e) {
			logger.error("识别结果流水页面打开失败", e);
			return null;
		}
	}

	@RequestMapping(value = "/recognitionList")
	public String recognitionListSearch(HttpServletRequest request, ModelMap model, Long fileId, String startTime, String endTime, Long page) {
		try {
			HttpSession session = request.getSession();
			String operator = (String) session.getAttribute(SessionConstant.USER_NAME);
			page = page == null || page < 0 ? 0 : page;
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			long totalNum = recognitionHistoryService.searchCount(operator, fileId, startDate, endDate);
			long totalPageNum = totalNum / limit;
			if (totalNum > totalPageNum * limit)
				totalPageNum++;
			if (page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<RecognitionHistory> recognitionList = recognitionHistoryService.searchList(operator, fileId, startDate, endDate, page * limit, limit);
			// model.addAttribute("operator", operator);
			model.addAttribute("fileId", fileId);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("recognitionList", recognitionList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "recognition/recognitionList";
		} catch (Exception e) {
			logger.error("识别列表页面打开失败", e);
			return null;
		}
	}

	@RequestMapping(value = "/updateRecognitionList")
	public @ResponseBody
	ResponseResult updateRecognitionList(HttpServletRequest request, String fileIds, String results, String status) throws IOException {
		try {
			String rootPath = request.getSession().getServletContext().getRealPath("");
			if (StringUtils.isNotEmpty(results))
				results = new String(results.getBytes("ISO-8859-1"), "UTF-8");
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);

			String[] fileIdArray = fileIds.split(",");
			Long[] picIds = new Long[fileIdArray.length];
			for (int i = 0; i < fileIdArray.length; i++) {
				if (StringUtils.isEmpty(fileIdArray[i]) || !StringUtils.isNumeric(fileIdArray[i]))
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
				picIds[i] = Long.valueOf(fileIdArray[i]);
			}
			// 解析图片状态
			String[] statusArray = status.split(",");
			Integer[] picStatus = new Integer[statusArray.length];
			for (int i = 0; i < statusArray.length; i++) {
				if (StringUtils.isEmpty(statusArray[i]))
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
				picStatus[i] = Integer.valueOf(statusArray[i]);
			}
			// 解析识别结果
			String[] resultArray = results.split(",");
			resultArray = resultArray.length < picIds.length ? Arrays.copyOf(resultArray, picIds.length) : resultArray;
			for (int i = 0; i < resultArray.length; i++) {
				if (picStatus[i] == 1 && (StringUtils.isEmpty(resultArray[i]) || resultArray[i].length() > 1))
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			// UserInfo userInfo =
			// userInfoService.getOneUserInfoByKey(operatorName);
			// if(GroupNameConstant.RECOGNITION.equals(userInfo.getGroupname())){
			// }else{
			// return errorJson(EnumResCode.SERVER_ERROR.value(), "非识别用户不能提交");
			// }
			recognitionPictureService.updatePicture(operatorName, picIds, resultArray, picStatus, rootPath);
			return successJson();
		} catch (Exception e) {
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			logger.info("保存图片出错,operator={}:", operatorName);
//			logger.error("保存图片出错", e);
			return errorJson(EnumResCode.SERVER_ERROR.value(), "保存图片出错");
		}
	}

	@RequestMapping(value = "/addPic")
	public @ResponseBody
	ResponseResult importPic(HttpServletRequest request, HttpServletResponse response, MultipartFile file) throws IOException {
		try {
			logger.info("后台开始接收数据：{}",DateUtils.formatDate(new Date()));
			if (file == null)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "本地文件为空，请重新上传");
			String fileName = file.getOriginalFilename();
			File filePath = new File(request.getSession().getServletContext()
					.getRealPath(File.separator + UPLOAD_PATH + File.separator + DateUtils.formatDate(new Date(), "yyyy-MM-dd"))
					+ File.separator + fileName.substring(0, fileName.lastIndexOf(".")));
			if (filePath.exists()) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "您已经上传过该文件，请勿重复上传");
			}
			if (!".zip".equals(fileName.substring(fileName.lastIndexOf(".")))) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "文件类型错误，请选择正确的文件");
			}
			response.setHeader("Access-Control-Allow-Origin", "*");
			InputStream is = file.getInputStream();
			String realPath = request.getSession().getServletContext().getRealPath(File.separator + UPLOAD_PATH);
			recognitionPictureService.DecompressAndSave(is, fileName, realPath);
			return successJson();
		} catch (Exception e) {
			logger.error("读取文件失败", e);
			return errorJson(EnumResCode.SERVER_ERROR.value(), "读取文件失败，请重新上传");
		}
	}

}
