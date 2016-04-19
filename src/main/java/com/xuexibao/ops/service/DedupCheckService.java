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

import com.xuexibao.ops.dao.IDedupCheckDetailDao;
import com.xuexibao.ops.dao.IDedupCheckDetailRecordDao;
import com.xuexibao.ops.dao.IDedupCheckRecordDao;
import com.xuexibao.ops.dao.IDedupMarkDao;
import com.xuexibao.ops.dao.impl.DedupMarkDao;
import com.xuexibao.ops.model.DedupCheckDetail;
import com.xuexibao.ops.model.DedupCheckDetailRecord;
import com.xuexibao.ops.model.DedupCheckRecord;
import com.xuexibao.ops.model.DedupMark;

@Service
public class DedupCheckService {
	@Resource
	IDedupMarkDao dedupMarkDaos;
	@Resource
	DedupMarkDao dedupMarkDao;
	@Resource
	IDedupCheckRecordDao dedupCheckRecordDao;
	@Resource
	IDedupCheckDetailRecordDao dedupCheckDetailRecordDao;
	@Resource
	IDedupCheckDetailDao dedupCheckDetailDao;

	@Resource
    DedupMarkService dedupMarkService;
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public void generateCheckRecord(Integer num, List<DedupMark> dedupMarkList, String operator) {
		// 保证各表的冗余create_time字段值相等
		Date createTime = new Date();
		StringBuffer sb = new StringBuffer();
		int i = 0;
		for(DedupMark s : dedupMarkList) {
			if(i != 0)
				sb.append(',');
			sb.append(s.getBlock());
			i++;
		}
		// 在Dedup_check_record生成总记录
		DedupCheckRecord dedupcheckRecord = new DedupCheckRecord(num, sb.toString(), dedupMarkList.size(), operator, createTime);
		dedupCheckRecordDao.insertSelective(dedupcheckRecord);
	
		for(DedupMark teamId : dedupMarkList) {			
			generateCheckDetailRecord(num, teamId.getBlock(), operator, createTime, dedupcheckRecord.getId());
		}
	}

	private void generateCheckDetailRecord(Integer num, Integer blockId, String operator, Date createTime, Long parentId) {
		// 查询真实的n值，防止检查数大于试题数
		List<DedupMark> dedupList = dedupMarkDaos.searchIds(blockId);
		int real_n = dedupList.size();
		if(real_n < num)
			num = real_n;
		// 在dedup_check_detail_record生成每个包记录
		DedupCheckDetailRecord dedupcheckDetailRecord = new DedupCheckDetailRecord(num, blockId, createTime, parentId);
		dedupCheckDetailRecordDao.insertSelective(dedupcheckDetailRecord);
		//生成完成 更新 dedupMark中的blockid 的check状态为 已抽检
		dedupMarkDao.updateDedupMarkChecked(blockId);
		
		// 随机选取n个待抽检的试题
		dedupList = random(dedupList, num);
		generateCheckDetails(dedupList, dedupcheckDetailRecord.getId(), dedupcheckDetailRecord.getParentId());
	}
	
	private void generateCheckDetails(List<DedupMark> tranOpsList, Long parentId, Long grandParentId) {
		for(DedupMark tranOps : tranOpsList) {
			generateCheckDetail(tranOps, parentId, grandParentId);
		}
	}
	
	private void generateCheckDetail(DedupMark tranOps, Long parentId, Long grandParentId) {
		// 在dedup_check_detail生成抽检题目记录
		DedupCheckDetail checkDetail = new DedupCheckDetail(tranOps, parentId, grandParentId);
		dedupCheckDetailDao.insertSelective(checkDetail);
	}
	
	private List<DedupMark> random(List<DedupMark> tranOpsList, Integer n) {
		
		// 涉及到删除操作，LinkedList效率更高
		tranOpsList = new LinkedList<DedupMark>(tranOpsList);
		if(n > tranOpsList.size())
			n = tranOpsList.size();
		List<DedupMark> resultList = new ArrayList<DedupMark>(n);
		int index = 0;
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			index = random.nextInt(tranOpsList.size());
			resultList.add(tranOpsList.remove(index));
		}
		return resultList;
	}
}
