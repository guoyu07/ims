package com.xuexibao.ops.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuexibao.ops.constant.RoleNameConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.TikuTeam;
import com.xuexibao.ops.service.TikuTeamService;
import com.xuexibao.ops.service.TranOpsService;
import com.xuexibao.ops.service.UserInfoService;

@Controller
@RequestMapping(value = "/tranops")
public class TikuTeamController extends AbstractController {
	
	@Autowired
	protected TikuTeamService tikuTeamService;
	
	@Autowired
	protected TranOpsService tranOpsService;
	
	@Autowired
	protected UserInfoService userInfoService;
	
	private static final int limit = 10;
	
	@RequestMapping(value = "/tikuTeamSearch")
	public String tikuTeamSearch(HttpServletRequest request, ModelMap model,
		   Long teamId, String captain, String transStartDate, String transEndDate,
		   String checkStartDate, String checkEndDate, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(captain))
				captain = new String(captain.getBytes("ISO-8859-1"), "UTF-8");
			long totalNum = tikuTeamService.searchCount(teamId, captain);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;	
			List<TikuTeam> tikuTeamList = tikuTeamService.searchList(teamId, captain, page * limit, limit);
			for(TikuTeam team : tikuTeamList) {
				tikuTeamService.setTeamNum(team, transStartDate, transEndDate, checkStartDate, checkEndDate);
			}		
			model.addAttribute("teamId", teamId);
			model.addAttribute("captain", captain);	
			model.addAttribute("transStartDate", transStartDate);
			model.addAttribute("transEndDate", transEndDate);
			model.addAttribute("checkStartDate", checkStartDate);
			model.addAttribute("checkEndDate", checkEndDate);	
			model.addAttribute("tikuTeamList", tikuTeamList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "tranops/tikuTeamList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping(value = "/tikuTeamInfoSearch")
	public String tikuTeamInfoSearch(HttpServletRequest request, ModelMap model,
		   Long teamId, String role, String captain, String transStartDate, String transEndDate,
		   String checkStartDate, String checkEndDate, Long page) {
		try {
			page = page == null || page < 0 ? 0 : page;
			if(StringUtils.isNotEmpty(captain))
				captain = new String(captain.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(role))
				role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
			long totalNum = tikuTeamService.searchCount(teamId, captain, role);
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;	
			List<TikuTeam> tikuTeamList = tikuTeamService.searchList(teamId, captain, role, page * limit, limit);
			model.addAttribute("teamId", teamId);
			model.addAttribute("captain", captain);
			model.addAttribute("role", role);
			model.addAttribute("transStartDate", transStartDate);
			model.addAttribute("transEndDate", transEndDate);
			model.addAttribute("checkStartDate", checkStartDate);
			model.addAttribute("checkEndDate", checkEndDate);	
			model.addAttribute("tikuTeamList", tikuTeamList);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "tranops/tikuTeamInfoList";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/getAllTikuTeam")
	public @ResponseBody ResponseResult getAllTikuTeam(String role,Long page) {
		try {
			if (StringUtils.isNotEmpty(role)){
				role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
			}else{
			  role = RoleNameConstant.STUDENT;
			}
			page = page == null || page < 0 ? 0 : page;
			long totalNum = tikuTeamService.searchCount();
			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<TikuTeam> tikuTeamList = tikuTeamService.searchList(role, page * limit, limit);
			for(TikuTeam team : tikuTeamList) {
				tikuTeamService.setTeamNum(team);
			}
			return successJson(tikuTeamList);
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "查询异常");
		}
	}
	
	@RequestMapping(value = "/tikuTeamExport2Excel")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response,
			Long teamId, String captain, String transStartDate, String transEndDate,
			String checkStartDate, String checkEndDate) {
		try {
			if(StringUtils.isNotEmpty(captain))
				captain = new String(captain.getBytes("ISO-8859-1"), "UTF-8");
			List<TikuTeam> tikuTeamList = tikuTeamService.searchList(teamId, captain);
			for(TikuTeam team : tikuTeamList) {
				tikuTeamService.setTeamNum(team, transStartDate, transEndDate, checkStartDate, checkEndDate);
			}
			String fileName = "tiku_team.xls";
			Workbook workBook = tikuTeamService.save2Excel(tikuTeamList);
	
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
	
	
	@RequestMapping(value = "/updateTeam")
	public @ResponseBody
	ResponseResult updateTeam(HttpServletRequest request, ModelMap model,
			Integer teamId, String oldCaptain, String newCaptain, String newTeamName) {
		try {
			if (StringUtils.isNotEmpty(oldCaptain))
				oldCaptain = new String(oldCaptain.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(newCaptain))
				newCaptain = new String(newCaptain.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(newTeamName))
				newTeamName = new String(newTeamName.getBytes("ISO-8859-1"), "UTF-8");
			int ret = 0;
			if(newCaptain != null && oldCaptain != null){
				ret = tikuTeamService.updateTeamCaptain(teamId, newCaptain);
				if(ret != 1){
					return errorJson(EnumResCode.SERVER_ERROR.value(), "更新小组失败");
				}
				
				ret = userInfoService.updateUserNotCaptain(oldCaptain);
				if(ret != 1){
					return errorJson(EnumResCode.SERVER_ERROR.value(), "更新小组失败");
				}
				
				ret = userInfoService.updateUserCaptain(teamId, newCaptain);
				if(ret != 1){
					return errorJson(EnumResCode.SERVER_ERROR.value(), "更新小组失败");
				}				
			}
			
			if(newTeamName != null){
				ret = tikuTeamService.updateTeamName(teamId, newTeamName);
				if(ret != 1){
					return errorJson(EnumResCode.SERVER_ERROR.value(), "更新小组失败");
				}
			}
				

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return successJson();
	}
	
	@RequestMapping(value = "/newTeam")
	public @ResponseBody
	ResponseResult newTeam(HttpServletRequest request, ModelMap model,
			String teamName, String captain, String captainName) {
		try {
			if (StringUtils.isNotEmpty(teamName))
				teamName = new String(teamName.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(captain))
				captain = new String(captain.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(captainName))
				captainName = new String(captainName.getBytes("ISO-8859-1"), "UTF-8");
			int ret = 0;		
			TikuTeam tikuTeam = new TikuTeam(null, teamName, captain, captainName);		
			tikuTeam = tikuTeamService.insertTikuTeam(tikuTeam);
			if (tikuTeam == null) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "新建小组失败");			
			}		
			int teamId = tikuTeamService.getIdByTeamName(teamName);
			tikuTeam.setId(teamId);	
			ret = userInfoService.newUserCaptain(teamId, captain);
			if(ret != 1){
				return errorJson(EnumResCode.SERVER_ERROR.value(), "新建小组失败");
			}
			return successJson(tikuTeam);
			
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "新建小组失败");
		}
		
	}

}
