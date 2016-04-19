package com.xuexibao.ops.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.xuexibao.ops.dao.IDedupBaseExamDao;
import com.xuexibao.ops.dao.IDedupGroupCandidatesDao;
import com.xuexibao.ops.dao.IDedupGroupExams;
import com.xuexibao.ops.dao.IDedupGroupSelectedDao;
import com.xuexibao.ops.model.DedupBaseExam;
import com.xuexibao.ops.model.DedupGroupCandidates;
import com.xuexibao.ops.model.DedupGroupExams;
import com.xuexibao.ops.model.DedupGroupSelected;

@Service
public class DeDuplicateService {
	
	@Resource
	IDedupBaseExamDao DedupBaseExamDao;
	@Resource
	IDedupGroupCandidatesDao DedupGroupCandidatesDao;
	@Resource
	IDedupGroupExams DedupGroupExam;
	@Resource
	IDedupGroupSelectedDao DedupGroupSelected;	
	
	private static Logger logger = LoggerFactory.getLogger("dupQuestion_check_log");

	public List<DedupGroupCandidates> getCandidatesById(Integer groupId, Long questionId) {
		List<DedupGroupCandidates> cadidates_total = new ArrayList<>();
		
		List<DedupGroupCandidates> cadidates = DedupGroupCandidatesDao.searchListByGroupId(groupId);
		
		for(DedupGroupCandidates gCandidate : cadidates){
			if(gCandidate.getQuestionId().equals(questionId)){
				cadidates_total.add(gCandidate);
			}else if( DedupGroupExam.searchListByQuestionId(gCandidate.getQuestionId()) == null ){
				cadidates_total.add(gCandidate);
			}
		}
		
		return cadidates_total;
	}
	
	public List<DedupGroupCandidates> getSelectedById(Integer groupId) {
		
		List<DedupGroupCandidates> cadidates_total = new ArrayList<>();
		
		List<DedupGroupCandidates> cadidates = DedupGroupCandidatesDao.searchListByGroupId(groupId);
		
		for(DedupGroupCandidates gCandidate : cadidates){
			if( DedupGroupExam.searchListByQuestionId(gCandidate.getQuestionId()) != null ){
				cadidates_total.add(gCandidate);
			}
		}
		
		return cadidates_total;
	}
	
	public void setDedupBaseExam(Integer groupId, String user_key, Integer Status, Integer phase, Integer round,Integer tinyPhase) {
		
		DedupBaseExamDao.updateByGroupId(groupId, user_key, Status, phase, round, tinyPhase);
		
	}
	
	public void setDedupGroupExams(DedupGroupExams entity) {
		
		DedupGroupExam.insert(entity);
		
	}	
	
	public DedupBaseExam selectBaseByGroupId(Integer groupId) {
		
		DedupBaseExam base = DedupBaseExamDao.selectByKey(groupId);
		
		return base;
	}	
	
	public void setDedupGroupSelected(DedupGroupSelected entity) {
		
		DedupGroupSelected.insert(entity);
		
	}
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public void updateGroup(String userKey, Integer groupId, Integer analyzeCount, String[] dupQuestionId) {
		Integer phase=null;
		Integer round=null;
		Integer tiny_phase=null;
		Integer status=0;
		
		//
		DedupBaseExam baseExam = DedupBaseExamDao.selectByKey(groupId);
		tiny_phase = baseExam.getTinyPhase();
		round = baseExam.getRound();
		phase=baseExam.getPhase();
		
		if(tiny_phase == 0 || tiny_phase == 2){
			tiny_phase = 1;
			round +=1;			
		}else if(tiny_phase == 1){
			tiny_phase = 2;
		}
		
		//insert dedup_group_selected
		DedupGroupSelected groupSelected =new DedupGroupSelected();
		groupSelected.setGroupId(groupId);
		groupSelected.setRound(round);
		groupSelected.setTinyPhase(tiny_phase);
		groupSelected.setUserKey(userKey);
		StringBuilder sb= new StringBuilder();
		String questions="";
		for(String question :dupQuestionId){
			sb.append(question).append(";");
		}
		questions = sb.toString();
		if(questions.length() > 0){
			questions = questions.substring(0, questions.length()-1);
		}
		groupSelected.setQuestionIds(questions);
		groupSelected.setAnalyzeCount(analyzeCount);
		groupSelected.setQuestionIdsLen(dupQuestionId.length);
		SimpleDateFormat  dateFormat = new SimpleDateFormat("yyyyMMdd");
		String operatYMD = dateFormat.format(new Date());
		groupSelected.setDateStr(operatYMD);
		DedupGroupSelected.insert(groupSelected);			
			
		if(tiny_phase == 1){
			DedupBaseExamDao.updateByGroupId(groupId, userKey, status, phase, round, tiny_phase);
		}else if(tiny_phase == 2){
			List<DedupGroupSelected> GroupSelected = DedupGroupSelected.searchByNecessary(null, groupId, round, 1, null, null);
			if(GroupSelected.size()==0){
				logger.error("No found phase 1 data by groupid=" + groupId +" round=" + round );
			}else{
				String phase1_questionids = GroupSelected.get(0).getQuestionIds();
				String[] phase1_questionidsArr = phase1_questionids.split(";");
				if(phase1_questionidsArr != null && phase1_questionidsArr.length == 1 && StringUtils.isEmpty(phase1_questionidsArr[0]))
					phase1_questionidsArr = new String[0];
				Long[] merger_ids =  merger( phase1_questionidsArr, dupQuestionId);
	
				//update dedup_group_selected
				Integer validQstnLen=0;
				if(merger_ids != null && merger_ids.length>0){
					validQstnLen = merger_ids.length;
				}else if(phase1_questionids.equals("") && dupQuestionId.length==0){
					validQstnLen=1;
				}
				DedupGroupSelected.updateByRound(groupId, round, validQstnLen, operatYMD);
				
				//if duplicate finish current group
				if(isquestionidDup(merger_ids)){
					phase=1;
					status=0;
					DedupBaseExamDao.updateByGroupId(groupId, userKey, status, phase, round, tiny_phase);
					logger.info("##Duplicate questionId in different group: groupId=" + groupId + ";questionIds={" + questionIdStr(baseExam.getQuestionId(),merger_ids) +"}");
				}else{
					//insert dedup_group_exams--other ones
					for(Long id: merger_ids){
						synchronized (id.toString().intern()) {
							if(isquestionidDup(id)) {
								phase=1;
								status=0;
								DedupBaseExamDao.updateByGroupId(groupId, userKey, status, phase, round, tiny_phase);
								logger.info("##Duplicate questionId in different group: groupId=" + groupId + ";questionIds={" + questionIdStr(baseExam.getQuestionId(),merger_ids) +"}");
								break;
							} else {
								DedupGroupExams entity=new DedupGroupExams();
								entity.setGroupId(groupId);
								entity.setQuestionId(id);
								entity.setBest(0);
								entity.setCreateTime(new Date());
								DedupGroupExam.insert(entity);					
							}
						}
					}
					//update deduo_base_exam
					Integer groupCount = DedupGroupExam.searchListByGroupId(groupId);
					if( groupCount == baseExam.getGroupCount() || round >= 6 ||
					   (( dupQuestionId == null || dupQuestionId.length == 0) && ( phase1_questionidsArr == null || phase1_questionidsArr.length == 0)) ){
						phase=1;
						status=0;
					}
					DedupBaseExamDao.updateByGroupId(groupId, userKey, status, phase, round, tiny_phase);					
				}
			}
		}
	}
	

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = RuntimeException.class)
	public void updateBest(String userKey, Integer groupId, String bestQuestionId) {
		Integer status=3;
		DedupBaseExamDao.updateByGroupId(groupId, userKey, status, null, null, null);
		DedupGroupExam.updateByQuestionId(groupId, Long.parseLong(bestQuestionId));
	}
	
	private Long[] merger(String[] questionids1,String[] questionids2){
		List<Long> result= new ArrayList<Long>();
		
		for(String q1 : questionids1){
			for(String q2 : questionids2){
				if(q1.endsWith(q2)){
					result.add(Long.parseLong(q1));
					break;
				}
			}
		}
		Long[] resultArray = new Long[result.size()];
		return result.toArray(resultArray);
	}
	
	private boolean isquestionidDup(Long[] questionId){
		boolean result=false;
		
		for(Long id : questionId){
			if(DedupGroupExam.searchListByQuestionId(id)!=null){
				result =true;
				break;
			}
		}
		return result;
	}
	
	private boolean isquestionidDup(Long questionId){
		Long[] questionIds = new Long[]{questionId};
		
		boolean result =isquestionidDup(questionIds);
		
		return result;
	}
	
	private String questionIdStr(Long base, Long[] selected){
		String result="";
		result=base.toString() + ";";
		for(Long select : selected){
			result += select+";";
		}
		return result;
	}
}	