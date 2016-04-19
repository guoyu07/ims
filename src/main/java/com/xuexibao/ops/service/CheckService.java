package com.xuexibao.ops.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.dao.ICheckDetailDao;
import com.xuexibao.ops.dao.ICheckDetailRecordDao;
import com.xuexibao.ops.dao.ICheckRecordDao;
import com.xuexibao.ops.dao.ITranOpsDao;
import com.xuexibao.ops.model.CheckDetail;
import com.xuexibao.ops.model.CheckDetailRecord;
import com.xuexibao.ops.model.CheckRecord;
import com.xuexibao.ops.model.TranOps;

@Service
public class CheckService {
	@Resource
	ITranOpsDao tranOpsDao;
	@Resource
	ICheckRecordDao checkRecordDao;
	@Resource
	ICheckDetailRecordDao checkDetailRecordDao;
	@Resource
	ICheckDetailDao checkDetailDao;
	@Resource
	TikuTeamService tikuTeamService;
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public void generateCheckRecord(Date startDate, Date endDate, Integer n, String teamIds, String operator) {
		// 保证各表的冗余create_time字段值相等
		Date createTime = new Date();
		
		// 在check_record生成总记录
		CheckRecord checkRecord = new CheckRecord(startDate, endDate, n, teamIds, teamIds.split(",").length, operator, createTime);
		checkRecordDao.insertSelective(checkRecord);
		
		Integer[] teamIdArray = getTeamIds(teamIds);
		for(Integer teamId : teamIdArray) {
			generateCheckDetailRecord(startDate, endDate, n, teamId, operator, createTime, checkRecord.getId());
		}
	}

	private void generateCheckDetailRecord(Date startDate, Date endDate, Integer n, Integer teamId, String operator, Date createTime, Long parentId) {
		// 查询真实的n值，防止检查数大于试题数
		List<TranOps> tranOpsList = tranOpsDao.searchIds(startDate, endDate, teamId);
		int real_n = tranOpsList.size();
		if(real_n < n)
			n = real_n;
		// 在check_detail_record生成小组记录
		CheckDetailRecord checkDetailRecord = new CheckDetailRecord(startDate, endDate, n, teamId, createTime, parentId);
		checkDetailRecordDao.insertSelective(checkDetailRecord);
		
		// 随机选取n个待抽检的试题
		tranOpsList = random(tranOpsList, n);
		
		generateCheckDetails(tranOpsList, checkDetailRecord.getId(), checkDetailRecord.getParentId());
	}
	
	private void generateCheckDetails(List<TranOps> tranOpsList, Long parentId, Long grandParentId) {
		for(TranOps tranOps : tranOpsList) {
			generateCheckDetail(tranOps, parentId, grandParentId);
		}
	}
	
	private void generateCheckDetail(TranOps tranOps, Long parentId, Long grandParentId) {
		// 在check_detail生成抽检题目记录
		CheckDetail checkDetail = new CheckDetail(tranOps, parentId, grandParentId);
		checkDetailDao.insertSelective(checkDetail);
	}
	
	private List<TranOps> random(List<TranOps> tranOpsList, Integer n) {
		
		// 涉及到删除操作，LinkedList效率更高
		tranOpsList = new LinkedList<TranOps>(tranOpsList);
		if(n > tranOpsList.size())
			n = tranOpsList.size();
		List<TranOps> resultList = new ArrayList<TranOps>(n);
		int index = 0;
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			index = random.nextInt(tranOpsList.size());
			resultList.add(tranOpsList.remove(index));
		}
		return resultList;
	}
	
	private Integer[] getTeamIds(String teamIds) {
		String[] teamIdStrArray = teamIds.split(",");
		Integer[] teamIdArray = new Integer[teamIdStrArray.length];
		for(int i = 0; i < teamIdStrArray.length; i++) {
			teamIdArray[i] = Integer.valueOf(teamIdStrArray[i]);
		}
		return teamIdArray;
	}
}
