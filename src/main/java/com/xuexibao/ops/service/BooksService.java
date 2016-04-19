package com.xuexibao.ops.service;

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
import com.xuexibao.ops.dao.IOrganizationSourcesDao;
import com.xuexibao.ops.model.Books;
import com.xuexibao.ops.util.http.HttpSubmit;

@Service
public class BooksService {
	@Resource
	IBooksDao booksDao;
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
	
	public List<Books> searchList(String name, String isbn,
			String sourceName, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		List<Books> booksList = booksDao.searchList(name,isbn,
				sourceName, status,startDate,endDate,
				page, limit);
		for(Books books:booksList){
			setSourceId(books);
		}
		
		return booksList;
	}
	public List<Books> searchList(String nameisbn) {
		List<Books> booksList = booksDao.searchList(nameisbn);
		for(Books books:booksList){
			setSourceId(books);
		}
		
		return booksList;
	}
	public long searchCount(String name, String isbn, String sourceName, 
			Integer status, Date startDate, Date endDate) {
		return booksDao.searchCount(name, isbn, sourceName, 
				status, startDate, endDate);
	}
	
	public Books insertBookInfo(Books book){
		  Books bookInfo= booksDao.addBooksInfo(book);
		  return bookInfo;
	}
	
	public Books getBookInfoById(Long Id){
		Books book=booksDao.getById(Id);
		return book;
	}
	
	public Books getById(Long Id){
		Books book=booksDao.getById(Id);
		return book;
	}
	public Books getBookInfoByName(String name){
		Books book=booksDao.getByName(name);
		return book;
	}
	
	public Long getIdByName(String name){
		Long id = booksDao.getIdByName(name);
		return id;
	}
	public int updateBookInfo(Books book) {
		int count = booksDao.updateBookInfoById(book);	 
		return count;
	}
	public boolean updateIfNecessary(Books book) {
		int updateNum = booksDao.updateIfNecessary(book);
		if (updateNum == 1)
			return true;
		return false;
	}
	
}
