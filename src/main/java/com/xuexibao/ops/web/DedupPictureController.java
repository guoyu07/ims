package com.xuexibao.ops.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.dto.ResponseResult;
import com.xuexibao.ops.enumeration.EnumResCode;
import com.xuexibao.ops.model.DedupBaseExam;
import com.xuexibao.ops.model.DedupGroupCandidates;
import com.xuexibao.ops.service.DeDuplicateService;
import com.xuexibao.ops.task.DeleteDuplicateTask;


@Controller
@RequestMapping(value = "/markDupPic")
public class DedupPictureController extends AbstractController {
	
	@Autowired
	protected DeDuplicateService deDuplicateService;
	
	private static Logger logger = LoggerFactory.getLogger("dupQuestion_check_log");
	
	@RequestMapping(value = "/getOneDupGroup")
	public String getOneDupGroup(HttpServletRequest request, ModelMap model, String phase) {
		try {
			boolean nodata = true;
			JSONObject result = new JSONObject();
			
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			DedupBaseExam baseExam =null;
			List<DedupGroupCandidates> candidateList =null;
			
			if("0".equals(phase)){
				baseExam = DeleteDuplicateTask.duplicateGroupList.poll();
			}else{
				baseExam = DeleteDuplicateTask.duplicateGroupListPhase2.poll();
			}
			
			if(baseExam !=null){
				//update dedup_base_exam ==>2
				deDuplicateService.setDedupBaseExam(baseExam.getGroupId(), userKey, 2,null, null, null);
				
				if("0".equals(phase)){
					candidateList = deDuplicateService.getCandidatesById(baseExam.getGroupId(), baseExam.getQuestionId());
				}else{
					candidateList = deDuplicateService.getSelectedById(baseExam.getGroupId());
				}				
				 
				JSONArray  candidateListJson = new JSONArray();
				if(candidateList.size() > 0){
					candidateListJson = (JSONArray) JSONArray.toJSON(candidateList);
					result.put("status", "0");
					result.put("msg", "ok");					
					result.put("candidate_list", candidateListJson);
					nodata = false;
				}				
			}
			if(nodata){
				result.put("status", "1");					
				result.put("msg", "没有要处理的数据.");
			}

			model.addAttribute("result", result);
			if("0".equals(phase)){
				return "mark/markRepeat";
			}else{
				return "mark/selectBest";
			}			
	  		
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	

	@RequestMapping(value = "/mark")
	public @ResponseBody ResponseResult markDuplicate(HttpServletRequest request, HttpServletResponse response, Integer groupId, Integer analyzeCount, String[] dupQuestionId) throws IOException {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);

			deDuplicateService.updateGroup(userKey, groupId, analyzeCount, dupQuestionId);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "标记重复失败！(GroupId=" + groupId + ";Time=" + new Date() +")" );
		}
		
		return successJson(null);
	}	
	

	@RequestMapping(value = "/bestSelect")
	public @ResponseBody ResponseResult bestSelect(HttpServletRequest request, HttpServletResponse response, Integer groupId, String bestQuestionId) throws IOException {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			
			deDuplicateService.updateBest(userKey, groupId, bestQuestionId);
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "标记重复失败！(GroupId=" + groupId + ";Time=" + new Date() +")" );
		}
		
		return successJson(null);
	}		
}
