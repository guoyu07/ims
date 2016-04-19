package com.xuexibao.ops.service;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.xuexibao.ops.dao.IRecognitionAnalysisDao;
import com.xuexibao.ops.model.RecognitionAnalysis;

@Service
public class RecognitionAnalysisService {
	@Resource
	IRecognitionAnalysisDao recognitionAnalysisDao;

	public long searchCount(String operater, Date startDate, Date endDate) {
		return recognitionAnalysisDao.searchCount(operater, startDate, endDate);
	}

	public List<RecognitionAnalysis> searchList(String operater, Date startDate, Date endDate, Long page, int limit) {
		List<RecognitionAnalysis> recognitionAnalysisList = recognitionAnalysisDao.searchList(operater, startDate, endDate, page, limit);
		return recognitionAnalysisList;
	}

	public void updatePicture(String operator, Integer[] statusArray) {
		Integer successNum = 0;
		Integer failNum = 0;
		for (int i = 0; i < statusArray.length; i++) {
			if (statusArray[i].equals(1)) {
				successNum++;
			} else if (statusArray[i].equals(-1)) {
				failNum++;
			}
		}
		RecognitionAnalysis recogAnalysis = recognitionAnalysisDao.getByOperatorName(operator);
		if (null == recogAnalysis) {
			RecognitionAnalysis recognitionAnalysis = new RecognitionAnalysis(operator, successNum, failNum, successNum + failNum);
			recognitionAnalysisDao.insertSelective(recognitionAnalysis);
		} else {
//			successNum += recogAnalysis.getSuccessNum();
//			failNum += recogAnalysis.getFailNum();
			RecognitionAnalysis recognitionAnalysis = new RecognitionAnalysis(operator, successNum, failNum, successNum + failNum);
			recognitionAnalysisDao.update(recognitionAnalysis);
		}
	}
}
