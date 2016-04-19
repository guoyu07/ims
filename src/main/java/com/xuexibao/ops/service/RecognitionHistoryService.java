package com.xuexibao.ops.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.xuexibao.ops.constant.RecognitionHistoryResultsConstant;
import com.xuexibao.ops.dao.IRecognitionHistoryDao;
import com.xuexibao.ops.model.RecognitionHistory;

@Service
public class RecognitionHistoryService {
	@Resource
	IRecognitionHistoryDao recognitionHistoryDao;
	
	public long searchCount(String operator, Long pictureId, Date startDate, Date endDate) {
		return recognitionHistoryDao.searchCount(operator, pictureId, startDate, endDate);
	}

	public List<RecognitionHistory> searchList(String operater, Long pictureId, Date startDate, Date endDate, Long page, int limit) {
		List<RecognitionHistory> recognitionHistoryList = recognitionHistoryDao.searchList(operater, pictureId, startDate, endDate, page, limit);
		Iterator<RecognitionHistory> it=recognitionHistoryList.iterator();
		while(it.hasNext()){
			RecognitionHistory recognitionHistory=it.next();
			String result = recognitionHistory.getResult();
			if(StringUtils.isEmpty(result)){
				recognitionHistory.setResult(RecognitionHistoryResultsConstant.CANNOT_RECOGNIZED);
			}
		}
		return recognitionHistoryList;
	}
	
	public RecognitionHistory[] updatePicture(String operater, Long[] idArray, String[] results, Integer[] statusArray) {
		
		List<RecognitionHistory> recognitionHistoryList = new ArrayList<>();
		for (int i = 0; i < idArray.length; i++) {
			if(idArray[i] == null)
				continue;
			recognitionHistoryList.add(new RecognitionHistory(idArray[i], operater, new Date(), results[i], statusArray[i]));
		}
		RecognitionHistory[] recognitionHistorys = new RecognitionHistory[recognitionHistoryList.size()];
		
		return recognitionHistoryDao.insertSelective(recognitionHistoryList.toArray(recognitionHistorys));
	}
}
