package com.xuexibao.ops.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.constant.OrcPictureCheckStatusConstant;
import com.xuexibao.ops.constant.OrcPictureStatusConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.enumeration.EnumSubjectType;
import com.xuexibao.ops.enumeration.TranOpsCompleteStatus;
import com.xuexibao.ops.model.Books;
import com.xuexibao.ops.model.OrcAnalysisBydate;
import com.xuexibao.ops.model.OrcPicture;
import com.xuexibao.ops.model.TranOps;
import com.xuexibao.ops.service.BooksService;
import com.xuexibao.ops.service.OrcPictureService;
import com.xuexibao.ops.util.DateUtils;


@Controller
@RequestMapping(value = "/picture")
public class OrcPictureController extends AbstractController {
	
	private static final int limit = 30;
	
	@Autowired
	protected OrcPictureService orcPictureService;
	@Autowired
	protected BooksService booksService;
	
	@RequestMapping(value = "/orcPictureViewSearch")
	public String orcPictureViewSearch(HttpServletRequest request, ModelMap model,
			Long pictureId,  String userKey, Integer status,String bookName,  String startTime, String endTime, Long page ) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(userKey))
				userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			Long bookId=null;
			List<Long> bookIds = new ArrayList<Long>();
			if(StringUtils.isNotEmpty(bookName)){
				bookName = new String(bookName.getBytes("ISO-8859-1"), "UTF-8");
              	List<Books> booksInfoList = booksService.searchList(bookName);
              	if(booksInfoList != null && booksInfoList.size()>0 ){
              		for (Books book:booksInfoList){
              		    bookId= book.getId();	
              		    bookIds.add(bookId);
              		}
              		
              	}else{
              	    bookId=Long.valueOf(-1);
          		    bookIds.add(bookId);
              	}
			}else{
				
      		    bookIds = null;
			}
			
			HttpSession session = request.getSession();
			String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
	  		if(GroupNameConstant.ADMIN.equals(groupName)) {
	  		}else{
	  			userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
	  		}
	  		long totalNum = orcPictureService.searchCount(pictureId, bookIds, userKey, status, startDate, endDate);
		
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
		    List<OrcPicture> orcPictureList = orcPictureService.searchList(pictureId, bookIds, userKey, status, startDate, endDate, page * limit, limit);

			model.addAttribute("pictureId", pictureId);
			model.addAttribute("userKey", userKey);
			model.addAttribute("status", status);
			model.addAttribute("bookId", bookId);
			model.addAttribute("bookName", bookName);
			model.addAttribute("orcPictureList", orcPictureList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
	  		if(GroupNameConstant.ADMIN.equals(groupName)) {
	  			return "picture/orcPictureViewList";	
	  		}else{
	  			return "picture/orcPictureMemberViewList";
	  		}		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping(value = "/orcPictureCount")
	public String orcPictureCount(HttpServletRequest request, ModelMap model,  String startTime, String endTime, Long page ) {
		try {
			page = page == null || page < 0 ? 0 : page;

			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");

			long totalNum = orcPictureService.searchOrcCount( startDate, endDate);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<OrcAnalysisBydate> orcList = orcPictureService.searchListCount( startDate, endDate, page * limit, limit);

			model.addAttribute("orcList", orcList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
	  		return "picture/orcList";	
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/orcBookExport2Excel")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response,  String startTime, String endTime,
			  Long page) {
		try {
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			List<OrcAnalysisBydate> orcList = orcPictureService.saveSearchListCount( startDate, endDate);
            String fileName = "orcList.xls";
			Workbook workBook = orcPictureService.save2Excel(orcList);
	
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
	
	
	@RequestMapping(value = "/orcPictureMemberViewSearch")
	public String orcPictureMemberViewSearch(HttpServletRequest request, ModelMap model,
			Long pictureId,  Integer status, String bookName,  String startTime, String endTime, Long page ) {
		try {
			page = page == null || page < 0 ? 0 : page;
			Long bookId=null;
			List<Long> bookIds = new ArrayList<Long>();
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			if(StringUtils.isNotEmpty(bookName)){
				bookName = new String(bookName.getBytes("ISO-8859-1"), "UTF-8");
              	List<Books> booksInfoList = booksService.searchList(bookName);
              	if(booksInfoList != null && booksInfoList.size()>0){
          		  for (Books book:booksInfoList){
            		    bookId= book.getId();	
            		    bookIds.add(bookId);
            		}
              	}else{
                 	bookId=Long.valueOf(-1);
              		bookIds.add(bookId);
              	}
			}else{
      		    bookIds = null;
			}
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			long totalNum = orcPictureService.searchCount(pictureId, bookIds, userKey, status, startDate, endDate);
			
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
		    List<OrcPicture> orcPictureList = orcPictureService.searchList(pictureId, bookIds, userKey, status, startDate, endDate, page * limit, limit);
					
			model.addAttribute("pictureId", pictureId);
			model.addAttribute("userKey", userKey);
			model.addAttribute("bookId", bookId);
			model.addAttribute("bookName", bookName);
			model.addAttribute("status", status);
			model.addAttribute("orcPictureList", orcPictureList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			return "picture/orcPictureMemberViewList";		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	//查看详细
	@RequestMapping(value = "/viewOrcPictureById")
	public  String viewOrcPictureById(HttpServletRequest request, ModelMap model, Long pictureId,  String userKey, Integer status,  String startTime, String endTime) {
		OrcPicture orcPicture = orcPictureService.getOrcPictureById(pictureId);
    	if(orcPicture != null) {
    		
    		HttpSession session = request.getSession();
	  		String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
	  		String operatorName="";
	  		if(GroupNameConstant.ADMIN.equals(groupName)) {
    			 operatorName="";
	  		}else{
	  			 operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
	  		}
		
			OrcPicture nextorcPicture = orcPictureService.getNextPicture(pictureId, operatorName, status, null, null);
			if(nextorcPicture != null){
				Long nextpictureId = nextorcPicture.getId();
				model.addAttribute("nextpictureId", nextpictureId);
			}
			OrcPicture lastorcPicture = orcPictureService.getLastPicture(pictureId, operatorName, status, null, null);
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
    		//识别错误还未人工录入进入编辑界面
//    		if(orcPicture.getStatus() == OrcPictureCheckStatusConstant.ERROR_UNRECORD){
//    		   return "picture/orcPictureEditDetail";
//    		}
			return "picture/orcPictureViewDetail";
    	}
    	return null;
	}
	
	//识别错误未录入查看详细
	@RequestMapping(value = "/eidtOrcPictureById")
	public String eidtOrcPictureById(ModelMap model, Long pictureId,  String userKey, Integer status,  String startTime, String endTime, String noSelection) {
		OrcPicture orcPicture = orcPictureService.getOrcPictureById(pictureId);
    	if(orcPicture != null) {
    		model.addAttribute("pictureId", pictureId);
			model.addAttribute("userKey", userKey);
			model.addAttribute("status", status);
    		model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
    		model.addAttribute("orcPicture", orcPicture); 
    		//识别错误还未人工录入进入编辑界面
    		if(orcPicture.getStatus() == OrcPictureCheckStatusConstant.ERROR_UNRECORD || orcPicture.getStatus() == OrcPictureCheckStatusConstant.USOLVE_ERROR || orcPicture.getStatus() == OrcPictureCheckStatusConstant.BEST_SOLVE_RIGHT){
        		if( orcPicture.getBookId() != null){
       			 Books book=booksService.getById(orcPicture.getBookId());
       			 if( book != null && book.getBest() == 1){
       				return "picture/orcPictureEditDetail";
       			 } 
       		    }
    			if(noSelection == null){
	    			List<TranOps> otherRecos = orcPictureService.batchRecognition(orcPicture);
	    			if(otherRecos != null && otherRecos.size() >0){
	    				model.addAttribute("otherRecos", otherRecos);
	    				return "picture/orcOtherDetail";
	    			}
    			}
    			return "picture/orcPictureEditDetail";
    	    }
    		return "picture/orcPictureViewDetail";
    	}
    	return null;
	}
	//识别正确，识别错误(未人工录入)
	@RequestMapping(value = "/edit")
	public @ResponseBody ResponseResult edit(HttpServletRequest request, Long pictureId, Integer status) {
		try {
			if (pictureId == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片id不能为空");
			}
			if (status == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "status状态不能为空");
			}		
			HttpSession session = request.getSession();
			String approvor = (String) session.getAttribute(SessionConstant.USER_NAME);
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			boolean updateSuccess = orcPictureService.editOrcPicture(pictureId, approvor, teamid, status, null);
			if(updateSuccess) {
				OrcPicture orcPicture = orcPictureService.getOrcPictureById(pictureId);
				if(orcPicture != null) {
			    	return successJson(orcPicture);
			 	}
			}
			  
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "判断识别结果失败");
	}
	//识别错误，进行人工录入
	@RequestMapping(value = "/addTranOpsOrc")
	public @ResponseBody ResponseResult addTranOpsOrc(HttpServletRequest request, Long pictureId, String pictureUrl,
			Integer complete, String selectContent , String[] selectOptionArray ,String selectOption, Integer subject, Integer target, Integer realType, 
			Integer realLearnPhase, String content, String latex, String answer, String answerLatex, String solution) {
		try {
			if (pictureId == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片id不能为空");
			}
			if (complete == null || subject == null || realType == null || realLearnPhase == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
			}
			if (TranOpsCompleteStatus.NOT_COMPLETE.getId() == complete) {
				if (StringUtils.isEmpty(content) || StringUtils.isEmpty(latex)) {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
				}
			} else {
				if (StringUtils.isEmpty(content) || StringUtils.isEmpty(latex) || StringUtils.isEmpty(answer) || StringUtils.isEmpty(solution)) {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "参数异常");
				}
				if(realType == EnumSubjectType.RADIO.getId() || realType == EnumSubjectType.CHECK.getId()){
					if(StringUtils.isEmpty(selectContent) || selectOptionArray.length <= 0){
						return errorJson(EnumResCode.SERVER_ERROR.value(), "单选，多选,参数异常");
					}
				}
			}
			if(target == null){
				target = 0;
			}
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			if(StringUtils.isNotEmpty(content))
				content = content.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(latex))
				latex = latex.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(answer))
				answer = answer.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(answerLatex))
				answerLatex = answerLatex.replaceAll("'&nbsp';", "&nbsp");
			if(StringUtils.isNotEmpty(solution))
				solution = solution.replaceAll("'&nbsp';", "&nbsp");

			if(StringUtils.isNotEmpty(selectContent))
				selectContent = selectContent.replaceAll("'&nbsp';", "&nbsp");
			if( selectOptionArray != null && selectOptionArray.length > 0) {
				List<String> options =new ArrayList<String>();
				for(String option : selectOptionArray) {
					option = URLDecoder.decode(option, "UTF-8");
					options.add(option);
				}			
				selectOption = JSONArray.toJSONString(options);
			}
			
			TranOps tranOps = new TranOps(complete, subject, target, realType, realLearnPhase, content, latex, answer, answerLatex, solution, 
					operatorName, teamid, new Date() , pictureId, pictureUrl, OrcPictureStatusConstant.ORC, selectContent, selectOption);

			boolean updateSuccess = orcPictureService.editOrcPicture(pictureId, operatorName, teamid, OrcPictureCheckStatusConstant.ERROR_RECORD, tranOps);
			if(updateSuccess) return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "人工录题失败");
	}
	
	//下一题
	
	@RequestMapping(value = "/getNextOrc")
	public @ResponseBody ResponseResult getNextOrc(HttpServletRequest request, Long pictureId,  Integer status,  String startTime, String endTime) {
		try {
 			if (pictureId == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片id不能为空");
			}	
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			
			HttpSession session = request.getSession();
	  		String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
	  		String operatorName="";
	  		if(GroupNameConstant.ADMIN.equals(groupName)) {
    			 operatorName="";
	  		}else{
	  			 operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
	  		}
		
			OrcPicture orcPicture = orcPictureService.getNextPicture(pictureId, operatorName, status, startDate, endDate);
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
	public @ResponseBody ResponseResult getLastOrc(HttpServletRequest request, Long pictureId,  Integer status,  String startTime, String endTime) {
		try {
			if (pictureId == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "图片id不能为空");
			}	
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			
			HttpSession session = request.getSession();
	  		String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
			String operatorName="";
	  		if(GroupNameConstant.ADMIN.equals(groupName)) {
    			 operatorName="";
	  		}else{
	  			 operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
	  		}
			OrcPicture orcPicture = orcPictureService.getLastPicture(pictureId, operatorName, status, startDate, endDate);
			if(orcPicture != null)
				  return successJson(orcPicture);			
			return errorJson(EnumResCode.SERVER_ERROR.value(), "已无上一题");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "获取上一题失败");
	}
	
}
