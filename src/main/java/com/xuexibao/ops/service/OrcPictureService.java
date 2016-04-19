package com.xuexibao.ops.service;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import javax.annotation.Resource;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.xuexibao.ops.constant.OrcPictureCheckStatusConstant;
import com.xuexibao.ops.dao.IBooksDao;
import com.xuexibao.ops.dao.IOrcBookRatesDao;
import com.xuexibao.ops.dao.IOrcBooksDao;
import com.xuexibao.ops.dao.IOrcPictureDao;
import com.xuexibao.ops.dao.ITranOpsDao;
import com.xuexibao.ops.dao.ITranOpsListDao;
import com.xuexibao.ops.enumeration.EnumSubject;
import com.xuexibao.ops.enumeration.OrcPictureCheckStatus;
import com.xuexibao.ops.enumeration.OrcPictureCheckTarget;
import com.xuexibao.ops.model.Books;
import com.xuexibao.ops.model.OrcAnalysisBydate;
import com.xuexibao.ops.model.OrcBookRates;
import com.xuexibao.ops.model.OrcBooks;
import com.xuexibao.ops.model.OrcPicture;
import com.xuexibao.ops.model.TranOps;
import com.xuexibao.ops.model.TranOpsList;
import com.xuexibao.ops.util.FileUtil;
import com.xuexibao.ops.util.MathUtils;
import com.xuexibao.ops.util.RecoJson;
import com.xuexibao.ops.web.FileUploadController;


@Service
public class OrcPictureService {
	
	@Resource
	IOrcPictureDao orcPictureDao;
	@Resource
	ITranOpsListDao tranOpsListDao;
	@Resource
	IOrcBooksDao orcBooksDao;
	@Resource
	IOrcBookRatesDao orcBookRatesDao;
	@Resource
	IBooksDao booksDao;
	
	
	private static Logger logger = LoggerFactory.getLogger("machine_recognition_log");

	@Resource		
	ITranOpsDao tranOpsDao;
	
	
	public OrcPicture getOrcPictureById(Long pictureId) {
		OrcPicture orcPicture = orcPictureDao.getById(pictureId);
		if(orcPicture != null) {
			setSubject(orcPicture);
			setStatus(orcPicture);
			String knowledge = orcPicture.getKnowledge();
			if(StringUtils.isNotEmpty(knowledge)) {
				orcPicture.setKnowledgeArray(knowledge.split(","));
			}
		}
		return orcPicture;
	}
	
	public OrcPicture getNextPicture(Long pictureId, String operatorName, Integer status, Date startDate, Date endDate) {
		OrcPicture orcPicture = orcPictureDao.getNextById(pictureId, operatorName, status, startDate, endDate);
		if(orcPicture != null) {
			setSubject(orcPicture);
			setStatus(orcPicture);
			String knowledge = orcPicture.getKnowledge();
			if(StringUtils.isNotEmpty(knowledge)) {
				orcPicture.setKnowledgeArray(knowledge.split(","));
			}
		}
		return orcPicture;
	}
	
	public OrcPicture getLastPicture(Long pictureId, String operatorName, Integer status, Date startDate, Date endDate) {
		OrcPicture orcPicture = orcPictureDao.getLastById(pictureId, operatorName, status, startDate, endDate);
		if(orcPicture != null) {
			setSubject(orcPicture);
			setStatus(orcPicture);
			String knowledge = orcPicture.getKnowledge();
			if(StringUtils.isNotEmpty(knowledge)) {
				orcPicture.setKnowledgeArray(knowledge.split(","));
			}
		}
		return orcPicture;
	}
	
	public boolean editOrcPicture(Long id, String approvor, Integer operatorTeamId, Integer status ,TranOps  tranOps) {
		return updateOrcPicture(id,  approvor,  operatorTeamId,  status, tranOps);
	}
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	private boolean updateOrcPicture(Long id, String operatorName, Integer operatorTeamId, Integer status, TranOps tranOps) {
		// 通过id查询tranOps，不存在则退出此次循环
		final OrcPicture orcPicture = orcPictureDao.getById(id);
		if(orcPicture == null) {
			return false;
		}
		final TranOps tranOpsN = tranOpsDao.getByPictureId(id);
		if( tranOpsN != null) {
			return false;
		}
		//如果状态是识别错误需要人工录入，则插入识别的修正结果
		if(status == OrcPictureCheckStatusConstant.ERROR_RECORD || status == OrcPictureCheckStatusConstant.USOLVE_ERROR) {
			if(tranOps != null) {
				//插入试题列表tranOps
				tranOpsDao.insertSelective(tranOps);
			} else {
				return false;
			}
			//如果识别错误，准备录入或未录入，修改处理数-1，重新计算识别率
			if(orcPicture.getBookId() != null) {
				//取出统计记录进行  正确数累加1，处理数减1，上传数不变，识别率重新计算
				Long bookId = orcPicture.getBookId();
				OrcBookRates orcbookrates = orcBookRatesDao.getByBookId(bookId);
				BigDecimal rate = new BigDecimal(0);
				//				 rate = MathUtils.div(orcbookrates.getOrcRight(), orcbookrates.getOrcUpload() - orcbookrates.getOrcUndealt() - 1, 2);							
				OrcBookRates orcbookrate=new OrcBookRates(orcbookrates.getId(),bookId, Long.valueOf(orcbookrates.getOrcRight().longValue()), Long.valueOf(orcbookrates.getOrcUndealt().longValue()-1), Long.valueOf(orcbookrates.getOrcUpload().longValue()), String.valueOf(rate));
				orcBookRatesDao.updateIfNecessary(orcbookrate);
				Books book=booksDao.getById(bookId);
				if( book != null && book.getBest() == 1 && orcPicture.getStatus() == 5){
					status = OrcPictureCheckStatusConstant.BEST_RECORD;
				}
			}
		}
		if(status ==  OrcPictureCheckStatusConstant.SOLVE_RIGHT) {//如果识别正确，修改统计结果，识别率
			if(orcPicture.getBookId() != null) {
				 //取出统计记录进行  正确数累加1，处理数减1，上传数不变，识别率重新计算
				 Long bookId = orcPicture.getBookId();
				 OrcBookRates orcbookrates=orcBookRatesDao.getByBookId(bookId);
				 BigDecimal rate = new BigDecimal(0);
//				 rate = MathUtils.div(orcbookrates.getOrcRight() + 1, orcbookrates.getOrcUpload() - orcbookrates.getOrcUndealt() - 1, 2);							
				 OrcBookRates orcbookrate = new OrcBookRates(orcbookrates.getId(),bookId, Long.valueOf(orcbookrates.getOrcRight().longValue()+1), Long.valueOf(orcbookrates.getOrcUndealt().longValue()-1), Long.valueOf(orcbookrates.getOrcUpload().longValue()), String.valueOf(rate));
				 orcBookRatesDao.updateIfNecessary(orcbookrate);
				 
				 // 识别正确 --> 是精品书 --> 插入tiku_question表---此流程已修改为，精品题识别正确，还是需要人工录入
//				 TencentQuestion question = new TencentQuestion(orcPicture);
//				 tencentQuestionDao.insertSelective(question);				 
				 Books book=booksDao.getById(bookId);
				 if( book != null && book.getBest() == 1){
					 status = OrcPictureCheckStatusConstant.BEST_SOLVE_RIGHT;
				 } 
			}
		} else if(status == OrcPictureCheckStatusConstant.ERROR_UNRECORD) {
			//如果识别错误，准备录入或未录入，修改处理数-1，重新计算识别率
			if(orcPicture.getBookId() != null){
				 //取出统计记录进行  正确数累加1，处理数减1，上传数不变，识别率重新计算
				 Long bookId = orcPicture.getBookId();
				 OrcBookRates orcbookrates = orcBookRatesDao.getByBookId(bookId);
				 BigDecimal rate = new BigDecimal(0);
//				 rate = MathUtils.div(orcbookrates.getOrcRight(), orcbookrates.getOrcUpload() -orcbookrates.getOrcUndealt() - 1, 2);							
				 OrcBookRates orcbookrate = new OrcBookRates(orcbookrates.getId(),bookId, Long.valueOf(orcbookrates.getOrcRight().longValue()), Long.valueOf(orcbookrates.getOrcUndealt().longValue()-1), Long.valueOf(orcbookrates.getOrcUpload().longValue()), String.valueOf(rate));
				 orcBookRatesDao.updateIfNecessary(orcbookrate);
			 }
		} else if(status == OrcPictureCheckStatusConstant.BEST_SOLVE_RIGHT) {
			if(orcPicture.getBookId() != null) {
				Long bookId = orcPicture.getBookId();
				Books book=booksDao.getById(bookId);
				if( book != null && book.getBest() == 1){
					status = OrcPictureCheckStatusConstant.BEST_RECORD;
				} 
			}
		}
		// 更新OrcPicture
		orcPicture.setStatus(status);
		orcPicture.setOperatorName(operatorName);
		orcPicture.setOperatorTeamId(operatorTeamId);
		orcPicture.setApproveTime(new Date());
		orcPictureDao.updateIfNecessary(orcPicture);
		return true;
	}
	
	public long searchCount(Long pictureId,  String userKey, Integer status,  Date startDate, Date endDate) {
		return orcPictureDao.searchCount(pictureId,  userKey,
				status, startDate, endDate);
	}

	public long searchCount(Long pictureId, Long bookId, String userKey, Integer status,  Date startDate, Date endDate) {
		return orcPictureDao.searchCount(pictureId, bookId, userKey,
				status, startDate, endDate);
	}
	
	public long searchCount(Long pictureId, List<Long> bookIds, String userKey, Integer status,  Date startDate, Date endDate) {
		return orcPictureDao.searchCount(pictureId, bookIds, userKey,
				status, startDate, endDate);
	}
	
	public List<OrcPicture> searchList(Long pictureId,  String userKey, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		List<OrcPicture> orcPictureList = orcPictureDao.searchList(pictureId, userKey, status, startDate, endDate,
				page, limit);
		for(OrcPicture orcPicture : orcPictureList) {
//			setSubject(orcPicture);
			setStatus(orcPicture);

		}
		return orcPictureList;
	}
	
	public List<OrcPicture> searchList(Long pictureId, Long bookId, String userKey, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		List<OrcPicture> orcPictureList = orcPictureDao.searchList(pictureId, bookId, userKey, status, startDate, endDate,
				page, limit);
		for(OrcPicture orcPicture : orcPictureList) {
//			setSubject(orcPicture);
			setStatus(orcPicture);

		}
		return orcPictureList;
	}
	
	public List<OrcPicture> searchList(Long pictureId, List<Long> bookIds, String userKey, Integer status, Date startDate, Date endDate,
			Long page, int limit) {
		List<OrcPicture> orcPictureList = orcPictureDao.searchList(pictureId, bookIds, userKey, status, startDate, endDate,
				page, limit);
		for(OrcPicture orcPicture : orcPictureList) {
//			setSubject(orcPicture);
			setStatus(orcPicture);

		}
		return orcPictureList;
	}
	public long searchOrcCount( Date startDate, Date endDate) {
		return orcPictureDao.searchOrcCount(startDate, endDate);
	}
	public List<OrcAnalysisBydate> searchListCount( Date startDate, Date endDate,
			Long page, int limit) {
		List<OrcAnalysisBydate> orcList = orcPictureDao.searchListCount(startDate, endDate,
				page, limit);

		return orcList;
	}
	public List<OrcAnalysisBydate> saveSearchListCount( Date startDate, Date endDate) {
		List<OrcAnalysisBydate> orcList = orcPictureDao.saveSearchListCount(startDate, endDate);

		return orcList;
	}
	
	private void setStatus(OrcPicture picture) {
		Integer status = picture.getStatus();
    	if(status != null) {
    		for(OrcPictureCheckStatus pictureStatus : OrcPictureCheckStatus.values()) {
    			if(status.equals(pictureStatus.getId())) {
    				picture.setStatusStr(pictureStatus.getDesc());
    				break;
    			}
    		}
    	}
    }
	private void setSubject(OrcPicture picture) {
    	Integer subject = picture.getRealSubject();
		if(subject != null) {
			for(EnumSubject enumsubject : EnumSubject.values()) {
				if(subject.equals(enumsubject.getId())) {
					picture.setSubject(enumsubject.getChineseName());
					break;
				}
			}
		}
	}

	public int getRecogResultAndSave(String image_id, String userKey){
		int ret = 0;
		try{
			
			OrcPicture orcPicture = null;
			
			JSONObject recogObj = RecoJson.getRecogeResult(image_id);
			
			if(recogObj == null ){
				logger.error("getRecogResultAndSave failed, no data.");
				return -1;
			}
			else if(recogObj.getInteger("status") != 0){
				orcPicture = setNoresOrcPicture(recogObj, userKey);
				if(orcPicture == null){
					logger.error("get image url null.");
					return -1;
				}
			}
			else{
				orcPicture = newOrcPictureData(recogObj, userKey);
			}	
			
			if(orcPicture != null){
				orcPictureDao.insertSelective(orcPicture);
			}
		
		}catch(Exception e){
			logger.error("getRecogResultAndSave failed,", e);
			return -1;
		}
		
		return ret;
	}
	
	public synchronized boolean getRecogResultAndSave(String image_id, String userKey, Long bookId) {
		boolean ret = true;
		try {
			OrcPicture orcPicture = null;
			JSONObject recogObj = RecoJson.getRecogeResult(image_id);
			if(recogObj == null) {
				logger.error("getRecogResultAndSave failed, no data.");
				return false;
			}
			if(recogObj.getInteger("status") != 0) {
				orcPicture = setNoresOrcPicture(recogObj, userKey, bookId);
				if(orcPicture == null) {
					logger.error("get image url null.");
					return false;
				}
			} else {
				orcPicture = newOrcPictureData(recogObj, userKey, bookId);
			}	
			
			if(orcPicture != null) {
				orcPictureDao.insertSelective(orcPicture);
				OrcBooks orcbooks = orcBooksDao.getByBookId(bookId);
				if(orcbooks != null) {
					//上传书籍图片 插入上传数量--如果orc_books 已经存在该书籍并且 识别人是相同的人，只更新orc_book_rates orc_upload，orc_undealt字段
					if(StringUtils.isNotEmpty(orcbooks.getOperatorName()) && (orcbooks.getOperatorName()).equals(userKey)) {
						 //取出统计记录进行累加
						 OrcBookRates orcbookrates = orcBookRatesDao.getByBookId(bookId);
						 BigDecimal rate = new BigDecimal(0);
						 if(orcbookrates != null) {
							 if((orcbookrates.getOrcUndealt()).equals(orcbookrates.getOrcUpload())) {
								 rate = new BigDecimal(0);
							 } else {
//								 rate = MathUtils.div(orcbookrates.getOrcRight(), orcbookrates.getOrcUpload() - orcbookrates.getOrcUndealt(), 2);							
							 }
							 OrcBookRates orcbookrate = new OrcBookRates(orcbookrates.getId(),bookId, orcbookrates.getOrcRight().longValue(), 
									 orcbookrates.getOrcUndealt() + 1, orcbookrates.getOrcUpload() + 1, String.valueOf(rate));
							 orcBookRatesDao.updateIfNecessary(orcbookrate);	
						 }
					} else {
						//上传书籍图片 插入上传数量--如果orc_books 已经存在该书籍并且 识别人不是是相同的人，更新orc_books 的 录题人，只更新orc_book_rates orc_upload，orc_undealt字段
						
					    //更新录题人
						OrcBooks orcbook = new OrcBooks(orcbooks.getId(), null, userKey, null, null, null, null, null, new Date());
					    orcBooksDao.updateIfNecessary(orcbook);	
						//统计累加
						 OrcBookRates orcbookrates = orcBookRatesDao.getByBookId(bookId);
						 
						 BigDecimal rate = new BigDecimal(0);
						 if(orcbookrates != null) {
							 if((orcbookrates.getOrcUndealt()).equals(orcbookrates.getOrcUpload())) {
								 rate = new BigDecimal(0);
							 } else {
//								 rate = MathUtils.div(orcbookrates.getOrcRight(), orcbookrates.getOrcUpload() - orcbookrates.getOrcUndealt(), 2);							
							 }
							 OrcBookRates orcbookrate = new OrcBookRates(orcbookrates.getId(), bookId, orcbookrates.getOrcRight(), 
									 orcbookrates.getOrcUndealt() + 1, orcbookrates.getOrcUpload() + 1, String.valueOf(rate));
							 orcBookRatesDao.updateIfNecessary(orcbookrate);
						 }						 										
					}
					
				} else {
					//上传书籍图片 插入上传数量--如果orc_books 没有该书籍， 插入一条记录，并且插入 orc_book_rates 一条记录 
					OrcBooks orcbook = new OrcBooks(bookId, userKey, new Date(), null, 0, null, new Date(), new Date());
				    orcBooksDao.insertSelective(orcbook);				    
				    OrcBookRates orcbookrate = new OrcBookRates(bookId, 0L, 1L, 1L, String.valueOf(0));
				    orcBookRatesDao.insert(orcbookrate);
				}
				
			}
		
		} catch(Exception e) {
			e.printStackTrace();
			logger.error("getRecogResultAndSave failed,", e);
			return false;
		}
		
		return ret;
	}
	private OrcPicture setNoresOrcPicture(JSONObject recogObj, String userKey){
		
		if(recogObj.getString("image_url_jpg") == null || StringUtils.isEmpty(recogObj.getString("image_url_jpg"))){
			return null;
		}
		
		OrcPicture orcPicture = new OrcPicture();
		orcPicture.setStatus(4);
		orcPicture.setUserKey(userKey);
		orcPicture.setCreateTime(new Date());
		orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg")); //for test,"http://xfsr.91xuexibao.com/7,13c8959e91c96e"; 
		return orcPicture;
	}
	
	private OrcPicture setNoresOrcPicture(JSONObject recogObj, String userKey, Long bookId) {
		
		if(StringUtils.isEmpty(recogObj.getString("image_url_jpg"))) {
			return null;
		}
		OrcPicture orcPicture = new OrcPicture();
		orcPicture.setStatus(4);
		orcPicture.setBookId(bookId);
		orcPicture.setUserKey(userKey);
		orcPicture.setCreateTime(new Date());
		orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg")); //for test,"http://xfsr.91xuexibao.com/7,13c8959e91c96e"; 
		return orcPicture;
	} 

	
	public static class UTF8PostMethod extends PostMethod{   
		  public UTF8PostMethod(String url){   
			  super(url);   
		  }   
	}
	
	private OrcPicture newOrcPictureData(JSONObject recogObj,  String userKey){
		OrcPicture orcPicture = new OrcPicture();
		JSONObject resultObj = recogObj.getJSONObject("result");
		    		  
	    JSONObject answerObj = resultObj.getJSONArray("answers").getJSONObject(0);	    
		orcPicture.setStatus(0);
		orcPicture.setUserKey(userKey);
		orcPicture.setCreateTime(new Date());
		orcPicture.setAnswer(answerObj.getString("question_answer"));
		orcPicture.setContent(answerObj.getString("question_body_html"));
		orcPicture.setLatex(answerObj.getString("question_body"));
		orcPicture.setSolution(answerObj.getString("answer_analysis"));
		orcPicture.setQuestionId(Long.parseLong(answerObj.getString("question_id")));		
		orcPicture.setKnowledge(answerObj.getString("question_tag"));
		orcPicture.setRealSubject(Integer.parseInt(answerObj.getString("subject")));
		
		orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg"));

		return orcPicture;		
	}
	private OrcPicture newOrcPictureData(JSONObject recogObj,  String userKey , Long bookId){
		OrcPicture orcPicture = new OrcPicture();
		JSONObject resultObj = recogObj.getJSONObject("result");
		    		  
	    JSONObject answerObj = resultObj.getJSONArray("answers").getJSONObject(0);	    
		orcPicture.setStatus(0);
		orcPicture.setUserKey(userKey);
		orcPicture.setBookId(bookId);
		
		orcPicture.setCreateTime(new Date());
		orcPicture.setAnswer(answerObj.getString("question_answer"));
		orcPicture.setContent(answerObj.getString("question_body_html"));
		orcPicture.setLatex(answerObj.getString("question_body"));
		orcPicture.setSolution(answerObj.getString("answer_analysis"));
		orcPicture.setQuestionId(Long.parseLong(answerObj.getString("question_id")));		
		orcPicture.setKnowledge(answerObj.getString("question_tag"));
		orcPicture.setRealSubject(Integer.parseInt(answerObj.getString("subject")));
		
		orcPicture.setOrcPictureUrl(recogObj.getString("image_url_jpg"));

		return orcPicture;		
	}
	public long getHasAnswerCount(Long questionId) {
		return orcPictureDao.getHasAnswerCount(questionId);
	}
	
	public List<OrcPicture> searchHasAnswerList(Long questionId) {
		List<OrcPicture> orcPictureList = orcPictureDao.searchHasAnswerList(questionId);
		for(OrcPicture orcPicture : orcPictureList) {
			setStatus(orcPicture);

		}
		return orcPictureList;
	}
	
	public Workbook save2Excel(List<OrcAnalysisBydate> orcList) {
		Workbook workBook = new HSSFWorkbook();
		Sheet sheet = workBook.createSheet();
		sheet.setDefaultColumnWidth(20);

		int rows = orcList.size();
		Row row = sheet.createRow(0);
		row.setHeightInPoints(20);
		Cell cell = row.createCell(0);
		cell.setCellValue("录题人");
		cell = row.createCell(1);
		cell.setCellValue("未处理数量");
		cell = row.createCell(2);
		cell.setCellValue("识别正确数量");
		cell = row.createCell(3);
		cell.setCellValue("已录入数量");

		
		for (int i = 1; i <= rows; i++) {
			OrcAnalysisBydate orcInfo = orcList.get(i - 1);
			row = sheet.createRow(i);
			row.setHeightInPoints(20);
			cell = row.createCell(0);
			if(orcInfo != null){
			if(orcInfo.getOperator() != null){
				cell.setCellValue(orcInfo.getOperator());
			}
			
			cell = row.createCell(1);
			if(orcInfo.getCnt_not() != null){
				cell.setCellValue(orcInfo.getCnt_not());
			}
			
			cell = row.createCell(2);
			if(orcInfo.getCnt_right() != null){
				cell.setCellValue(orcInfo.getCnt_right());	
			}
			
			cell = row.createCell(3);
			if(orcInfo.getCnt_finish() != null){
				cell.setCellValue(orcInfo.getCnt_finish());
			}
		  }
		}

		return workBook;
	}
	 //同图多应用同时识别
		public List<TranOps> batchRecognition(OrcPicture orcPicture){
			List<TranOps> otherRecos = new ArrayList<>();
			InputStream inStream = null;
			try{
				String fileName = UUID.randomUUID().toString();
				String uploadfile = orcPicture.getOrcPictureUrl();
				//根据图片id优先查询是否已经二次识别过并且有返回结果
				List<TranOpsList> tranopslist= tranOpsListDao.getByPictureId(orcPicture.getId());
		
				if(tranopslist != null && tranopslist.size()>0){
					
					for(TranOpsList tranops: tranopslist){	
						TranOps otherORC = new TranOps();
						otherORC.setAnswer(tranops.getAnswer());
						otherORC.setContent(tranops.getContent());
						otherORC.setSolution(tranops.getSolution());
						otherORC.setTarget(tranops.getTarget());	
						otherRecos.add(otherORC);
					}
				}else{
					//upload file
					URL url = new URL(uploadfile);
					HttpURLConnection conn = (HttpURLConnection)url.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(10 * 1000);
					inStream = conn.getInputStream();

					File local = FileUploadController.copyFile(inStream, fileName);
					String downloadUrl = FileUtil.upload(local);
					local.delete();
					String pictureId = downloadUrl.replace(FileUtil.file_download_url, "");
					
					//多线程处理 
					CountDownLatch runningThreadNum = new CountDownLatch(1);
					Integer seed = (int)(Math.random() * 10);
					if( (seed % 2) == 0) {
						new OrcPictureOtherRecoThreadService(runningThreadNum, OrcPictureCheckTarget.XUEBAJUN, pictureId, orcPicture.getId(),tranOpsListDao).start();
					} else {
						new OrcPictureOtherRecoThreadService(runningThreadNum, OrcPictureCheckTarget.XIAOYUANSOUTI, pictureId, orcPicture.getId(),tranOpsListDao).start();
					}
					runningThreadNum.await();
					
					List<TranOpsList> tranopslist_new = tranOpsListDao.getByPictureId(orcPicture.getId());
					if(tranopslist_new != null && tranopslist_new.size()>0){
						for(TranOpsList tranops: tranopslist_new){	
							TranOps otherORC = new TranOps();
							otherORC.setAnswer(tranops.getAnswer());
							otherORC.setContent(tranops.getContent());
							otherORC.setSolution(tranops.getSolution());
							otherORC.setTarget(tranops.getTarget());	
							otherRecos.add(otherORC);
						}
					}
				}		    		
				return otherRecos;
			}catch(Exception e)
			{
				e.printStackTrace();
				return null;
			}
		}

}
