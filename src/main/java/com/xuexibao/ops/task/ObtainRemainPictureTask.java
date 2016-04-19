package com.xuexibao.ops.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.xuexibao.ops.dao.IRecognitionPictureDao;
import com.xuexibao.ops.model.RecognitionPicture;

//@Component
public class ObtainRemainPictureTask {
	
	private static Logger logger = LoggerFactory.getLogger("artificial_recognition_log");
	
	private static final int MAX_SIZE = 50000;
	private static final int PER_SIZE = 1000;
	public static LinkedBlockingDeque<RecognitionPicture> pictureList = new LinkedBlockingDeque<>(MAX_SIZE);
	
	@Resource
	IRecognitionPictureDao recognitionPictureDao;
	
	// 每15秒执行一次
	@Scheduled(cron = "0/15 * *  * * ? ")
	private void obtainRemainPictureList() {
		long startTime = System.currentTimeMillis();
		logger.info("[BEGIN] Picture Pool SIZE: {}", pictureList.size());
		int currentRemain = MAX_SIZE - pictureList.size();
		int limit = currentRemain > PER_SIZE ? PER_SIZE : currentRemain;
		// 优先status = 1
		List<RecognitionPicture> results = recognitionPictureDao.obtainRemainPictureList(limit);
		if (results.size() < limit) {
			// 继续请求status = 0
			List<RecognitionPicture> alternateResults = recognitionPictureDao.obtainAlternateRemainPictureList(limit - results.size());
			results.addAll(alternateResults);
		}
		updateRequestNum(results);
		pictureList.addAll(results);
		logger.info("[E N D] Picture Pool SIZE: {}, 执行{}毫秒", pictureList.size(), System.currentTimeMillis() - startTime);
	}
	
	private void updateRequestNum(List<RecognitionPicture> results) {
		Date date = new Date();
		for(RecognitionPicture picture : results) {
			picture.setRequest_time(date);
			picture.setRequest_num(1);
		}
		recognitionPictureDao.updateRequestNum(results);
	}
}
