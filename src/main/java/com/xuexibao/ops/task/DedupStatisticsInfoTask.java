package com.xuexibao.ops.task;

import java.util.Calendar;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;

import com.xuexibao.ops.service.DedupStatisticsInfoService;

//@Component
public class DedupStatisticsInfoTask {
	
	@Resource
	DedupStatisticsInfoService dedupStatisticsInfoService;
	
	@Scheduled(cron = "0 0 3  * * ? ")
	private void getDailyDedupStatisticsInfo() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		dedupStatisticsInfoService.generateDedupStatistics(calendar.getTime());
	}
	
}
