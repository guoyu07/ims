package com.xuexibao.ops.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
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

import com.xuexibao.ops.util.xls.Column;
import com.xuexibao.ops.util.xls.IExcelBuilder;
import com.xuexibao.ops.util.xls.IExcelSheetBuilder;
import com.xuexibao.ops.util.xls.XlsxBuilder;
import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.constant.RoleNameConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.TikuTeam;
import com.xuexibao.ops.model.UserInfo;
import com.xuexibao.ops.service.RecognitionUserInfoService;
import com.xuexibao.ops.service.TikuTeamService;
import com.xuexibao.ops.service.UserInfoService;
import com.xuexibao.ops.util.DateUtils;
import com.xuexibao.ops.util.Md5PasswordEncoder;
import com.xuexibao.ops.util.ParamUtil;

/**
 * @author liujun
 *2015年6月29日
 */
@Controller
@RequestMapping(value = "/user")
public class UserController extends AbstractController {
	private static final int limit = 30;
	/**
	 * 导出最多支持两万条
	 */
	private static final int TOTAL_LIMIT = 20000;
	private int pageNum = 0;
	private long totalNum;
	private int totalPageNum;
	
	private static final String passwordKey = "123456";

	@Autowired
	protected UserInfoService userInfoService;
	@Autowired
	protected RecognitionUserInfoService recognitionUserInfoService;
	@Autowired
	protected TikuTeamService tikuTeamService;
	/*
	 * 获取可选择组长的用户列表
	 */
	@RequestMapping(value = "/getCanCaptainUsersList")
	public @ResponseBody
	ResponseResult getCanCaptainUsersList() {
		try {
			List<UserInfo> userInfos = userInfoService.getCanCaptainList();
			List<Map<String, Object>> result = new ArrayList<>();
			if (userInfos != null) {
				for (UserInfo userInfo : userInfos) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", userInfo.getId());
					map.put("key", userInfo.getUserKey());
					map.put("name", userInfo.getUserName());
					result.add(map);
				}
				return successJson(result);
			}
			return successJson(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "获取组列表失败！");
	}

	/*
	 * 获取可选择组长的用户列表
	 */
	@RequestMapping(value = "/getUsersListByTeamId")
	public @ResponseBody
	ResponseResult getUsersListByTeamId(Long teamId) {
		try {
			List<UserInfo> userInfos = userInfoService.getUsersListByTeamId(teamId);
			List<Map<String, Object>> result = new ArrayList<>();
			if (userInfos != null) {
				for (UserInfo userInfo : userInfos) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", userInfo.getId());
					map.put("key", userInfo.getUserKey());
					map.put("name", userInfo.getUserName());
					result.add(map);
				}
				return successJson(result);
			}
			return successJson(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "获取组列表失败！");
	}

	@RequestMapping(value = "/userInfoList")
	public String userInfoList(HttpServletRequest request, ModelMap model, String userKey, String role, String groupName, String teamId,
			String status, String startTime, String endTime, Long page) throws Exception {
		HttpSession session = request.getSession();
		String groupname = "";
		String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
		if (StringUtils.isNotEmpty((String) session.getAttribute(SessionConstant.GROUP_NAME))) {
			groupname = (String) session.getAttribute(SessionConstant.GROUP_NAME);
		}
		UserInfo userinfo = null;
		if (StringUtils.isNotEmpty(operatorName)) {
			userinfo = userInfoService.getUserInfoByKey(operatorName);
		}
		try {
			if (userinfo != null) {
				page = page == null || page < 0 ? 0 : page;
				if (StringUtils.isNotEmpty(userKey))
					userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(role))
					role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(groupName))
					groupName = new String(groupName.getBytes("ISO-8859-1"), "UTF-8");
				Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
				Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
				if (groupname.equals(GroupNameConstant.ADMIN)) {
					long totalNum = userInfoService.searchCount(userKey, role, groupName, teamId, status, startDate, endDate);
					long totalPageNum = totalNum / limit;
					if (totalNum > totalPageNum * limit)
						totalPageNum++;
					if (page >= totalPageNum && totalPageNum != 0)
						page = totalPageNum - 1;
					long start = page * limit;
					List<UserInfo> userInfoList = userInfoService.searchList(userKey, role, groupName, teamId, status, startDate, endDate, start,
							limit);
					model.addAttribute("userKey", userKey);
					model.addAttribute("role", role);
					model.addAttribute("teamId", teamId);
					model.addAttribute("groupName", groupName);
					model.addAttribute("status", status);
					model.addAttribute("startTime", startTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("userInfoList", userInfoList);
					model.addAttribute("page", page);
					model.addAttribute("totalNum", totalNum);
					model.addAttribute("totalpage", totalPageNum);
					return "management/userInfoList";
				}
			}
			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/userBankInfoExport2Excel")
	public void export2Excel(HttpServletRequest request, HttpServletResponse response) {
		try {
			//List<UserInfo> userInfoList = userInfoService.searchList();
			String fileName = "用户信息列表.xls";
			//Workbook workBook = userInfoService.save2Excel_BankInfo(userInfoList);
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", new String(("attachment; filename=\"" + fileName + "\"").getBytes("GBK"), "ISO-8859-1"));
			IExcelBuilder excelBuilder = XlsxBuilder.newExcel(response.getOutputStream());
			IExcelSheetBuilder sheetBuilder = excelBuilder.newSheet("用户信息列表");
			// 输出excel表头
			sheetBuilder = sheetBuilder.appendHeader(
					Column.column("账号"),
					Column.column("手机号"), 
					Column.column("姓名"),
					Column.column("身份证号"), 
					Column.column("开户银行"),
					Column.column("开卡地"), 
					Column.column("支行名称"),
					Column.column("银行卡号"));
			final IExcelSheetBuilder finalSheetBuilder = sheetBuilder;
			long totalNum = userInfoService.searchCount(null, null, null, null, null, null, null);
           
			int one = 500;	    
			int countUserDev = (int)totalNum / one;	//每500条记录一页
			if (totalNum % one > 0) countUserDev++;
			long id = 0L;
			for (int i = 0; i < countUserDev; i++) {
				//0-1000 查询
				List<UserInfo> limitList = userInfoService.searchLimitList(id, one);
				id = limitList.get(limitList.size() - 1).getId();
				for (UserInfo userInfo : limitList) {
					// 将当前统计记录输出到Excel中
					List<Object> result = new ArrayList<Object>();
					result.add(userInfo.getUserKey());
					result.add(userInfo.getUserMobile());
					result.add(userInfo.getUserName());
					result.add(userInfo.getIdNumber());
					result.add(userInfo.getBank());
					result.add(userInfo.getProvince()+" "+userInfo.getCity() +" " +userInfo.getCounty());
					result.add(userInfo.getBankSubbranch());
					result.add(userInfo.getBankCard());
					finalSheetBuilder.appendContent(result);
				}
			}
			excelBuilder.flushAndClose();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/userBankInfoList")
	public String userBankInfoList(HttpServletRequest request, ModelMap model, String userKey, String role, String groupName, String teamId,
			String status, String startTime, String endTime, Long page) throws Exception {
		HttpSession session = request.getSession();
		String groupname = "";
		String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
		if (StringUtils.isNotEmpty((String) session.getAttribute(SessionConstant.GROUP_NAME))) {
			groupname = (String) session.getAttribute(SessionConstant.GROUP_NAME);
		}
		UserInfo userinfo = null;
		if (StringUtils.isNotEmpty(operatorName)) {
			userinfo = userInfoService.getUserInfoByKey(operatorName);
		}
		try {
			if (userinfo != null) {
				page = page == null || page < 0 ? 0 : page;
				if (StringUtils.isNotEmpty(userKey))
					userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(role))
					role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(groupName))
					groupName = new String(groupName.getBytes("ISO-8859-1"), "UTF-8");
				Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
				Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
				if (groupname.equals(GroupNameConstant.ADMIN)) {
					long totalNum = userInfoService.searchCount(userKey, role, groupName, teamId, status, startDate, endDate);
					long totalPageNum = totalNum / limit;
					if (totalNum > totalPageNum * limit)
						totalPageNum++;
					if (page >= totalPageNum && totalPageNum != 0)
						page = totalPageNum - 1;
					long start = page * limit;
					List<UserInfo> userInfoList = userInfoService.searchList(userKey, role, groupName, teamId, status, startDate, endDate, start,
							limit);
					model.addAttribute("userKey", userKey);
					model.addAttribute("role", role);
					model.addAttribute("teamId", teamId);
					model.addAttribute("groupName", groupName);
					model.addAttribute("status", status);
					model.addAttribute("startTime", startTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("userInfoList", userInfoList);
					model.addAttribute("page", page);
					model.addAttribute("totalNum", totalNum);
					model.addAttribute("totalpage", totalPageNum);
					return "management/userBankInfoList";
				}
			}
			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/recognitionUserInfoList")
	public String recognitionUserInfoList(HttpServletRequest request, ModelMap model, String userKey, String role, String groupName, String teamId,
			String status, String startTime, String endTime, Long page) throws Exception {
		HttpSession session = request.getSession();
		String groupname = "";
		String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
		if (StringUtils.isNotEmpty((String) session.getAttribute(SessionConstant.GROUP_NAME))) {
			groupname = (String) session.getAttribute(SessionConstant.GROUP_NAME);
		}
		UserInfo userinfo = null;
		if (StringUtils.isNotEmpty(operatorName)) {
			userinfo = recognitionUserInfoService.getUserInfoByKey(operatorName);
		}
		try {
			if (userinfo != null) {
				page = page == null || page < 0 ? 0 : page;
				if (StringUtils.isNotEmpty(userKey))
					userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(role))
					role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(groupName))
					groupName = new String(groupName.getBytes("ISO-8859-1"), "UTF-8");
				Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
				Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
				if (groupname.equals(GroupNameConstant.ADMIN)) {
					long totalNum = recognitionUserInfoService.searchCount(userKey, role, groupName, teamId, status, startDate, endDate);
					long totalPageNum = totalNum / limit;
					if (totalNum > totalPageNum * limit)
						totalPageNum++;
					if (page >= totalPageNum && totalPageNum != 0)
						page = totalPageNum - 1;
					long start = page * limit;
					List<UserInfo> userInfoList = recognitionUserInfoService.searchList(userKey, role, groupName, teamId, status, startDate, endDate,
							start, limit);
					model.addAttribute("userKey", userKey);
					model.addAttribute("role", role);
					model.addAttribute("groupName", groupName);
					model.addAttribute("status", status);
					model.addAttribute("startTime", startTime);
					model.addAttribute("endTime", endTime);
					model.addAttribute("userInfoList", userInfoList);
					model.addAttribute("page", page);
					model.addAttribute("totalNum", totalNum);
					model.addAttribute("totalpage", totalPageNum);
					return "recUserManagement/userInfoList";
				}
			}
			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 审核获取成员列表
	 */
	@RequestMapping(value = "/userTeamInfoList")
	public String userTeamInfoList(HttpServletRequest request, ModelMap model, String userKey, String role, String groupName, String teamId,
			String status, String startTime, String endTime, Long page) throws Exception {
		HttpSession session = request.getSession();
		String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
		UserInfo userinfo = null;
		if (StringUtils.isNotEmpty(operatorName)) {
			userinfo = userInfoService.getUserInfoByKey(operatorName);
		}
		try {
			if (userinfo != null) {
				page = page == null || page < 0 ? 0 : page;
				if (StringUtils.isNotEmpty(userKey))
					userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(role))
					role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(groupName))
					groupName = new String(groupName.getBytes("ISO-8859-1"), "UTF-8");
				Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
				Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
				long totalNum = userInfoService.searchCount(userKey, role, groupName, userinfo.getTeamId().toString(), status, startDate, endDate);
				long totalPageNum = totalNum / limit;
				if (totalNum > totalPageNum * limit)
					totalPageNum++;
				if (page >= totalPageNum && totalPageNum != 0)
					page = totalPageNum - 1;
				long start = page * limit;
				List<UserInfo> userInfoList = userInfoService.searchList(userKey, role, groupName, userinfo.getTeamId().toString(), status,
						startDate, endDate, start, limit);
				model.addAttribute("userKey", userKey);
				model.addAttribute("role", role);
				model.addAttribute("teamId", teamId);
				model.addAttribute("groupName", groupName);
				model.addAttribute("status", status);
				model.addAttribute("startTime", startTime);
				model.addAttribute("endTime", endTime);
				model.addAttribute("userInfoList", userInfoList);
				model.addAttribute("page", page);
				model.addAttribute("totalNum", totalNum);
				model.addAttribute("totalpage", totalPageNum);
				return "management/userTeamInfoList";
			}
			return "index";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * 获取小组列表
	 */
	@RequestMapping(value = "/getTikuTeamList")
	public @ResponseBody
	ResponseResult getTikuTeamList(String role) {
		try {
		  if (StringUtils.isNotEmpty(role))
		    role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
			List<TikuTeam> tikuTeams = tikuTeamService.getTikuTeamList(role);
			List<Map<String, Object>> result = new ArrayList<>();
			if (tikuTeams != null) {
				for (TikuTeam team : tikuTeams) {
					Map<String, Object> map = new HashMap<>();
					map.put("id", team.getId());
					map.put("name", team.getName());
					result.add(map);
				}
				return successJson(result);
			}
			return successJson(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "获取组列表失败！");
	}

	@RequestMapping(value = "/tikuTeamListExport2Excel")
	public void tikuTeamListExport2Excel(HttpServletRequest request, HttpServletResponse response, String userKey, String role, String groupName,
			String teamId, String status) {
		HttpSession session = request.getSession();
		String groupname = "";
		String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
		if (StringUtils.isNotEmpty((String) session.getAttribute(SessionConstant.GROUP_NAME))) {
			groupname = (String) session.getAttribute(SessionConstant.GROUP_NAME);
		}
		UserInfo userinfo = null;
		try {
			if (StringUtils.isNotEmpty(operatorName)) {
				userinfo = userInfoService.getUserInfoByKey(operatorName);
			}
			if (userinfo != null) {
				if (StringUtils.isNotEmpty(userKey))
					userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(role))
					role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
				if (StringUtils.isNotEmpty(groupName))
					groupName = new String(groupName.getBytes("ISO-8859-1"), "UTF-8");
				if (groupname.equals(GroupNameConstant.ADMIN)) {
					List<UserInfo> userInfoList = userInfoService.searchList(userKey, role, groupName, teamId, status);
					Workbook workBook = userInfoService.save2Excel(userInfoList);
					String fileName = "UserInfo.xls";
					response.setContentType("application/x-msdownload");
					response.setHeader("Content-Disposition", new String(("attachment; filename=\"" + fileName + "\"").getBytes("GBK"), "ISO-8859-1"));
					workBook.write(response.getOutputStream());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/addUserInfo")
	public @ResponseBody
	ResponseResult addUserInfo(HttpServletRequest request, ModelMap model, String userKey, String userName, String password, String userMobile,
			String role, String groupName, Integer teamId, Byte status) {
		try {
			Date date = new Date();
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			if (StringUtils.isNotEmpty(userKey)) {
				userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
				if (userInfoService.isExistUserKey(userKey) == true) {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "姓名已存在");
				}
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "姓名不能为空");
			}
			if (StringUtils.isNotEmpty(userMobile)) {
				userMobile = new String(userMobile.getBytes("ISO-8859-1"), "UTF-8");
			if (ParamUtil.isMobile(userMobile) == false) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "手机号格式不正确");
			}
			if (userInfoService.isExistUserMobile(userMobile) == true) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "手机号已存在");
				}
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "手机号不能为空");
			}
			if (StringUtils.isNotEmpty(role))	role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(userName))	userName = new String(userName.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(password)) {
				password = Md5PasswordEncoder.encode(password);
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "密码不能为空");
			}
			if (StringUtils.isNotEmpty(groupName)) {
				groupName = new String(groupName.getBytes("ISO-8859-1"), "UTF-8");
			}
			UserInfo userinfo2 = null;
			if (StringUtils.isNotEmpty(operatorName)) {
				userinfo2 = userInfoService.getUserInfoByKey(operatorName);
				if (StringUtils.isNotEmpty(userinfo2.getRole())) {
					role = userinfo2.getRole();
					if(role.equals(RoleNameConstant.TEACHER)){
						groupName = GroupNameConstant.T_EDITOR;
					}else if(role.equals(RoleNameConstant.STUDENT)){
						groupName = GroupNameConstant.EDITOR;
					}	
				}
				if (userinfo2.getTeamId() != null) {
					teamId = userinfo2.getTeamId();
				}
			}

			UserInfo userinfo = new UserInfo(userKey, userName, userMobile, password, role, groupName, status, teamId, date, date, operatorName);
			userinfo = userInfoService.insertUserInfo(userinfo);
			if (userinfo != null) {
				UserInfo userinfo1 = userInfoService.getUserInfoByKey(userinfo.getUserKey());
				return successJson(userinfo1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "创建用户失败");
	}

	@RequestMapping(value = "/updateUserInfo")
	public @ResponseBody
	ResponseResult editUserInfo(HttpServletRequest request, ModelMap model, Integer id, String userKey, String userName, String password,
			String userMobile, String role, String groupName, Integer teamId, Byte status) {
		try {
			Date date = new Date();
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			if (StringUtils.isNotEmpty(userKey))
				userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(userName))
				userName = new String(userName.getBytes("ISO-8859-1"), "UTF-8");	
			if (StringUtils.isNotEmpty(userMobile)) {
				userMobile = new String(userMobile.getBytes("ISO-8859-1"), "UTF-8");
				if (ParamUtil.isMobile(userMobile) == false)
					return errorJson(EnumResCode.SERVER_ERROR.value(), "手机号格式不正确");
			} else
				return errorJson(EnumResCode.SERVER_ERROR.value(), "手机号不能为空");
			
			if (StringUtils.isNotEmpty(role))
				role = new String(role.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(password))
				password = Md5PasswordEncoder.encode(password);
			UserInfo userinfos = userInfoService.getOneUserInfoByKey(operatorName);
			if (userinfos != null) {
				if (GroupNameConstant.SHEN_HE.equals(userinfos.getGroupname())&&GroupNameConstant.T_SHEN_HE.equals(userinfos.getGroupname())) {
					teamId = userinfos.getTeamId();
				}
			}
			UserInfo userkeyinfo = userInfoService.getUserInfoById(id);
			if (userkeyinfo != null) {
				userKey = userkeyinfo.getUserKey();
				if (StringUtils.isNotEmpty(groupName)){
				groupName = new String(groupName.getBytes("ISO-8859-1"), "UTF-8");
			    }else{
			    	groupName = userkeyinfo.getGroupname();		    	
			    	if(groupName.equals(GroupNameConstant.T_EDITOR) ){
			    		if(StringUtils.isNotEmpty(role) && role.equals(RoleNameConstant.TEACHER)){
							groupName = GroupNameConstant.T_EDITOR;
							}else if(StringUtils.isNotEmpty(role) && role.equals(RoleNameConstant.STUDENT)){
							groupName = GroupNameConstant.EDITOR;
							}
			    	}else if(groupName.equals(GroupNameConstant.EDITOR)){
			    	  if(StringUtils.isNotEmpty(role) && role.equals(RoleNameConstant.TEACHER)){
			    	    groupName = GroupNameConstant.T_EDITOR;
			    	    }else if(StringUtils.isNotEmpty(role) && role.equals(RoleNameConstant.STUDENT)){
			    	      groupName = GroupNameConstant.EDITOR;
			    	     }  	
			    	}
			    }
			}
			UserInfo userinfo = new UserInfo(id, userKey, userName, userMobile, password, role, groupName, status, teamId, date, date, operatorName);
			int updateNum = userInfoService.updateIfNecessary(userinfo);
			if (updateNum == 1) {
				userinfo = userInfoService.getUserInfoById(userinfo.getId());
				return successJson(userinfo);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "更新用户失败");
	}

	@RequestMapping(value = "/getUserInfoByUserKey")
	public String getUserInfoByUserKey(HttpServletRequest request, ModelMap model, String userKey) throws Exception {
		HttpSession session = request.getSession();
		String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
		UserInfo userinfo = null;
		if (StringUtils.isNotEmpty(userKey)) {
			userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
		}
		userinfo = userInfoService.getOneUserInfoByKey(operatorName);
		if (userinfo != null) {
			model.addAttribute("userinfo", userinfo);
			return "management/editUserInfo";
		}
		return null;
	}

	@RequestMapping(value = "/updateBankUserInfo")
	public @ResponseBody
	ResponseResult updateBankUserInfo(HttpServletRequest request, ModelMap model, String userKey, String userName, String idNumber, String bank,
			String province, String city, String county, String bankSubbranch, String bankCard, String password, String passwordNew,
			String passwordNew2) {
		try {
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
			String newPassword = "";
			String oldIdNumber = "";
			UserInfo userinfos = userInfoService.getOneUserInfoByKey(operatorName);
			UserInfo userinfo = null;
			if (StringUtils.isEmpty(userKey) || StringUtils.isEmpty(userName)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "姓名不能为空");
			}
			userName = new String(userName.getBytes("ISO-8859-1"), "UTF-8");
			userKey = new String(userKey.getBytes("ISO-8859-1"), "UTF-8");
			
			if (StringUtils.isEmpty(idNumber))
				return errorJson(EnumResCode.SERVER_ERROR.value(), "身份证号不能为空");
			idNumber = new String(idNumber.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isEmpty(bank)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "开户银行不能为空");
			}
			bank = new String(bank.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isEmpty(province)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "省份不能为空");
			}
			province = new String(province.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isEmpty(city)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "地级市不能为空");
			}
			city = new String(city.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isEmpty(county)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "县级市不能为空");
			}
			county = new String(county.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isEmpty(bankCard)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "银行卡号不能为空");
			}
			bankCard = new String(bankCard.getBytes("ISO-8859-1"), "UTF-8"); 
			if (StringUtils.isEmpty(bankSubbranch)) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "支行名称不能为空");
			}
			bankSubbranch = new String(bankSubbranch.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(password)) {
				password = Md5PasswordEncoder.encode(password);
				if (password.equals(userinfos.getPassword())) {
					if (StringUtils.isNotEmpty(passwordNew) && StringUtils.isNotEmpty(passwordNew2)) {
						passwordNew = new String(passwordNew.getBytes("ISO-8859-1"), "UTF-8");
						passwordNew2 = new String(passwordNew2.getBytes("ISO-8859-1"), "UTF-8");
						if (passwordNew.equals(passwordNew2)) {
							newPassword = Md5PasswordEncoder.encode(passwordNew);
						}
					} else {
						return errorJson(EnumResCode.SERVER_ERROR.value(), "新密码不能为空");
					}
				} else {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "原密码输入错误");
				}
			}		
			if (userinfos != null) {		  		
			    operatorName="";
		  		if(GroupNameConstant.ADMIN.equals(groupName)) {
	    			 operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
		  			 userinfo= new UserInfo(operatorName, userName, idNumber, bank,
		  		    		 province, city, county,
		  		    		 bankSubbranch, bankCard, newPassword, new Date(), operatorName);
		 			int updateNum = userInfoService.updateUserInfoByUserKey(userinfo);
		 			if (updateNum == 1) {
		 				userinfo = userInfoService.getOneUserInfoByKey(operatorName);
		 				return successSaveJson(userinfo);
		 			}
		  		}else{	  			 
		  			oldIdNumber=userinfos.getIdNumber();
		  			if(StringUtils.isNotEmpty(oldIdNumber)){
		  				operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			  			 userinfo= new UserInfo(operatorName, userName, idNumber, bank,
			  		    		 province, city, county,
			  		    		 bankSubbranch, bankCard, newPassword, new Date(), operatorName);
			 			int updateNum = userInfoService.updateUserInfoByUserKey(userinfo);
			 			if (updateNum == 1) {
			 				userinfo = userInfoService.getOneUserInfoByKey(operatorName);
			 				return successSaveJson(userinfo);
			 			}
		  			}else{
		  				operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			  			 userinfo= new UserInfo(operatorName, userName, idNumber, bank,
			  		    		 province, city, county,
			  		    		 bankSubbranch, bankCard, newPassword, new Date(), operatorName);
			 			int updateNum = userInfoService.updateUserInfoByUserKey(userinfo);
			 			if (updateNum == 1) {
			 				userinfo = userInfoService.getOneUserInfoByKey(operatorName);
			 				return successJson(userinfo);
			 			}
		  			}
		  			 
		  		}									
			}else{
				return errorJson(EnumResCode.SERVER_ERROR.value(), "登陆账号不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "补充用户信息失败");
	}

	
	/*
	 * 重置密码 为1234
	 */
	@RequestMapping(value = "/updateUserPassword")
	public @ResponseBody
	ResponseResult editUserPassword(HttpServletRequest request, ModelMap model, Integer id) {
		try {
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			int updateNum = userInfoService.updatePassword(id, Md5PasswordEncoder.encode(passwordKey), operatorName);
			if (updateNum == 1)return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "重置密码失败");
	}

	/*
	 * 重置密码 为1234
	 */
	@RequestMapping(value = "/updateRecognitionUserPassword")
	public @ResponseBody
	ResponseResult updateRecognitionUserPassword(HttpServletRequest request, ModelMap model, Integer id) {
		try {
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			int updateNum = userInfoService.updatePassword(id, Md5PasswordEncoder.encode(passwordKey), operatorName);
			if (updateNum == 1)return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "重置密码失败");
	}

	/*
	 * 更新最新阅读公告id 为readid
	 */
	@RequestMapping(value = "/updateUserReadId")
	public @ResponseBody
	ResponseResult updateUserReadId(HttpServletRequest request, ModelMap model, String userkey, Integer readid) {
		try {
			if (StringUtils.isNotEmpty(userkey))
				userkey = new String(userkey.getBytes("ISO-8859-1"), "UTF-8");
			int updateNum = userInfoService.updateUserReadId(userkey, readid);
			if (updateNum == 1)return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "阅读失败");
	}
}
