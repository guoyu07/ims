
package com.xuexibao.ops.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.xuexibao.ops.constant.SessionConstant;
import com.xuexibao.ops.model.OrcPictureResultCount;
import com.xuexibao.ops.service.BatchOrcPictureService;
import com.xuexibao.ops.util.DateUtils;


@Controller
@RequestMapping(value = "/batchpicture")
public class BatchOrcPictureResultController extends AbstractController {
	
	@Autowired
	protected BatchOrcPictureService orcPictureService;
	
	
	@RequestMapping(value = "/orcPictureViewResult")
	public String orcPictureViewResult(HttpServletRequest request, ModelMap model,
			  String userKey, String target,   String searchMonth) {
		try {
			HttpSession session = request.getSession();
			userKey = (String) session.getAttribute(SessionConstant.USER_NAME);
			
			if(StringUtils.isEmpty(target)){
				target="0";
			}
			if(StringUtils.isEmpty(searchMonth)){
				searchMonth =  DateUtils.formatDate(new Date(), "yyyy-MM");
			}else{
				if(searchMonth.contains("/")){
					searchMonth= searchMonth.replace("/", "-"); 
				}else if(!searchMonth.contains("-")){
					searchMonth= searchMonth.substring(0, 4) + "-" + searchMonth.substring(4, 6); 
				}
			}
			
			List<OrcPictureResultCount> OrcPictureBatchSearchList = orcPictureService.computeRecPercentPerMonth(target, userKey, searchMonth);
			
			List<OrcPictureResultCount> resultCountList = new ArrayList<OrcPictureResultCount>();
			
			if(OrcPictureBatchSearchList != null && OrcPictureBatchSearchList.size() >0){
				long total=0;
				long success=0;
				long failure=0;
				long noProcess=0;
				long totalJedged=0;
				double percent=0.0;
				String newDate="";
				OrcPictureResultCount orcPictureResult=null;
								
				for(OrcPictureResultCount orcPictureSearch :OrcPictureBatchSearchList){
					if(StringUtils.isEmpty(newDate)){
						orcPictureResult = new OrcPictureResultCount();
						newDate = orcPictureSearch.getCreate_ymd();
					}else if(!newDate.equals(orcPictureSearch.getCreate_ymd())){
						//日期变化
						total = success + failure + noProcess;
						totalJedged = success + failure;
						if(totalJedged>0){
							percent =success*1.0/ totalJedged;
							orcPictureResult.setSuccess_rate(String.format("%1$.2f",percent*100));
						}else{
							orcPictureResult.setSuccess_rate("-");
						}
						orcPictureResult.setCreate_ymd(newDate);
						orcPictureResult.setSuccess_count(success);
						orcPictureResult.setFailure_count(failure);
						orcPictureResult.setTotal_count(total);
						orcPictureResult.setUnjudged_count(noProcess);
						resultCountList.add(orcPictureResult);
						
						//新对象
						orcPictureResult = new OrcPictureResultCount();
						newDate = orcPictureSearch.getCreate_ymd();
						total=0;
						success=0;
						failure=0;
						totalJedged=0;
						percent=0.0;
						noProcess=0;
					}
			
					switch (orcPictureSearch.getStatus()) {
					case 0:
						noProcess +=orcPictureSearch.getCountByStatus();
						break;					
					case 1:
						success +=orcPictureSearch.getCountByStatus();
						break;
					case 2:
						failure+=orcPictureSearch.getCountByStatus();
						break;
					case 3:
						failure+=orcPictureSearch.getCountByStatus();
						break;
					case 4:
						failure+=orcPictureSearch.getCountByStatus();
						break;
					case 5:
						failure+=orcPictureSearch.getCountByStatus();
						break;	
					case 6:
						failure+=orcPictureSearch.getCountByStatus();
						break;	
					case 7:
						failure+=orcPictureSearch.getCountByStatus();
						break;						
					default:
						break;
					}
				}
				
				//最后一条
				total = success + failure +noProcess;
				totalJedged = success + failure;
				if(totalJedged>0){
					percent =success*1.0/ totalJedged;
					orcPictureResult.setSuccess_rate(String.format("%1$.2f",percent*100));
				}else{
					orcPictureResult.setSuccess_rate("-");
				}
				orcPictureResult.setCreate_ymd(newDate);
				orcPictureResult.setSuccess_count(success);
				orcPictureResult.setFailure_count(failure);
				orcPictureResult.setTotal_count(total);
				orcPictureResult.setUnjudged_count(noProcess);
				resultCountList.add(orcPictureResult);
			}
			
			model.addAttribute("userKey", userKey);
			model.addAttribute("resultCountList", resultCountList);
			model.addAttribute("target", target);
			model.addAttribute("searchMonth", searchMonth);
			return "picture/batchOrcPictureViewResult";		
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
		
}
