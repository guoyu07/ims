package com.xuexibao.ops.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.xuexibao.ops.dao.IRecognitionAnalysisBydateDao;
import com.xuexibao.ops.dao.IRecognitionPictureDao;
import com.xuexibao.ops.dao.IRecognitionPictureMediumDao;
import com.xuexibao.ops.model.RecognitionAnalysisBydate;
import com.xuexibao.ops.model.RecognitionPicture;
import com.xuexibao.ops.model.RecognitionPictureMedium;

//@Component
public class RecognitionAnalysisNonBatchTask {

	private static final int INSERT_PER_SIZE = 3000;

	private static Logger logger = LoggerFactory.getLogger("artificial_recognition_log");

	@Resource
	IRecognitionPictureMediumDao recognitionPictureMediumDao;
	@Resource
	IRecognitionAnalysisBydateDao recognitionAnalysisBydateDao;
	@Resource
	IRecognitionPictureDao recognitionPictureDao;

	public static List<RecognitionPictureMedium> recognitionPictureMediumList;

	public static List<RecognitionAnalysisBydate> recognitionAnalysisBydateList = new ArrayList<>();

	public static Date getDay(Date date, int flag) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, flag);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}

	public static Date getRecognitionTime(Date date, int flag) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, flag);
		calendar.set(Calendar.HOUR_OF_DAY, 12);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		date = calendar.getTime();
		return date;
	}

	@Scheduled(cron = "14 13 01  * * ? ")
	private void obtainYesterdayData() {
		logger.info("开始执行RecognitionPictureMedium:{}", System.currentTimeMillis());
		// 清空前一天数据
		recognitionPictureMediumDao.clearDatabase();
		Date date = new Date();
		Date yesterdayDawn = getDay(date, -1);
		Date todayDawn = getDay(date, 0);
		List<RecognitionPicture> yesterdayDataList = recognitionPictureDao.obtainYesterdayData(todayDawn, yesterdayDawn);
		if (yesterdayDataList != null && !yesterdayDataList.isEmpty()) {
			logger.info("yesterdayDataList.size():{}", yesterdayDataList.size());
			RecognitionPictureMedium[] mediumPictures = new RecognitionPictureMedium[2 * yesterdayDataList.size()];
			int listSize=yesterdayDataList.size();
			int index = 0;
			RecognitionPicture recognitionPicture = null;
			for (Iterator<RecognitionPicture> it = yesterdayDataList.iterator(); it.hasNext();) {
				recognitionPicture = it.next();
				Long fileId = recognitionPicture.getId();
				String fileName = recognitionPicture.getFileName();
				String filePath = recognitionPicture.getFilePath();
				String operator1 = recognitionPicture.getOperator1();
				String operator2 = recognitionPicture.getOperator2();
				Date recognitionTime = recognitionPicture.getRecognitionTime();
				Integer status = recognitionPicture.getStatus();
				Date createTime = recognitionPicture.getCreateTime();
				Date updateTime = recognitionPicture.getUpdateTime();
				Date requestTime = recognitionPicture.getRequest_time();
				Integer requestNum = recognitionPicture.getRequest_num();
				mediumPictures[index] = new RecognitionPictureMedium(fileId, fileName, filePath, operator1, recognitionTime, status, createTime,
						updateTime, requestTime, requestNum);

				mediumPictures[index + listSize] = new RecognitionPictureMedium(fileId, fileName, filePath, operator2, recognitionTime, status, createTime,
						updateTime, requestTime, requestNum);
				index++;
			}
			logger.info("开始插入recognition_picture_medium表:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
			int i = 0;
			for (int length = mediumPictures.length / INSERT_PER_SIZE; i < length;) {
				RecognitionPictureMedium[] mediumPics1 = Arrays.copyOfRange(mediumPictures, i * INSERT_PER_SIZE, ++i * INSERT_PER_SIZE);
				recognitionPictureMediumDao.insertBatch(Arrays.asList(mediumPics1));
			}
			int length = mediumPictures.length;
			int startPos = length - length % INSERT_PER_SIZE;
			RecognitionPictureMedium[] mediumPics2 = Arrays.copyOfRange(mediumPictures, startPos, length);
			recognitionPictureMediumDao.insertBatch(Arrays.asList(mediumPics2));
			logger.info("插入recognition_picture_medium表完成:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));

			logger.info("开始获取统计数据:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
			recognitionPictureMediumList = recognitionPictureMediumDao.getAnalysisTable();
			logger.info("获取统计数据完成:{},{}", recognitionPictureMediumList.size(), com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
			for (Iterator<RecognitionPictureMedium> it = recognitionPictureMediumList.iterator(); it.hasNext();) {
				RecognitionPictureMedium recogPicMedium = it.next();
				if (null == recogPicMedium || null == recogPicMedium.getOperator() || " ".equals(recogPicMedium.getOperator()))
					continue;
				Date recogTime = getRecognitionTime(date, -1);

				recognitionAnalysisBydateList.add(new RecognitionAnalysisBydate(recogPicMedium.getOperator(), recogPicMedium.getTotalNum(),
						recogPicMedium.getCorrectNum(), recogPicMedium.getUnrecNum(), recogPicMedium.getDisunityNum(), recogTime));
			}
			logger.info("开始插入recognition_analysis_bydate表数据:{},{}", recognitionAnalysisBydateList.size(),
					com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
			recognitionAnalysisBydateDao.insertBatch(recognitionAnalysisBydateList);
			logger.info("插入recognition_analysis_bydate表完成:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
			recognitionAnalysisBydateList.clear();

		} else {
			logger.info("yesterdayDataList没有数据:{}", com.xuexibao.ops.util.DateUtils.formatDate(new Date()));
		}
	}
}
