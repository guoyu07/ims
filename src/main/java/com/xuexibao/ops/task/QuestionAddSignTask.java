package com.xuexibao.ops.task;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.xuexibao.ops.dao.impl.TencentQuestionDao;
import com.xuexibao.ops.model.TencentQuestion;

//@Component
public class QuestionAddSignTask {
	
	private static Logger logger = LoggerFactory.getLogger("question_bank_log");
	
	private static final int MAX_SIZE = 500;
	private static final int PER_SIZE = 100;
	private static final int NORMAL = 0;
	private static final int RECALL = 2;
	private static final int PROCESSING = 1;
	private static boolean recalled = false;
	public static LinkedBlockingDeque<TencentQuestion> tranOpsSignList = new LinkedBlockingDeque<>(MAX_SIZE);
	
	@Resource
	TencentQuestionDao tencentQuestionDao;
	
	// 每30秒执行一次
	@Scheduled(cron = "0/30 * * * * ? ")
	private void obtainRemainGroupList() {
		long startTime = System.currentTimeMillis();
		logger.debug("[BEGIN] TranOpsSignList Pool SIZE: {}", tranOpsSignList.size());
		
		//处理宕机数据
		if(!recalled){
			tencentQuestionDao.recallDisableData();
			recalled=true;
		}	
		
		int currentRemain = MAX_SIZE - tranOpsSignList.size();
		int limit = currentRemain > PER_SIZE ? PER_SIZE : currentRemain;
		List<TencentQuestion> results = null;
		Integer size =0;
		
		//取得初始数据
		if(limit >0){
			results = tencentQuestionDao.getSignMarkList(NORMAL, limit);
		}
		if( results !=null && results.size() > 0 ){
			size = results.size();
			updateStatus(results);
			tranOpsSignList.addAll(results);			
		}
		
		//取得超长时间处理数据(session过期等)
		if(tranOpsSignList.size() < MAX_SIZE &&  size < limit ){
			limit = limit -size;
			//取数据
			results = tencentQuestionDao.getSignMarkList(RECALL, limit);
			if( results != null && results.size() > 0 ){
				updateStatus(results);
				tranOpsSignList.addAll(results);			
			}			
		}

		logger.debug("[E N D] TranOpsSignList Pool SIZE: {}, 执行{}毫秒", tranOpsSignList.size(), System.currentTimeMillis() - startTime);
	}
	
	//更新数据状态为[1]
	private void updateStatus(List<TencentQuestion> results) {
		for(TencentQuestion baseExam : results) {
			tencentQuestionDao.updateProcessStatusById(baseExam.getId(), PROCESSING);
		}
		
	}
}
