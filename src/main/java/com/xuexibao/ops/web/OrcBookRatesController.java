package com.xuexibao.ops.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuexibao.ops.model.OrcBookRates;
import com.xuexibao.ops.service.OrcBookRatesService;
import com.xuexibao.ops.service.OrganizationService;
import com.xuexibao.ops.util.DateUtils;


@Controller
@RequestMapping(value = "/book")
public class OrcBookRatesController extends AbstractController {
	
	private static final int limit = 30;

	@Autowired
	protected OrcBookRatesService orcBookRatesService;
	@Autowired
	protected OrganizationService organizationService;


	@RequestMapping(value = "/orcBookRatesList")
	public String orcBookRatesList(HttpServletRequest request, ModelMap model, String name, Integer teamId, String teamName, String isbn, String operator, Integer status,
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
             	long totalNum = orcBookRatesService.searchCount(name, isbn, operator, teamName, teamId, status, startDate, endDate);
					long totalPageNum = totalNum / limit;
					if (totalNum > totalPageNum * limit)
						totalPageNum++;
					if (page >= totalPageNum && totalPageNum != 0)
						page = totalPageNum - 1;
					long start = page * limit;
					List<OrcBookRates> orcBookRatesInfoList = orcBookRatesService.searchList(name, isbn, operator,teamName, teamId, status,  startDate, endDate, start,
							limit);
					model.addAttribute("name", name);
					model.addAttribute("isbn", isbn);
					model.addAttribute("operator", operator);
					model.addAttribute("status", status);
					model.addAttribute("teamName", teamName);
					model.addAttribute("startTime", startTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("orcBookRatesInfoList", orcBookRatesInfoList);
					model.addAttribute("page", page);
					model.addAttribute("totalNum", totalNum);
					model.addAttribute("totalpage", totalPageNum);
					return "book/orcBookRatesList";

	}
	@RequestMapping(value = "/orcBookRatesCaptainList")
	public String orcBookRatesCaptainList(HttpServletRequest request, ModelMap model, String name, Integer teamId, String teamName, String isbn, String operator, Integer status,
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
             	long totalNum = orcBookRatesService.searchCount(name, isbn, operator, teamName, teamId, status, startDate, endDate);
					long totalPageNum = totalNum / limit;
					if (totalNum > totalPageNum * limit)
						totalPageNum++;
					if (page >= totalPageNum && totalPageNum != 0)
						page = totalPageNum - 1;
					long start = page * limit;
					List<OrcBookRates> orcBookRatesInfoList = orcBookRatesService.searchList(name, isbn, operator,teamName, teamId, status,  startDate, endDate, start,
							limit);
					model.addAttribute("name", name);
					model.addAttribute("isbn", isbn);
					model.addAttribute("operator", operator);
					model.addAttribute("status", status);
					model.addAttribute("teamName", teamName);
					model.addAttribute("startTime", startTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("orcBookRatesInfoList", orcBookRatesInfoList);
					model.addAttribute("page", page);
					model.addAttribute("totalNum", totalNum);
					model.addAttribute("totalpage", totalPageNum);
					return "book/orcBookRatesCaptainList";

	}

}
