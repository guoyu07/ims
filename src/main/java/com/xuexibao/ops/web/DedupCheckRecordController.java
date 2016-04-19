package com.xuexibao.ops.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuexibao.ops.model.DedupCheckList;
import com.xuexibao.ops.service.DedupCheckDetailRecordService;


@Controller
@RequestMapping(value = "/dedup")
public class DedupCheckRecordController extends AbstractController {
	
	private static final int limit = 10;
	
	@Autowired
	protected DedupCheckDetailRecordService dedupCheckDetailRecordService;
	
	@RequestMapping(value = "/checkRecordList")//去重抽检生成列表--以包显示列表
	public String checkRecordSearch(HttpServletRequest request, ModelMap model, Integer month,Integer status, String userKey ,String name, Long page) {
		try{
			page = page == null || page < 0 ? 0 : page;
			if (StringUtils.isNotEmpty(userKey))
				userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(name))
				name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		
			long totalNum = dedupCheckDetailRecordService.searchCount(status,userKey,name);
			
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			
			List<DedupCheckList> checkRecordList = dedupCheckDetailRecordService.searchList(status,userKey,name, page * limit, limit);
	
			model.addAttribute("name", name);
			model.addAttribute("userKey", userKey);
			model.addAttribute("status", status);
			model.addAttribute("checkRecordList", checkRecordList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
	    	return "dedup/dedupCheckRecord";
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
