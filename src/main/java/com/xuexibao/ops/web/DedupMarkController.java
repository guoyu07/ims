package com.xuexibao.ops.web;


import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.xuexibao.ops.model.DedupCheckDetail;
import com.xuexibao.ops.model.DedupMark;
import com.xuexibao.ops.model.DedupMarkMongo;
import com.xuexibao.ops.service.DedupCheckDetailService;
import com.xuexibao.ops.service.DedupMarkService;


@Controller
@RequestMapping(value = "/mark")
public class DedupMarkController extends AbstractController {
	
	@Autowired
	protected DedupMarkService dedupMarkService;
	
	@Autowired
	protected DedupCheckDetailService dedupCheckDetailService;
	
//	@RequestMapping(value = "/fromMongoToMysql")
	public @ResponseBody ResponseResult fromMongoToMysql() {
		try {
			dedupMarkService.fromMongoToMysql();
			return successJson();
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "fromMongoToMysql失败");
		}
	}
	
	@RequestMapping(value = "/getOneDupGroup")
	public String getOneDupGroup(HttpServletRequest request, ModelMap model) {
		try {
			JSONObject result = new JSONObject();
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			DedupMark dedupMark = dedupMarkService.getOneDedupMark(userKey);
			List<DedupMarkMongo> dedupMarkGroup;
			
			if(dedupMark != null){
				dedupMarkGroup = dedupMarkService.getOneGroupQuestion(dedupMark);
				JSONArray  candidateListJson = new JSONArray();
				
				candidateListJson = (JSONArray) JSONArray.toJSON(dedupMarkGroup);
				Integer remainCNT = dedupMarkService.getUnfinishedCount(userKey);
				if(remainCNT ==null) remainCNT=0;
				result.put("status", "0");
				result.put("msg", "ok");
				result.put("remainCNT", remainCNT);
				result.put("block", dedupMark.getBlock());
				result.put("candidate_list", candidateListJson);

			}else{
				result.put("status", "1");
				result.put("msg", "当前组已经完毕，如需继续请点击【下一组】");					
			}

			model.addAttribute("result", result);
			return "mark/markRepeat";		
	  		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	//抽检详细---分重复不重复。
	@RequestMapping(value = "/getCheckOneDupGroup")
	public String getCheckOneDupGroup(HttpServletRequest request, ModelMap model,Long checkId)  {
		try {
			JSONObject result = new JSONObject();
			DedupCheckDetail checkDetail = dedupCheckDetailService.getCheckDetailById(checkId);
			//根据beas_id获取重复题目组
			DedupMark dedupMark = dedupMarkService.getdedupMarkById(checkDetail.getTranId());
			List<DedupMarkMongo> dedupMarkBestGroup;
			List<DedupMarkMongo> dedupMarkSameGroup;
			List<DedupMarkMongo> dedupMarkDifGroup;
			
			if(dedupMark != null){
				dedupMarkBestGroup = dedupMarkService.getCheckBestOneQuestion(dedupMark);
				dedupMarkSameGroup = dedupMarkService.getCheckSameOneGroupQuestion(dedupMark);
				dedupMarkDifGroup  = dedupMarkService.getCheckDifOneGroupQuestion(dedupMark);
				
				JSONArray  candidateBestListJson = new JSONArray();
				JSONArray  candidateSameListJson = new JSONArray();
				JSONArray  candidateDifListJson = new JSONArray();
				
				candidateBestListJson = (JSONArray) JSONArray.toJSON(dedupMarkBestGroup);
				candidateSameListJson = (JSONArray) JSONArray.toJSON(dedupMarkSameGroup);
				candidateDifListJson = (JSONArray) JSONArray.toJSON(dedupMarkDifGroup);
				
				result.put("status", "0");
				result.put("msg", "ok");
				result.put("checkId", checkId);
				result.put("parentId", checkDetail.getParentId());
				result.put("cstatus", checkDetail.getCstatus());
				result.put("base_list", candidateBestListJson);
				result.put("dup_list", candidateSameListJson);
				result.put("undup_list", candidateDifListJson);
				
			}

			model.addAttribute("result", result);
			return "dedup/sampleCheck";		
	  		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@RequestMapping(value = "/updateOneDupGroup")
	public @ResponseBody ResponseResult updateOneDupGroup(HttpServletRequest request, HttpServletResponse response, String block, String baseId, String dupQuestionId) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			//标记单条为已经标记完毕status=1
			dedupMarkService.updateMarkStatus(userKey, baseId, dupQuestionId, block);
			
			//整租完成时标记finished=1
			dedupMarkService.updateMarkBlock(userKey, block, baseId);
			
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "标记重复失败！(baseId=" + baseId + ";Time=" + new Date() +")" );
		}
		
		return successJson(null);
	}
	
	@RequestMapping(value = "/cancelBlock")
	public @ResponseBody ResponseResult disableBlock(HttpServletRequest request, HttpServletResponse response, String block) {
		try {
			response.setHeader("Access-Control-Allow-Origin", "*");

			dedupMarkService.disableBlock(block);
			
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "重置组失败！" );
		}
		
		return successJson(null);
	}	
	
	@RequestMapping(value = "/assignNewBlock")
	public @ResponseBody ResponseResult assignNewBlock(HttpServletRequest request) {
		try {
			HttpSession session = request.getSession();
			String userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			Map<String, Object> result = dedupMarkService.assignNewBlock(userKey);
			if((Boolean) result.get("success"))
				return successJson();
			else
				return errorJson(EnumResCode.SERVER_ERROR.value(), (String) result.get("msg"));
		} catch (Exception e) {
			e.printStackTrace();
			return errorJson(EnumResCode.SERVER_ERROR.value(), "assignNewBlock失败");
		}
	}

}
