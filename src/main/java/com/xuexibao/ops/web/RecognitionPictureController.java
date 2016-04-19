package com.xuexibao.ops.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.RecognitionPicture;
import com.xuexibao.ops.service.RecognitionPictureService;
import com.xuexibao.ops.service.UserInfoService;

@Controller
@RequestMapping(value = "/recognition")
public class RecognitionPictureController extends AbstractController {
	
	private static Logger logger = LoggerFactory.getLogger("artificial_recognition_log");

	private static final int limit = 10;
	@Autowired
	protected RecognitionPictureService recognitionPictureService;
	@Autowired
	protected UserInfoService userInfoService;

	@RequestMapping(value = "/selectUnRecList")
	public @ResponseBody
	ResponseResult obtainUnRecList(HttpServletRequest request, HttpServletResponse response, String fileName) throws IOException {
		try {
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			List<RecognitionPicture> recognitionList = recognitionPictureService.selectUnRecList(operatorName, limit);
			return successJson(recognitionList);
		} catch (Exception e) {
			logger.error("获取图片失败", e);
			return errorJson(EnumResCode.SERVER_ERROR.value(), "获取图片失败");
		}
	}

	@RequestMapping(value = "/obtainRemainPictureList")
	public @ResponseBody
	ResponseResult obtainRemainPictureList(HttpServletRequest request, ModelMap model) {
		try {
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
//			UserInfo userInfo = userInfoService.getOneUserInfoByKey(operatorName);
//			if(!GroupNameConstant.RECOGNITION.equals(userInfo.getGroupname())){
//				return errorJson(EnumResCode.SERVER_ERROR.value(), "非识别用户无法获取图片");
//			}
			long startTime = System.currentTimeMillis();
			List<RecognitionPicture> remainPictureList = recognitionPictureService.obtainRemainPictureList(operatorName, limit);
			logger.info("lock外：{}", System.currentTimeMillis() - startTime);
			return successJson(remainPictureList);
		} catch (Exception e) {
			logger.error("获取图片失败", e);
			return errorJson(EnumResCode.SERVER_ERROR.value(), "获取图片失败");
		}
	}

	@RequestMapping(value = "/startRecognitionPicture")
	public String startRecognitionPicture(ModelMap model, HttpServletRequest request) {
		try {
			// Long page = 1L;
			// HttpSession session = request.getSession();
			// String operatorName = (String)
			// session.getAttribute(SessionConstant.USER_NAME);
			// List<RecognitionPicture> RemainPictureList =
			// recognitionPictureService.obtainRemainPictureList(operatorName,
			// page, limit);
			// model.addAttribute("RemainPictureList", RemainPictureList);
			return "recognition/picRecognition";
		} catch (Exception e) {
			logger.error("图片识别页面打开失败", e);
			return null;
		}
	}
}
