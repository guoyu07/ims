package com.xuexibao.ops.task;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.xuexibao.ops.dao.IDedupBaseExamDao;
import com.xuexibao.ops.model.DedupBaseExam;

//@Component
public class DeleteDuplicateTask {
	
	private static Logger logger = LoggerFactory.getLogger("dupQuestion_check_log");
	
	private static final int MAX_SIZE = 3000;
	private static final int PER_SIZE = 300;
	public static LinkedBlockingDeque<DedupBaseExam> duplicateGroupList = new LinkedBlockingDeque<>(MAX_SIZE);
	public static LinkedBlockingDeque<DedupBaseExam> duplicateGroupListPhase2 = new LinkedBlockingDeque<>(MAX_SIZE);
	
	@Resource
	IDedupBaseExamDao DedupBaseExamDao;
	
	// 每30秒执行一次
	@Scheduled(cron = "0/30 * * * * ? ")
	private void obtainRemainGroupList() {
		long startTime = System.currentTimeMillis();
		logger.info("[BEGIN] duplicateGroup Pool SIZE: {}", duplicateGroupList.size());
		
		int currentRemain = MAX_SIZE - duplicateGroupList.size();
		int limit = currentRemain > PER_SIZE ? PER_SIZE : currentRemain;
		List<DedupBaseExam> results =null;
		Integer size =0;
		
		//取得初始数据
		if(limit >0){
			results = DedupBaseExamDao.searchList(limit, 0, 0);
		}
		if( results !=null && results.size() > 0 ){
			size = results.size();
			updateStatus(results);
			duplicateGroupList.addAll(results);			
		}
		
		//取得超长时间处理数据
		if(duplicateGroupList.size() < MAX_SIZE &&  size < limit ){
			limit = limit -size;
			//取数据
			results = DedupBaseExamDao.searchList(limit,2, 0);
			if( results != null && results.size() > 0 ){
				updateStatus(results);
				duplicateGroupList.addAll(results);			
			}			
		}

		//当没有要处理的数据时，取得过长时间未更新状态数据（误操作，重启，宕机等引起的数据）
		if(duplicateGroupList.size() == 0){
			//初始化问题数据
			DedupBaseExamDao.updateByProblemData(0);
			
			//取数据
			results = DedupBaseExamDao.searchList(PER_SIZE, 1, 0);
			if( results != null && results.size() > 0 ){
				updateStatus(results);
				duplicateGroupList.addAll(results);			
			}			
		}		
		
		logger.info("[E N D] duplicateGroup Pool SIZE: {}, 执行{}毫秒", duplicateGroupList.size(), System.currentTimeMillis() - startTime);
	}
	
	// 每5分钟执行一次
	@Scheduled(cron = "0/30 * * * * ? ")
	private void obtainRemainGroupListPhase2Data() {
		long startTime = System.currentTimeMillis();
		logger.info("[BEGIN] duplicateGroup Pool2 SIZE: {}", duplicateGroupListPhase2.size());
		
		int currentRemain = MAX_SIZE - duplicateGroupListPhase2.size();
		int limit = currentRemain > PER_SIZE ? PER_SIZE : currentRemain;
		List<DedupBaseExam> results =null;
		Integer size =0;
		
		//取得初始数据
		if(limit >0){
			results = DedupBaseExamDao.searchList(limit, 0, 1);
		}
		if( results !=null && results.size() > 0 ){
			size = results.size();
			updateStatus(results);
			duplicateGroupListPhase2.addAll(results);			
		}
		
		//取得超长时间处理数据
		if(duplicateGroupListPhase2.size() < MAX_SIZE &&  size < limit ){
			limit = limit -size;
			//取数据
			results = DedupBaseExamDao.searchList(limit,2, 1);
			if( results != null && results.size() > 0 ){
				updateStatus(results);
				duplicateGroupListPhase2.addAll(results);			
			}			
		}

		//当没有要处理的数据时，取得过长时间未更新状态数据（误操作，重启，宕机等引起的数据）
		if(duplicateGroupListPhase2.size() == 0){
			//初始化问题数据
			DedupBaseExamDao.updateByProblemData(1);
			
			//取数据
			results = DedupBaseExamDao.searchList(PER_SIZE, 1, 1);
			if( results != null && results.size() > 0 ){
				updateStatus(results);
				duplicateGroupListPhase2.addAll(results);			
			}			
		}		
		
		logger.info("[E N D] duplicateGroup Pool2 SIZE: {}, 执行{}毫秒", duplicateGroupListPhase2.size(), System.currentTimeMillis() - startTime);
	}	
	
	//更新数据状态为[1]
	private void updateStatus(List<DedupBaseExam> results) {
		for(DedupBaseExam baseExam : results) {
			DedupBaseExamDao.updateByGroupId(baseExam.getGroupId(), null, 1, null, null, null);
		}
		
	}
}