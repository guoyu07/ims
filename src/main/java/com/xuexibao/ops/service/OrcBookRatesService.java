package com.xuexibao.ops.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.CommonConstant;
import com.xuexibao.ops.dao.IBooksDao;
import com.xuexibao.ops.dao.IOrcBookRatesDao;
import com.xuexibao.ops.dao.IOrganizationSourcesDao;
import com.xuexibao.ops.model.Books;
import com.xuexibao.ops.model.OrcBookRates;
import com.xuexibao.ops.util.MathUtils;
import com.xuexibao.ops.util.http.HttpSubmit;

@Service
public class OrcBookRatesService {
	@Resource
	IBooksDao booksDao;
	@Resource
	IOrcBookRatesDao orcBookRatesDao;
	@Resource
	IOrganizationSourcesDao organizationSourcesDao;

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
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}
	}
	private String setOrcRate(String rate) {
		Double result = Double.valueOf(rate)*100;
		String rates=String.valueOf(result.intValue());	
		return rates;
	}
	public List<OrcBookRates> searchList(String name, String isbn,
			String operator, String teamName, Integer teamId, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		List<OrcBookRates> orcBookRatesList = orcBookRatesDao.searchList(name,isbn,
				operator,teamName,teamId, status,startDate,endDate,
				page, limit);
		BigDecimal rate=new BigDecimal(0);
		for(OrcBookRates bookrates : orcBookRatesList){
			setSourceId(bookrates.getBooks());
			if((bookrates.getOrcUpload() - bookrates.getOrcUndealt()) == 0){
			}else{
				rate=BigDecimal.valueOf(MathUtils.div((double)bookrates.getOrcRight(), (double)(bookrates.getOrcUpload() - bookrates.getOrcUndealt()), 2));															
			}
			bookrates.setOrcRate(setOrcRate(String.valueOf(rate)));
		}
		
		return orcBookRatesList;
	}
	public long searchCount(String name, String isbn, String operator, 
			String teamName, Integer teamId, Integer status, Date startDate, Date endDate) {
		return orcBookRatesDao.searchCount(name, isbn, operator, 
				teamName,teamId,status,startDate, endDate);
	}

	

	
}
