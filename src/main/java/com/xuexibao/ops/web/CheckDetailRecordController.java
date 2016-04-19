package com.xuexibao.ops.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.model.CheckDetailRecord;
import com.xuexibao.ops.service.CheckDetailRecordService;


@Controller
@RequestMapping(value = "/check")
public class CheckDetailRecordController extends AbstractController {
	
	private static final int limit = 10;
	
	@Autowired
	protected CheckDetailRecordService checkDetailRecordService;
	
	@RequestMapping(value = "/checkDetailRecordList")
	public String checkDetailRecordSearch(HttpServletRequest request, ModelMap model, Integer month, Long page) {
		try{
			page = page == null || page < 0 ? 0 : page;
			
			HttpSession session = request.getSession();
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			
			long totalNum = checkDetailRecordService.searchCount(month, teamid);
			
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			
			List<CheckDetailRecord> checkDetailRecordList = checkDetailRecordService.searchList(month, teamid, 
					page * limit, limit);

			model.addAttribute("month", month);
			model.addAttribute("teamid", teamid);
			model.addAttribute("checkDetailRecordList", checkDetailRecordList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
	    	return "check/tranOpsCheckDetailRecord";
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/checkDetailRecordExport2Excel")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response, Long parentId) {
		try {
			List<CheckDetailRecord> checkDetailRecordList = checkDetailRecordService.searchList(parentId);
			String fileName = "CheckDetailRecord.xls";
			Workbook workBook = checkDetailRecordService.save2Excel(checkDetailRecordList);
	
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
