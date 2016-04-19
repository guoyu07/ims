package com.xuexibao.ops.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.OrcBooks;
import com.xuexibao.ops.service.OrcBooksService;
import com.xuexibao.ops.service.OrganizationService;
import com.xuexibao.ops.util.DateUtils;


@Controller
@RequestMapping(value = "/book")
public class OrcBooksController extends AbstractController {
	
	private static final int limit = 30;

	@Autowired
	protected OrcBooksService orcBooksService;
	@Autowired
	protected OrganizationService organizationService;


	@RequestMapping(value = "/orcBooksList")
	public String orcBooksList(HttpServletRequest request, ModelMap model, String name, Integer teamId, String teamName, String isbn, String operator, Integer status,
			  String startTime, String endTime,
			  Long page) throws Exception {

		page = page == null || page < 0 ? 0 : page;
				if (StringUtils.isNotEmpty(name))
					name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(isbn))
					isbn = new String(isbn.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(operator)){
					operator = new String(operator.getBytes("ISO-8859-1"), "UTF-8");				
			    }
				if (StringUtils.isNotEmpty(teamName)){
					teamName = new String(teamName.getBytes("ISO-8859-1"), "UTF-8");				
			    }
				Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
				Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
             	long totalNum = orcBooksService.searchCount(name, isbn, operator, teamName, teamId, status, startDate, endDate);
					long totalPageNum = totalNum / limit;
					if (totalNum > totalPageNum * limit)
						totalPageNum++;
					if (page >= totalPageNum && totalPageNum != 0)
						page = totalPageNum - 1;
					long start = page * limit;
					List<OrcBooks> orcBooksInfoList = orcBooksService.searchList(name, isbn, operator,teamName, teamId, status,  startDate, endDate, start,
							limit);
					model.addAttribute("name", name);
					model.addAttribute("isbn", isbn);
					model.addAttribute("operator", operator);
					model.addAttribute("status", status);
					model.addAttribute("teamName", teamName);
					model.addAttribute("startTime", startTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("orcBooksInfoList", orcBooksInfoList);
					model.addAttribute("page", page);
					model.addAttribute("totalNum", totalNum);
					model.addAttribute("totalpage", totalPageNum);
					return "book/orcBooksList";
	}
	@RequestMapping(value = "/orcBooksCaptainList")
	public String orcBooksCaptainList(HttpServletRequest request, ModelMap model, String name, Integer teamId, String teamName, String isbn, String operator, Integer status,
			  String startTime, String endTime,
			  Long page) throws Exception {
		HttpSession session = request.getSession();
		Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
		if(teamid != null){
			teamId=teamid;
		}
		page = page == null || page < 0 ? 0 : page;
				if (StringUtils.isNotEmpty(name))
					name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(isbn))
					isbn = new String(isbn.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(operator)){
					operator = new String(operator.getBytes("ISO-8859-1"), "UTF-8");				
			    }
				if (StringUtils.isNotEmpty(teamName)){
					teamName = new String(teamName.getBytes("ISO-8859-1"), "UTF-8");				
			    }
				Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
				Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
             	long totalNum = orcBooksService.searchCount(name, isbn, operator, teamName, teamId, status, startDate, endDate);
					long totalPageNum = totalNum / limit;
					if (totalNum > totalPageNum * limit)
						totalPageNum++;
					if (page >= totalPageNum && totalPageNum != 0)
						page = totalPageNum - 1;
					long start = page * limit;
					List<OrcBooks> orcBooksInfoList = orcBooksService.searchList(name, isbn, operator,teamName, teamId, status,  startDate, endDate, start,
							limit);
					model.addAttribute("name", name);
					model.addAttribute("isbn", isbn);
					model.addAttribute("operator", operator);
					model.addAttribute("status", status);
					model.addAttribute("teamName", teamName);
					model.addAttribute("startTime", startTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("orcBooksInfoList", orcBooksInfoList);
					model.addAttribute("page", page);
					model.addAttribute("totalNum", totalNum);
					model.addAttribute("totalpage", totalPageNum);
					return "book/orcBooksCaptainList";
	}
	/*
	 * 完成识别状态修改--0未完成1完成
	 */
	@RequestMapping(value = "/updateOrcBoosResult")
	public @ResponseBody
	ResponseResult updateOrcBoosResult(HttpServletRequest request, ModelMap model, Long id) {
		try {

			HttpSession session = request.getSession();

			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);

			int updateNum = orcBooksService.updateStatus(id, Integer.valueOf(1), operatorName);
			if (updateNum == 1){		
			    OrcBooks orcBook=orcBooksService.getById(id);
			    Map<String, Object> result = new HashMap<>();
			    if(orcBook != null){			    	
			    	result.put("id", orcBook.getId());
			    	result.put("status", orcBook.getStatus());
			    	result.put("operatorEndtime",DateUtils.formatDate(orcBook.getOperatorEndtime(), "yyyy-MM-dd HH:mm"));					
			    }
				return successJson(result);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "完成识别失败");
	}
	@RequestMapping(value = "/orcBookExport2Excel")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response, String name, Integer teamId, String teamName, String isbn, String operator, Integer status,
			  String startTime, String endTime,
			  Long page) {
		try {
			Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
			Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
			List<OrcBooks> orcBooksInfoList = orcBooksService.searchList(name, isbn, operator,teamName, teamId, status,  startDate, endDate);
			String fileName = "OrcBooK.xls";
			Workbook workBook = orcBooksService.save2Excel(orcBooksInfoList);
	
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
