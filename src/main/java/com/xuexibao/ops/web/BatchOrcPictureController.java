package com.xuexibao.ops.web;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.RecognitionBatchConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.BatchOrcPictureCheckStatus;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.enumeration.OrcPictureCheckTarget;
import com.xuexibao.ops.model.OrcPictureBatch;
import com.xuexibao.ops.model.OrcPictureRecolist;
import com.xuexibao.ops.service.BatchOrcPictureService;
import com.xuexibao.ops.util.DateUtils;
import com.xuexibao.ops.util.FileUtil;
import com.xuexibao.ops.util.RecoJson;


@Controller
@RequestMapping(value = "/batchpicture")
public class BatchOrcPictureController extends AbstractController {
	
	private static Logger logger = LoggerFactory.getLogger("orc_picture_log");
	private static final Long limit = 20L;
	
	@Autowired
	protected BatchOrcPictureService orcPictureService;
	
	
	@RequestMapping(value = "/orcPictureViewSearch")
	public String orcPictureViewSearch(HttpServletRequest request, ModelMap model,
			  String userKey, Integer status,  String startTime, String endTime, Long page , String pictureId, String target, String fileName, String dispalyMode) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(userKey))
				userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			
			HttpSession session = request.getSession();
			
			userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			
			long totalNum = orcPictureService.searchCount(target,pictureId, userKey, status, startDate, endDate, fileName);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;

			List<OrcPictureBatch> orcPictureList = orcPictureService.searchList(target,pictureId, userKey, status, startDate, endDate, page * limit, limit, fileName);
			model.addAttribute("pictureId", pictureId);
			model.addAttribute("fileName", fileName);
			model.addAttribute("userKey", userKey);
			model.addAttribute("status", status);
			model.addAttribute("orcPictureList", orcPictureList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("target", target);
			model.addAttribute("dispalyMode", dispalyMode);
			return "picture/batchOrcPictureViewList";		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping(value = "/orcPicViewAsync")
	public @ResponseBody ResponseResult orcPicViewAsync(HttpServletRequest request, ModelMap model,
			  String userKey, Integer status,  String startTime, String endTime, Long page , String pictureId, String target, String fileName) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(userKey))
				userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			
			HttpSession session = request.getSession();
			
			userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			
			long totalNum = orcPictureService.searchCount(target,pictureId, userKey, status, startDate, endDate, fileName);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;

			List<OrcPictureBatch> orcPictureList = orcPictureService.searchList(target,pictureId, userKey, status, startDate, endDate, page * limit, limit, fileName);
			return successJson(orcPictureList);	
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "获取图片失败");
		}
	}	
	
	//查看详细
	@RequestMapping(value = "/viewOrcPictureById")
	public String viewOrcPictureById(HttpServletRequest request, ModelMap model, Long pictureId,  String userKey, Integer status,  String startTime, String endTime) {
		OrcPictureBatch orcPicture = orcPictureService.getOrcPictureById(pictureId);
		List<OrcPictureRecolist> orcPictureRecolist = orcPictureService.getOrcpictureRecolist(pictureId);
		HttpSession session = request.getSession();
		userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
		String operatorName=(String) session.getAttribute(SessionConstant.USER_NAME);
		Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
		Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
    	if(orcPicture != null) {
    		
    		if(userKey != null && userKey != "")
	  		{
	  			operatorName = userKey;
	  		}
    		String batchId = orcPicture.getBatchId();
    		String target = orcPicture.getTarget();
	  		OrcPictureBatch nextorcPicture = orcPictureService.getNextPicture(target, pictureId, operatorName, status, startDate, endDate, batchId);
			if(nextorcPicture != null){
				Long nextpictureId = nextorcPicture.getId();
				model.addAttribute("nextpictureId", nextpictureId);
			}
			OrcPictureBatch lastorcPicture = orcPictureService.getLastPicture(target, pictureId, operatorName, status, startDate, endDate,batchId);
			if(lastorcPicture != null){
				Long lastpictureId = lastorcPicture.getId();
				model.addAttribute("lastpictureId", lastpictureId);
			}
    		model.addAttribute("pictureId", pictureId);
			model.addAttribute("userKey", userKey);
			model.addAttribute("status", status);
    		model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
    		model.addAttribute("orcPicture", orcPicture);
    		model.addAttribute("orcPictureRecolist", orcPictureRecolist);
			return "picture/batchOrcPictureViewDetail";
    	}
    	return null;
	}
	
	//更新状态
	@RequestMapping(value = "/edit")
	public @ResponseBody ResponseResult edit(HttpServletRequest request) {
		try {
			
			Long pictureId=  Long.parseLong(request.getParameter("pictureId"));
			Integer status= Integer.parseInt(request.getParameter("status"));
			String recoAddInfo= request.getParameter("recoAddInfo");
			if (pictureId == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片id不能为空");
			}
			if (status == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "status状态不能为空");
			}		
			HttpSession session = request.getSession();
			String approvor = (String) session.getAttribute(SessionConstant.USER_NAME);
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			boolean updateSuccess = orcPictureService.editOrcPicture(pictureId, approvor, teamid, status, recoAddInfo);
			if(updateSuccess){
				OrcPictureBatch orcPicture = orcPictureService.getOrcPictureById(pictureId);
				if(orcPicture != null){
			    	return successJson(orcPicture);
			 	}
			}
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "判断识别结果失败");
	}
	
	
	//下一题
	@RequestMapping(value = "/getNextOrc")
	public @ResponseBody ResponseResult getNextOrc(HttpServletRequest request, Long pictureId,  Integer status,  String startTime, String endTime, String batchId, String target, String userkey) {
		try {
 			if (pictureId == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片id不能为空");
			}	
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			
			HttpSession session = request.getSession();
			String operatorName=(String) session.getAttribute(SessionConstant.USER_NAME);
		
	  		if(userkey != null && userkey != "")
	  		{
	  			operatorName = userkey;
	  		}
	  		
	  		OrcPictureBatch orcPicture = orcPictureService.getNextPicture(target, pictureId, operatorName, status, startDate, endDate, batchId);
			if(orcPicture != null)
				  return successJson(orcPicture);
			
			return errorJson(EnumResCode.SERVER_ERROR.value(), "已无下一题");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "获取下一题失败");
	}
	
	//上一题
	@RequestMapping(value = "/getLastOrc")
	public @ResponseBody ResponseResult getLastOrc(HttpServletRequest request, Long pictureId,  Integer status,  String startTime, String endTime,String batchId, String target, String userkey) {
		try {
			if (pictureId == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片id不能为空");
			}	
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			
			HttpSession session = request.getSession();
			String operatorName=(String) session.getAttribute(SessionConstant.USER_NAME);
	  		
	  		if(userkey != null && userkey != "")
	  		{
	  			operatorName = userkey;
	  		}
	  		
	  		OrcPictureBatch orcPicture = orcPictureService.getLastPicture(target, pictureId, operatorName, status, startDate, endDate,batchId);
			if(orcPicture != null)
				  return successJson(orcPicture);			
			return errorJson(EnumResCode.SERVER_ERROR.value(), "已无上一题");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "获取上一题失败");
	}
	
	
	//计算识别率
	@RequestMapping(value = "/computeRecPercent")
	public @ResponseBody ResponseResult computeRecPercent(HttpServletRequest request, String target,String batchId, String userKey,  Integer status, String startTime, String endTime) {
		try {

			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			JSONObject result = new JSONObject();
			
			HttpSession session = request.getSession();
			userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			
	  		List<OrcPictureBatch> orcPicture = orcPictureService.computeRecPercent(target, batchId, userKey, status, startDate, endDate);
			if(orcPicture != null)
			{
				long total=0;
				long success=0;
				long failure=0;
				long noProcess=0;
				long noResult=0;
				double percent=0.0;
				String msgStr="";
				
				for(OrcPictureBatch one :orcPicture)
				{
					switch (one.getStatus()) {
					case 0:
						noProcess+=one.getCountByStatus();
						break;
					case 1:
						success+=one.getCountByStatus();
						break;
					case 2:
					case 3:
					case 4:
					case 5:	
					case 6:	
					case 7:
						failure+=one.getCountByStatus();
						break;
					case 9:
						noResult+=one.getCountByStatus();
						break;							
					default:
						break;
					}
				}
				total = success + failure;

				if(total>0){
					percent =success*1.0/ total;
				}
				msgStr += "识别无结果=" + noResult +"件; ";
				msgStr += "未判断=" + noProcess +"件; ";
				msgStr += "识别正确=" + success +"件; ";
				msgStr += "识别错误=" + failure +"件 ; ";
				msgStr += "成功率=识别正确/(识别正确+识别错误)=" + String.format("%1$.2f%%",percent*100);
				
				result.put("msg", msgStr);
				
				result.put("noResult", noResult);
				result.put("noProcess", noProcess);
				result.put("success", success);
				result.put("failure", failure);
				result.put("percent", String.format("%1$.2f%%",percent*100));
				return successJson(result);				
			}else{
				result.put("msg", "指定条件无数据");				
				return successJson(result);				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			JSONObject result = new JSONObject();
			result.put("msg", "系统错误！");				
			return successJson(result);
		}
	}
	
	
	
	@RequestMapping(value = "/downloadFile")
	public void downloadFile(HttpServletRequest request,  HttpServletResponse response,
			  String userKey, Integer status,  String startTime, String endTime, Long page , String pictureId, String fileName, String target) {
		try {
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);	
			
			if(StringUtils.isEmpty(userKey))
			{
				userKey = operatorName;
			}
			
			long totalNum = orcPictureService.searchCount(target,pictureId, userKey, status, startDate, endDate, fileName);
			
			String exportFileName = "batchReconitionResult.xls";
			Workbook workBook;
			
			if(totalNum <= RecognitionBatchConstant.exportMaxCNT)
			{
				List<OrcPictureBatch> orcPictureList = orcPictureService.searchList(target,pictureId, userKey, status, startDate, endDate, 0L, totalNum, fileName);
				 workBook = orcPictureService.saveExcel(orcPictureList);
			}else{
				//数据量太大，改变检索条件再写文件
				workBook = orcPictureService.save2Excel(RecognitionBatchConstant.exportMaxCNT, totalNum);
			}
			response.setContentType("application/x-msdownload");
			response.setHeader(
					"Content-Disposition",
					new String(("attachment; filename=\"" + exportFileName + "\"")
							.getBytes("GBK"), "ISO-8859-1"));
			workBook.write(response.getOutputStream());
			
		} catch (Exception e) {
			logger.error("download file error!");
			e.printStackTrace();
		}
	}	
	
	 //同图多应用同时识别
	@RequestMapping(value = "/batchRecognition")
	public String batchRecognition(HttpServletRequest request, HttpServletResponse response, ModelMap model, String pictureId, String targets) throws HttpException, IOException {
		InputStream inStream = null;
		try{
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			String fileName = "";
			String originalFileName = "";
			String batchId = Long.toString(new Date().getTime());
			String[]   targetStrings = targets.split(";");
			OrcPictureBatch orcPicture = null;
			
			if(StringUtils.isNotEmpty(pictureId) && targetStrings.length > 0) {
				//取得文件
				String newpictureId=null;				
				orcPicture = orcPictureService.getOrcPictureById(Long.parseLong(pictureId));
				String uploadfile = orcPicture.getOrcPictureUrl();
				
				fileName = FileUtil.getFileName(orcPicture.getOriginalFileName());
				URL url = new URL(uploadfile);
				HttpURLConnection conn = (HttpURLConnection)url.openConnection();
				conn.setRequestMethod("GET");
				conn.setConnectTimeout(5 * 1000);
				
				Integer target=0;
		        BatchOrcPictureCheckStatus status = BatchOrcPictureCheckStatus.URECO;

		        for(String targetString : targetStrings){
		        	
		        	targetString = targetString.replace(";", "");
		        	if(targetString.length() > 0){
		        		target = Integer.parseInt(targetString);
		        	}else{
		        		continue;
		        	}
		        	
		        	switch(OrcPictureCheckTarget.getEnum(target)) {
					case XUEXIBAO:
						inStream = conn.getInputStream();
						File file = RecoJson.copyFile(inStream, fileName,null);						
				        newpictureId = FileUtil.uploadToBatch(file);
						originalFileName = orcPicture.getOriginalFileName();				        
				        file.delete();
						if(StringUtils.isEmpty(newpictureId)){
							status = BatchOrcPictureCheckStatus.ERROR_UPLOAD_FAILURE;
						}
						break;
					case XUEBAJUN:
					case XIAOYUANSOUTI:
					case ZUOYEBANG:
						newpictureId = uploadfile.substring(uploadfile.lastIndexOf("/")+1);
						originalFileName = orcPicture.getOriginalFileName();						
						break;
					default:
						break;
					}
		        	orcPictureService.setUploadInfo(Integer.toString(target), userKey, batchId, uploadfile, newpictureId,originalFileName,status);	        	
		        }

			}
			
			Thread.currentThread();
			Thread.sleep(3000);
			//识别
			orcPictureService.getRecogResultByBatchIdOnly(batchId);
			
    		List<OrcPictureBatch> orcPictureList = null;
    		List<OrcPictureRecolist> orcPictureRecolist = null;
    		orcPictureList = orcPictureService.getBatchRecoResult(batchId);
    		orcPictureList.add(orcPicture);
    		
    		for(OrcPictureBatch orcPictureOne : orcPictureList){
    			if(orcPictureOne.getTarget().equals(Integer.toString(OrcPictureCheckTarget.XUEXIBAO.getId()))){
    				orcPictureRecolist = orcPictureService.getOrcpictureRecolist(orcPictureOne.getId());
    			}
    		}
	    		
    		model.addAttribute("orcPictureList", orcPictureList);
    		model.addAttribute("orcPictureRecolist", orcPictureRecolist);
			return "picture/batchOrcPictureViewDetailMulti";
		}catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/orcPictureUpload")
	public String index() {
			return "picture/batchOrcPictureUpload";		
	}
}
