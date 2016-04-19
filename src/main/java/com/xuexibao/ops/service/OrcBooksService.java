package com.xuexibao.ops.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.CommonConstant;
import com.xuexibao.ops.dao.IBooksDao;
import com.xuexibao.ops.dao.IOrcBooksDao;
import com.xuexibao.ops.dao.IOrganizationSourcesDao;
import com.xuexibao.ops.enumeration.EnumOrcResult;
import com.xuexibao.ops.model.Books;
import com.xuexibao.ops.model.OrcBooks;
import com.xuexibao.ops.util.DateUtils;
import com.xuexibao.ops.util.http.HttpSubmit;

@Service
public class OrcBooksService {
	@Resource
	IBooksDao booksDao;
	@Resource
	IOrcBooksDao orcBooksDao;
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
	
	private void setStatus(OrcBooks orcBooks) {
		Integer status = orcBooks.getStatus();
    	if(status != null) {
    		for(EnumOrcResult orcStatus : EnumOrcResult.values()) {
    			if(status.equals(orcStatus.getId())) {
    				orcBooks.setStatusStr(orcStatus.getDesc());
    				break;
    			}
    		}
    	}
    }
	public List<OrcBooks> searchList(String name, String isbn,
			String operator, String teamName, Integer teamId, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		List<OrcBooks> orcBooksList = orcBooksDao.searchList(name,isbn,
				operator,teamName,teamId, status,startDate,endDate,
				page, limit);
		for(OrcBooks books:orcBooksList){
			setSourceId(books.getBooks());
			setStatus(books);
		}
		
		return orcBooksList;
	}
	
	public List<OrcBooks> searchList(String name, String isbn,
			String operator, String teamName, Integer teamId, Integer status, Date startDate, Date endDate) {
		List<OrcBooks> orcBooksList = orcBooksDao.searchList(name,isbn,
				operator,teamName,teamId, status,startDate,endDate);
		for(OrcBooks books:orcBooksList){
			setSourceId(books.getBooks());
			setStatus(books);
		}
		
		return orcBooksList;
	}
	
	public long searchCount(String name, String isbn, String operator, 
			String teamName, Integer teamId, Integer status, Date startDate, Date endDate) {
		return orcBooksDao.searchCount(name, isbn, operator, 
				teamName,teamId,status,startDate, endDate);
	}
	public int updateStatus(Long id, Integer status, String operatorName)throws Exception{
		int count= orcBooksDao.updateStatus(id,status,operatorName);	 
		return count;
	}
	public OrcBooks getById(Long Id){
		OrcBooks orcBooks=orcBooksDao.getById(Id);
		return orcBooks;
	}
	
	public Workbook save2Excel(List<OrcBooks> OrcBooksList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		sheet.setDefaultColumnWidth(20);

		int rows = OrcBooksList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("书名");
		cell = row.createCell(1);
		cell.setCellValue("ISBN");
		cell = row.createCell(2);
		cell.setCellValue("出版社");
		cell = row.createCell(3);
		cell.setCellValue("来源");
		cell = row.createCell(4);
		cell.setCellValue("录题人");
		cell = row.createCell(5);
		cell.setCellValue("所在小组");
		cell = row.createCell(6);
		cell.setCellValue("开始识别时间");
		cell = row.createCell(7);
		cell.setCellValue("完成识别时间");
		cell = row.createCell(8);
		cell.setCellValue("状态");
		
		for (int i = 1; i <= rows; i++) {
			OrcBooks orcBooksInfo = OrcBooksList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);
			cell.setCellValue(orcBooksInfo.getBooks().getName());
			cell = row.createCell(1);
			cell.setCellValue(orcBooksInfo.getBooks().getIsbn());
			cell = row.createCell(2);
			cell.setCellValue(orcBooksInfo.getBooks().getPublishingHouse());
			cell = row.createCell(3);
			cell.setCellValue(orcBooksInfo.getBooks().getSourceId());
			cell = row.createCell(4);
			cell.setCellValue(orcBooksInfo.getOperatorName());
			cell = row.createCell(5);
			cell.setCellValue(orcBooksInfo.getTikuTeam().getName());
			cell = row.createCell(6);
			cell.setCellValue(DateUtils.formatDate(orcBooksInfo.getOperatorStartime()));
			cell = row.createCell(7);
			cell.setCellValue(DateUtils.formatDate(orcBooksInfo.getOperatorEndtime()));
			cell = row.createCell(8);
			cell.setCellValue(orcBooksInfo.getStatusStr());

		}

		return workBook;
	}
	
}
