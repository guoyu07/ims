package com.xuexibao.ops.task;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.xuexibao.ops.dao.IRecognitionPictureDao;
import com.xuexibao.ops.model.RecognitionPicture;

//@Component
public class ResetPictureRequestNumTask {
	
	private static Logger logger = LoggerFactory.getLogger("artificial_recognition_log");

	@Resource
	IRecognitionPictureDao recognitionPictureDao;
	/**
	 * 凌晨6点执行， 重置5点之前的死图片（被用户请求过，但是并没有提交识别结果）
	 */
	@Scheduled(cron = "0 0 6 * * ?")
	private void resetRequestNum() {
		long startTime = System.currentTimeMillis();
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date oneHourEarlier = calendar.getTime();
		
		List<Long> needResetRequestIds = recognitionPictureDao.needResetRequestIds(oneHourEarlier);
		
		logger.info("[BEGIN] Need Filter Number: {}", needResetRequestIds.size());
		needResetRequestIds = filterResetRequestIds(needResetRequestIds);
		logger.info("[AFTER] After Filter Number: {}", needResetRequestIds.size());
		int num = recognitionPictureDao.resetRequestNum(needResetRequestIds);
		logger.info("[E N D] Update Number: {}, 执行{}毫秒", num, System.currentTimeMillis() - startTime);
	}
	private List<Long> filterResetRequestIds(List<Long> needResetRequestIds) {
		Map<Long, RecognitionPicture> map = new HashMap<>();
		for(RecognitionPicture picture : ObtainRemainPictureTask.pictureList) {
			map.put(picture.getId(), picture);
		}
		Long id = null;
		for(Iterator<Long> iterator = needResetRequestIds.iterator(); iterator.hasNext(); ) {
			id = iterator.next();
			if(map.containsKey(id)) {
				iterator.remove();
			}
		}
		
		return needResetRequestIds;
	}
}
