package com.xuexibao.ops.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.OrganizationSources;
import com.xuexibao.ops.service.OrganizationService;
import com.xuexibao.ops.util.http.HttpSubmit;

@Controller
@RequestMapping(value = "/book")
public class OrganizationController extends AbstractController {
	
	private static final int limit = 30;
	private Long totalNum;
	private Long totalPageNum;
	
	
	@Autowired
	protected OrganizationService organizationService;

	@RequestMapping(value = "/organizationList")
	public String organizationList(HttpServletRequest request, ModelMap model, String name, String isbn, String sourceName, Integer status,
			  String startTime, String endTime, Long page)  {
		try {
			String Result="";
		    Map<String, String> MSG_sParaTemp = null;
		    JSONArray jsonArray = null; 	 		
		    page = page == null || page < 0 ? 0 : page;
		    if (StringUtils.isNotEmpty(name))
		    	name = new String(name.getBytes("ISO-8859-1"), "UTF-8");		
			    MSG_sParaTemp = new HashMap<String, String>();
			    JSONObject resObj = new JSONObject();
			    MSG_sParaTemp.put("organizationName", name);
			    MSG_sParaTemp.put("index", String.valueOf(page + 1));
			    MSG_sParaTemp.put("pageSize", String.valueOf(limit));								
			    Result=HttpSubmit.sendPostOrGetInfo_Teacher(MSG_sParaTemp, CommonConstant.TEACHER_QUERYURL, "POST");
			if(StringUtils.isNotEmpty(Result)){
				resObj=JSON.parseObject(Result);		
				JSONObject resultObj = resObj.getJSONObject("data");
				if(resultObj.getJSONArray("rows").size()>0){
					jsonArray = new JSONArray();  
					jsonArray = resultObj.getJSONArray("rows");					
					resultObj.getString("index");
					totalNum =Long.valueOf(resultObj.getString("total"));
					totalPageNum =Long.valueOf(resultObj.getString("total"))/Long.valueOf(resultObj.getString("pageSize"));
					resultObj.getString("pageSize");
				    resultObj.getString("total");
					//解析返回的数据
				    }else{
					//返回无结果
					}
				}
			model.addAttribute("name", name);
		    model.addAttribute("status", status);
			model.addAttribute("startTime", startTime);
			model.addAttribute("endTime", endTime);
			model.addAttribute("organizationList", jsonArray);
			model.addAttribute("page", page);
			model.addAttribute("totalNum", totalNum);
			model.addAttribute("totalpage", totalPageNum);
			return "book/organizationList";
		} catch (Exception e) {
			// TODO Auto-generated catch bloc
			e.printStackTrace();
			return "book/organizationList";
		}
	}

	@RequestMapping(value = "/organizationSourcesList")
	public @ResponseBody ResponseResult organizationSourcesList(HttpServletRequest request, ModelMap model, String name){
		try {
		String Result="";
		Map<String, String> MSG_sParaTemp = null;
		JSONArray jsonArray = null;  
		if (StringUtils.isNotEmpty(name))
			name = new String(name.getBytes("ISO-8859-1"), "UTF-8");		
		MSG_sParaTemp = new HashMap<String, String>();
		JSONObject resObj = new JSONObject();
		MSG_sParaTemp.put("organizationName", name);
		MSG_sParaTemp.put("index", String.valueOf(1));
		MSG_sParaTemp.put("pageSize", String.valueOf(limit));	
		Result=HttpSubmit.sendPostOrGetInfo_Teacher(MSG_sParaTemp, CommonConstant.TEACHER_QUERYURL, "POST");
		if(StringUtils.isNotEmpty(Result)){
            resObj=JSON.parseObject(Result);
			JSONObject resultObj = resObj.getJSONObject("data");
			jsonArray = new JSONArray();  
			jsonArray = resultObj.getJSONArray("rows");
			if(jsonArray.size()>0){
				List<Map<String, Object>> result = new ArrayList<>();
				for (int i = 0; i < jsonArray.size(); ++i) {
	            JSONObject o = (JSONObject) jsonArray.get(i);
	            Map<String, Object> map = new HashMap<>();
				map.put("id", o.getIntValue("id"));
				map.put("name", o.getString("organizationName"));
				result.add(map);
				}			
		    	return successJson(result);
	    	}
			  return errorJson(EnumResCode.SERVER_ERROR.value(), "来源不存在！");//解析返回的数据
		  }else{//返回无结果
			  return errorJson(EnumResCode.SERVER_ERROR.value(), "来源不存在！");
		  }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "来源不存在！");
		}
	}

	@RequestMapping(value = "/addOrganizationSources")
	public @ResponseBody
	ResponseResult addOrganizationSources(HttpServletRequest request, ModelMap model, String name) {
		try {
			HttpSession session = request.getSession();
			String operatorName = (String) session.getAttribute(SessionConstant.USER_NAME);
			if (StringUtils.isNotEmpty(name)) {
				name = new String(name.getBytes("ISO-8859-1"), "UTF-8");
				if (organizationService.isExistName(name) == true) {
					return errorJson(EnumResCode.SERVER_ERROR.value(), "来源已存在");
				}
			} else {
				return errorJson(EnumResCode.SERVER_ERROR.value(), "来源不能为空");
			}
			OrganizationSources organizationSourcesInfo = new OrganizationSources(name,Integer.valueOf(0),new Date(),new Date(),operatorName);
			organizationSourcesInfo = organizationService.insertOrganizationSourcesInfo(organizationSourcesInfo);
			if (organizationSourcesInfo != null) {
				OrganizationSources organizationSourcesInfo1 = organizationService.getOrganizationSourcesInfoByName(organizationSourcesInfo.getOrganizationName());
				return successJson(organizationSourcesInfo1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return errorJson(EnumResCode.SERVER_ERROR.value(), "添加来源失败");
	}
}
