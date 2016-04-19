package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.IUnRecognitionHistoryDao;
import com.xuexibao.ops.model.RecognitionHistory;

@Service
public class UnRecognitionHistoryService {
	@Resource
	IUnRecognitionHistoryDao unRecognitionHistoryDao;
	
	public long searchCount(String operater, Long pictureId, Date startDate, Date endDate) {
		return unRecognitionHistoryDao.searchCount(operater, pictureId, startDate, endDate);
	}

	public List<RecognitionHistory> searchList(Long pictureId, Integer status, Date startDate, Date endDate, Long page, int limit) {
		List<RecognitionHistory> unRecognitionHistoryList = unRecognitionHistoryDao.searchList(pictureId, status,startDate, endDate,
				page, limit);
		return unRecognitionHistoryList;
	}
}
