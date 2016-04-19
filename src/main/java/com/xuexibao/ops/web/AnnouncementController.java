package com.xuexibao.ops.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xuexibao.ops.constant.AnnouncementStatusConstant;
import com.xuexibao.ops.constant.GroupNameConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.Announcement;
import com.xuexibao.ops.model.UserInfo;
import com.xuexibao.ops.service.AnnouncementService;
import com.xuexibao.ops.service.UserInfoService;


@Controller
@RequestMapping(value = "/notice")
public class AnnouncementController extends AbstractController {

	private static final int limit = 2;

	@Autowired
	protected AnnouncementService announcementService;
	@Autowired
	protected UserInfoService userInfoService;
	@RequestMapping(value = "/announcementList")
	public String announcementSearch(HttpServletRequest request, ModelMap model, Integer status, Long page) {
		try{
			HttpSession session = request.getSession();
			String groupName = (String) session.getAttribute(SessionConstant.GROUP_NAME);
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			Boolean isread=null;
			String readid = null;
			String firstid = null;
			UserInfo userinfo = null;
			if (StringUtils.isNotEmpty(userKey)) {
				userinfo = userInfoService.getUserInfoByKey(userKey);			
			if( userinfo != null){
				Announcement announcement = announcementService.getNewAnnouncement();
				if( userinfo.getReadid() != null){
					readid = userinfo.getReadid().toString();
				}
				if (announcement != null){
					firstid=announcement.getId().toString();
					if(announcement.getId().equals(userinfo.getReadid())){
						isread=true;
					}else{
						isread=false;
					}
				}
			}	
			}
			page = page == null || page < 0 ? 0 : page;

			long totalNum = announcementService.searchCount(status);

			long totalPageNum = totalNum / limit;
			if(totalNum > totalPageNum * limit)
				totalPageNum++;
			if(page >= totalPageNum && totalPageNum != 0)
				page = totalPageNum - 1;
			List<Announcement> announcementList = announcementService.searchList(status, page * limit, limit);
			model.addAttribute("status", status);
			model.addAttribute("announcementList", announcementList);
			model.addAttribute("page", page);
			model.addAttribute("userKey", userKey);
			model.addAttribute("isread", isread);
			model.addAttribute("firstid", firstid);
			model.addAttribute("readid", readid);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);	
			if(GroupNameConstant.ADMIN.equals(groupName)) {
				return "management/announcementList";
			}else{
				return "management/notice";
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/addAnnouncement")
	public @ResponseBody ResponseResult addAnnouncement(HttpServletRequest request, String text) {
		try {
			if (StringUtils.isEmpty(text))
				return errorJson(EnumResCode.SERVER_ERROR.value(), "内容参数异常");
			text = new String(text.getBytes("ISO-8859-1"), "UTF-8");
			HttpSession session = request.getSession();
			String operator = (String) session.getAttribute(SessionConstant.USER_NAME);
			if(StringUtils.isNotEmpty(text))
				text = text.replaceAll("'&nbsp';", "&nbsp");
			Announcement announcement = new Announcement(AnnouncementStatusConstant.OPEN, text, operator, new Date());
			announcement = announcementService.insertSelective(announcement);
			if(announcement != null)
				return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "新增公告失败");
	}

	@RequestMapping(value = "/updateAnnouncement")
	public @ResponseBody ResponseResult updateAnnouncement(HttpServletRequest request, Integer id, Integer status, String text ) {
		try {
			if (StringUtils.isEmpty(text) ) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "内容参数异常");
			}	
			if (id == null || status == null ) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "id参数异常");
			}
			HttpSession session = request.getSession();
			String operator = (String) session.getAttribute(SessionConstant.USER_NAME);
			if(StringUtils.isNotEmpty(text))
				text = text.replaceAll("'&nbsp';", "&nbsp");
			Announcement announcement = new Announcement(id ,status, text, operator, new Date());
			boolean updateSuccess = announcementService.updateAnnouncement(announcement);
			if(updateSuccess)
				return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "修改公告失败");
	}
	//删除公告
	@RequestMapping(value = "/deleAnnouncement")
	public @ResponseBody ResponseResult deleAnnouncement(HttpServletRequest request, Integer id ) {
		try {		
			if (id == null ) {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "id参数异常");
			}
			HttpSession session = request.getSession();
			String operator = (String) session.getAttribute(SessionConstant.USER_NAME);	
			Announcement announcement = new Announcement(id ,AnnouncementStatusConstant.CLOSE, null, operator, new Date());
			boolean updateSuccess = announcementService.updateAnnouncement(announcement);
			if(updateSuccess)
				return successJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "修改公告失败");
	}
}
