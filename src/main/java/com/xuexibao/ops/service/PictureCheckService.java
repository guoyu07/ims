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

import com.xuexibao.ops.dao.IOrcPictureDao;
import com.xuexibao.ops.dao.impl.PictureCheckDetailDao;
import com.xuexibao.ops.dao.impl.PictureCheckDetailRecordDao;
import com.xuexibao.ops.dao.impl.PictureCheckRecordDao;
import com.xuexibao.ops.model.OrcPicture;
import com.xuexibao.ops.model.PictureCheckDetail;
import com.xuexibao.ops.model.PictureCheckDetailRecord;
import com.xuexibao.ops.model.PictureCheckRecord;

@Service
public class PictureCheckService {
	@Resource
	IOrcPictureDao orcPictureDao;
	@Resource
	PictureCheckRecordDao pictureCheckRecordDao;
	@Resource
	PictureCheckDetailRecordDao pictureCheckDetailRecordDao;
	@Resource
	PictureCheckDetailDao pictureCheckDetailDao;
	@Resource
	TikuTeamService tikuTeamService;
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public void generateCheckRecord(Date startDate, Date endDate, Integer n, String teamIds, String operator) {
		// 保证各表的冗余create_time字段值相等
		Date createTime = new Date();
		
		// 在check_record生成总记录
		PictureCheckRecord checkRecord = new PictureCheckRecord(startDate, endDate, n, teamIds, teamIds.split(",").length, operator, createTime);
		pictureCheckRecordDao.insertSelective(checkRecord);
		
		Integer[] teamIdArray = getTeamIds(teamIds);
		for(Integer teamId : teamIdArray) {
			generateCheckDetailRecord(startDate, endDate, n, teamId, operator, createTime, checkRecord.getId());
		}
	}

	private void generateCheckDetailRecord(Date startDate, Date endDate, Integer n, Integer teamId, String operator, Date createTime, Long parentId) {
		// 查询真实的n值，防止检查数大于试题数
		List<OrcPicture> tranOpsList = orcPictureDao.searchIds(startDate, endDate, teamId);
		int real_n = tranOpsList.size();
		if(real_n < n)
			n = real_n;
		// 在check_detail_record生成小组记录
		PictureCheckDetailRecord checkDetailRecord = new PictureCheckDetailRecord(startDate, endDate, n, teamId, createTime, parentId);
		pictureCheckDetailRecordDao.insertSelective(checkDetailRecord);
		
		// 随机选取n个待抽检的试题
		tranOpsList = random(tranOpsList, n);
		
		generateCheckDetails(tranOpsList, checkDetailRecord.getId(), checkDetailRecord.getParentId());
	}
	
	private void generateCheckDetails(List<OrcPicture> tranOpsList, Long parentId, Long grandParentId) {
		for(OrcPicture tranOps : tranOpsList) {
			generateCheckDetail(tranOps, parentId, grandParentId);
		}
	}
	
	private void generateCheckDetail(OrcPicture orcPicture, Long parentId, Long grandParentId) {
		// 在check_detail生成抽检题目记录
		PictureCheckDetail checkDetail = new PictureCheckDetail(orcPicture, parentId, grandParentId);
		pictureCheckDetailDao.insertSelective(checkDetail);
	}
	
	private List<OrcPicture> random(List<OrcPicture> tranOpsList, Integer n) {
		
		// 涉及到删除操作，LinkedList效率更高
		tranOpsList = new LinkedList<>(tranOpsList);
		if(n > tranOpsList.size())
			n = tranOpsList.size();
		List<OrcPicture> resultList = new ArrayList<>(n);
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
