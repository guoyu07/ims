package com.xuexibao.ops.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuexibao.ops.model.PictureCheckRecord;
import com.xuexibao.ops.service.PictureCheckRecordService;


@Controller
@RequestMapping(value = "/piccheck")
public class PictureCheckRecordController extends AbstractController {
	
	private static final int limit = 10;
	
	@Autowired
	protected PictureCheckRecordService pictureCheckRecordService;
	
	@RequestMapping(value = "/checkRecordList")
	public String checkRecordSearch(HttpServletRequest request, ModelMap model, Integer month, Long page) {
		try{
			page = page == null || page < 0 ? 0 : page;
			
			long totalNum = pictureCheckRecordService.searchCount(month);
			
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			
			List<PictureCheckRecord> checkRecordList = pictureCheckRecordService.searchList(month, page * limit, limit);

			model.addAttribute("month", month);
			
			model.addAttribute("checkRecordList", checkRecordList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
	    	return "picture/picOpsCheckRecord";
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
