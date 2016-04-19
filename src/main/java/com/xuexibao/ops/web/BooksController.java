package com.xuexibao.ops.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.CommonConstant;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dao.IOrcPictureDao;
import com.xuexibao.ops.dao.IOrganizationSourcesDao;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.Books;
import com.xuexibao.ops.service.BooksService;
import com.xuexibao.ops.service.OrganizationService;
import com.xuexibao.ops.util.DateUtils;
import com.xuexibao.ops.util.http.HttpSubmit;


@Controller
@RequestMapping(value = "/book")
public class BooksController extends AbstractController {

	private static final int limit = 30;

	@Autowired
	protected BooksService booksService;
	@Autowired
	protected OrganizationService organizationService;
	@Resource
	IOrganizationSourcesDao organizationSourcesDao;
	@Resource
	IOrcPictureDao orcPictureDao;

	@RequestMapping(value = "/booksInfoList")
	public String booksInfoList(HttpServletRequest request, ModelMap model, String name, String isbn, String sourceName, Integer status,
			String startTime, String endTime,
			Long page) throws Exception {
		String organizationName = null;
		page = page == null || page < 0 ? 0 : page;
		if (StringUtils.isNotEmpty(name))
			name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
		if (StringUtils.isNotEmpty(isbn))
			isbn = new String(isbn.getBytes("ISO-8859-1"), "UTF-8");
		if (StringUtils.isNotEmpty(sourceName)){
			sourceName = new String(sourceName.getBytes("ISO-8859-1"), "UTF-8");
			String Result="";
			Map<String, String> MSG_sParaTemp = null;
			JSONArray jsonArray = null;  
			MSG_sParaTemp = new HashMap<String, String>();
			JSONObject resObj = new JSONObject();
			MSG_sParaTemp.put("organizationName", sourceName);
			MSG_sParaTemp.put("index", String.valueOf(1));
			MSG_sParaTemp.put("pageSize", String.valueOf(limit));
			Result=HttpSubmit.sendPostOrGetInfo_Teacher(MSG_sParaTemp, CommonConstant.TEACHER_QUERYURL, "POST");
			if(StringUtils.isNotEmpty(Result)){
				resObj=JSON.parseObject(Result);
				JSONObject resultObj = resObj.getJSONObject("data");
				jsonArray = new JSONArray();  
				jsonArray = resultObj.getJSONArray("rows");
				if(jsonArray.size()>0){
					for (int i = 0; i < jsonArray.size(); ++i) {
						JSONObject o = (JSONObject) jsonArray.get(i);
						organizationName=String.valueOf(o.getIntValue("id"));
					}		
				}
			}else{
				organizationName=sourceName;
			}

		}
		Date startDate = DateUtils.parseDate(startTime, "yyyy-MM-dd HH:mm");
		Date endDate = DateUtils.parseDate(endTime, "yyyy-MM-dd HH:mm");
		long totalNum = booksService.searchCount(name, isbn, organizationName, status, startDate, endDate);
		long totalPageNum = totalNum / limit;
		if (totalNum > totalPageNum * limit)
			totalPageNum++;
		if (page >= totalPageNum && totalPageNum != 0)
			page = totalPageNum - 1;
		long start = page * limit;
		List<Books> booksInfoList = booksService.searchList(name, isbn, organizationName, status, startDate, endDate, start,
				limit);
		model.addAttribute("name", name);
		model.addAttribute("isbn", isbn);
		model.addAttribute("sourceName", sourceName);
		model.addAttribute("status", status);
		model.addAttribute("startTime", startTime);
		model.addAttribute("endTime", endTime);
		model.addAttribute("booksInfoList", booksInfoList);
		model.addAttribute("page", page);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("totalpage", totalPageNum);
		return "book/booksInfoList";

	}

	@RequestMapping(value = "/booksList")
	public @ResponseBody ResponseResult booksList(HttpServletRequest request, ModelMap model, String nameisbn) throws Exception {
		if (StringUtils.isNotEmpty(nameisbn))
			nameisbn = new String(nameisbn.getBytes("ISO-8859-1"), "UTF-8");
		List<Books> booksInfoList = booksService.searchList(nameisbn);
		List<Map<String, Object>> result = new ArrayList<>();
		if (booksInfoList != null && booksInfoList.size()>0){
			for (Books book:booksInfoList){
				Map<String, Object> map = new HashMap<>();
				map.put("id", book.getId());
				map.put("name", book.getName());
				map.put("isbn", book.getIsbn());
				map.put("publishingHouse", book.getPublishingHouse());
				result.add(map);
			}
			return successJson(result);
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "书籍不存在！");

	}


	@RequestMapping(value = "/addBookInfo")
	public @ResponseBody
	ResponseResult addBookInfo(HttpServletRequest request, ModelMap model, String name, String isbn, String subject, String grade,
			String publishing_house , String source_id) {
		try {

			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);

			if (StringUtils.isNotEmpty(name)) {
				name = new String(name.getBytes("ISO-8859-1"), "UTF-8");	
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "书名不能为空");
			}
			if (StringUtils.isNotEmpty(isbn) || StringUtils.isNotEmpty(source_id)) {
				if(StringUtils.isNotEmpty(isbn)) isbn = new String(isbn.getBytes("ISO-8859-1"), "UTF-8");
				if( StringUtils.isNotEmpty(source_id)) source_id = new String(source_id.getBytes("ISO-8859-1"), "UTF-8");
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "ISBN、来源必须填写一个");
			}
			if (StringUtils.isNotEmpty(subject))
				subject = new String(subject.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(grade))
				grade = new String(grade.getBytes("ISO-8859-1"), "UTF-8");
			if (StringUtils.isNotEmpty(publishing_house))
				publishing_house = new String(publishing_house.getBytes("ISO-8859-1"), "UTF-8");

			Books bookinfo = new Books(name, isbn, subject, grade, publishing_house, source_id, Integer.valueOf(0), new Date(), new Date(), operatorName);
			Books bookinfos = booksService.insertBookInfo(bookinfo);
			if (bookinfos != null) {
				if(bookinfo.getName() != null){
					Books bookinfo1 = booksService.getBookInfoByName(bookinfo.getName());
					if(bookinfo1 != null){
						setSourceId(bookinfo1);
						bookinfo1.setCreateTimeForShow(DateUtils.formatDate(bookinfo1.getCreateTime(), "yyyy-MM-dd HH:mm"));
						return successJson(bookinfo1);
					}else{
						return null;
					}
				}else{
					return errorJson(EnumResCode.SERVER_ERROR.value(), "创建书籍名称出错");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "创建书籍失败");
	}
	
	@RequestMapping(value = "/editBest")
	public @ResponseBody ResponseResult editBest(HttpServletRequest request, ModelMap model, Long id, Integer best) {
		try {
			if (id == null || best == null)
				return errorJson(EnumResCode.SERVER_ERROR.value(), "书籍ID不可以为空");
			Books bookinfo = new Books(id, best);
			booksService.updateIfNecessary(bookinfo);
			return successJson(bookinfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "修改书籍状态失败");
	}

	@RequestMapping(value = "/addBookSourceInfo")
	public @ResponseBody
	ResponseResult addBookSourceInfo(HttpServletRequest request, ModelMap model, Long id, String source_id) {
		try {

			HttpSession session = request.getSession();

			Books bookinfo=null;
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);

			bookinfo=booksService.getById(id);
			if (bookinfo != null) {		
				if (StringUtils.isNotEmpty(source_id)) {
					String organizationSources=null;
					organizationSources=bookinfo.getSourceId();					
					source_id = new String(source_id.getBytes("ISO-8859-1"), "UTF-8");	
					//如果新增来源存在，并且原来有来源 不包含新加的来源
					boolean isid = true;
					if(StringUtils.isNotEmpty(organizationSources)){
						String[] sourceIdArray =organizationSources.split(",");
						for(String i:sourceIdArray){
							//循环判断每个机构id是否存在，
							if(i.equals(source_id)){//如果存在
								isid = false;
								break;
							}
						}
						if( isid == true){
							source_id =organizationSources+","+source_id;
						}else{
							return errorJson(EnumResCode.SERVER_ERROR.value(), "来源已经存在");
						}
					}			
				} else {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "来源不能为空");
				}

				Books booksinfo = new Books(bookinfo.getId(),bookinfo.getName(),bookinfo.getIsbn(),bookinfo.getSubject(),bookinfo.getGrade(),bookinfo.getPublishingHouse(), source_id,bookinfo.getStatus(),bookinfo.getCreateTime(), new Date(), operatorName);
				int updateNum = booksService.updateBookInfo(booksinfo);		
				if (updateNum == 1) {
					Books book = booksService.getById(bookinfo.getId());
					setSourceId(book);
					//更新一本书的来源，重置推送过这本书下面的识别正确的题目状态，改为0需要重新推送
					orcPictureDao.updatePushstatusByBookId(book.getId());


					return successJson(book);
				}
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "书籍不存在");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "新增来源失败");
	}

	// 来源id 转换成来源名称
	private void setSourceId(Books books) {
		try {
			String Result="";
			Map<String, String> MSG_sParaTemp = null;
			String organizationName="";
			if(books != null){
				if(StringUtils.isNotEmpty(books.getSourceId())) {
					books.setSourceIdArray((books.getSourceId().split(",")));
					StringBuffer buff = new StringBuffer();
					for(String i:books.getSourceIdArray() ){
						if(StringUtils.isNotEmpty(i)){ 
							MSG_sParaTemp = new HashMap<String, String>();
							JSONObject resObj = new JSONObject();
							MSG_sParaTemp.put("id", i);	

							Result=HttpSubmit.sendPostOrGetInfo_Teacher(MSG_sParaTemp, CommonConstant.TEACHER_QUERYONEURL, "POST");
							if(StringUtils.isNotEmpty(Result)){
								resObj=JSON.parseObject(Result);					
								JSONObject resultObj = resObj.getJSONObject("data");
								if(resultObj != null && resultObj.size()>0){
									JSONObject o = resultObj;
									organizationName = o.getString("organizationName");						 
									buff.append(organizationName).append(',');					 
								}else{
									buff.append(i).append(',');	
								}
							}			
						}						 
					}		
					//去掉最后一个逗号
					if(buff.toString().length()>0){
						books.setSourceId(buff.toString().substring(0,buff.toString().length()-1));				
					}else
					{
						books.setSourceId(buff.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
