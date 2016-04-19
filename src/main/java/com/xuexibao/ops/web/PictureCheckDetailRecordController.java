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
import com.xuexibao.ops.model.PictureCheckDetailRecord;
import com.xuexibao.ops.service.PictureCheckDetailRecordService;


@Controller
@RequestMapping(value = "/piccheck")
public class PictureCheckDetailRecordController extends AbstractController {
	
	private static final int limit = 10;
	
	@Autowired
	protected PictureCheckDetailRecordService PictureCheckDetailRecordService;
	
	@RequestMapping(value = "/checkDetailRecordList")
	public String checkDetailRecordSearch(HttpServletRequest request, ModelMap model, Integer month, Long page) {
		try{
			page = page == null || page < 0 ? 0 : page;
			
			HttpSession session = request.getSession();
			Integer teamid = (Integer) session.getAttribute(SessionConstant.TEAM_ID);
			
			long totalNum = PictureCheckDetailRecordService.searchCount(month, teamid);
			
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			
			List<PictureCheckDetailRecord> checkDetailRecordList = PictureCheckDetailRecordService.searchList(month, teamid, 
					page * limit, limit);

			model.addAttribute("month", month);
			model.addAttribute("teamid", teamid);
			model.addAttribute("checkDetailRecordList", checkDetailRecordList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
	    	return "picture/picOpsCheckDetailRecord";
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/checkDetailRecordExport2Excel")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response, Long parentId) {
		try {
			List<PictureCheckDetailRecord> checkDetailRecordList = PictureCheckDetailRecordService.searchList(parentId);
			String fileName = "PictureCheckDetailRecord.xls";
			Workbook workBook = PictureCheckDetailRecordService.save2Excel(checkDetailRecordList);
	
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
