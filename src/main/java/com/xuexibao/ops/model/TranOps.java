package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

import com.xuexibao.ops.dto.KnowledgeNode;
import com.xuexibao.ops.enumeration.TranOpsCompleteStatus;

@Data
public class TranOps {

	private Long id;

	private String type;

	private String learnphase;

	private String subject;

	private String diff;

	private String knowledge;

	private String source;

	private String url;

	private Byte status;

	private Integer realSubject;

	private String ops;

	private String auditStatus;

	private Date createTime;

	private Date updateTime;

	private String operatorName;

	private String contentOperatorName;

	private Integer operatorTeamId;

	private Integer contentOperatorTeamId;

	private Date approveTime;

	private String content;

	private String answer;

	private String solution;

	private String remark;

	private String latex;

	private String answerlatex;

	private String statusForShow;

	private String isRerecordForShow;

	private String[] knowledgeArray;
	
	private KnowledgeNode[] knowledgeNodeArray;

	private Integer complete;

	private String completeForShow;

	private TranOpsApprove lastTranOpsApprove;

	private UserInfo userInfo;

	private String teamNameForShow;

	private Long orcPictureId;
	private String pictureUrl;
	private Integer orcType;
	private Integer orcpushstatus;

	private Integer target;
	private Integer realType;
	private Integer realLearnPhase;

	// 题目标注
	private String realKnowledge;
	private Integer realDiff;
	private String questionCategory;
	private String[] questionCategoryArray;
	private String teacher;
	private Integer teacherTeam;
	private Date teacherTime;
	private String auditReason;
	private Date teacherAuditTime;
	private String approver;
	private Integer teacherStatus;
	private Integer processStatus;

	
	//选择题新增字段
	private String selectContent;
	private String selectOption;
	
	public TranOps() {
		super();
	}

	public TranOps(Integer complete, Integer subject, Integer target,
			Integer realType, Integer realLearnPhase, String content,
			String latex, String answer, String answerLatex, String solution,
			String operatorName, Integer operatorTeamId,
			Date date, Long orcPictureId, String pictureUrl, Integer orcType, String selectContent, String selectOption) {
		this.complete = complete;
		this.realSubject = subject;

		this.target = target;
		this.realType = realType;
		this.realLearnPhase = realLearnPhase;

		this.content = content;
		this.latex = latex;
		this.answer = answer;
		this.answerlatex = answerLatex;
		this.solution = solution;

		this.contentOperatorName = operatorName;
		this.contentOperatorTeamId = operatorTeamId;
		if (TranOpsCompleteStatus.COMPLETE.getId() == complete) {
			this.operatorName = operatorName;
			this.operatorTeamId = operatorTeamId;
		}
		this.createTime = date;
		this.updateTime = date;

		this.orcPictureId = orcPictureId;
		this.pictureUrl = pictureUrl;
		this.orcType = orcType;
		this.selectContent = selectContent;
		this.selectOption = selectOption;
	}

	public TranOps(Integer complete, Integer subject, Integer target,
			Integer realType, Integer realLearnPhase, String content,
			String latex, String answer, String answerLatex, String solution,
			String operatorName, Integer operatorTeamId,
			Date date, String selectContent, String selectOption) {
		this.complete = complete;
		this.realSubject = subject;

		this.target = target;
		this.realType = realType;
		this.realLearnPhase = realLearnPhase;

		this.content = content;
		this.latex = latex;
		this.answer = answer;
		this.answerlatex = answerLatex;
		this.solution = solution;

		this.contentOperatorName = operatorName;
		this.contentOperatorTeamId = operatorTeamId;
		if (TranOpsCompleteStatus.COMPLETE.getId() == complete) {
			this.operatorName = operatorName;
			this.operatorTeamId = operatorTeamId;
		}
		this.createTime = date;
		this.updateTime = date;
		this.selectContent = selectContent;
		this.selectOption = selectOption;
	}

	public TranOps(Long id, Integer complete, Integer subject, Integer target,
			Integer realType, Integer realLearnPhase, String content,
			String latex, String answer, String answerLatex, String solution,
			String operatorName, Integer operatorTeamId,
			Date date, String selectContent, String selectOption) {
		this.id = id;
		this.complete = complete;
		this.realSubject = subject;

		this.target = target;
		this.realType = realType;
		this.realLearnPhase = realLearnPhase;

		this.content = content;
		this.latex = latex;
		this.answer = answer;
		this.answerlatex = answerLatex;
		this.solution = solution;
		if (TranOpsCompleteStatus.NOT_COMPLETE.getId() == complete) {
			this.contentOperatorName = operatorName;
			this.contentOperatorTeamId = operatorTeamId;
		} else {
			this.operatorName = operatorName;
			this.operatorTeamId = operatorTeamId;
		}
		this.createTime = date;
		this.updateTime = date;
		this.selectContent = selectContent;
		this.selectOption = selectOption;
	}

	public TranOps(Long id, Integer realLearnPhase, Integer realSubject,
			Integer realType, String[] realKnowledge, String[] knowledgeText, Integer realDiff,
			Integer[] questionCategory, String teacher, Integer teacherTeam,
			Date teacherTime, Integer teacherStatus, Integer processStatus) {
		super();
		this.id = id;
		this.realLearnPhase = realLearnPhase;
		this.realSubject = realSubject;
		this.realType = realType;
		StringBuilder realKnwStr = new StringBuilder();
		for(int i = 0; i < realKnowledge.length; i++) {
			if(i != 0) realKnwStr.append(",");
			realKnwStr.append(realKnowledge[i]);
		}
		this.realKnowledge = realKnwStr.toString();
		StringBuilder knwStr = new StringBuilder();
		for(int i = 0; i < knowledgeText.length; i++) {
			if(i != 0) knwStr.append(",");
			knwStr.append(knowledgeText[i]);
		}
		this.knowledge = knwStr.toString();
		this.realDiff = realDiff;
		StringBuilder categoryStr = new StringBuilder();
		for(int i = 0; i < questionCategory.length; i++) {
			if(i != 0) categoryStr.append(",");
			categoryStr.append(questionCategory[i]);
		}
		this.questionCategory = categoryStr.toString();
		this.teacher = teacher;
		this.teacherTeam = teacherTeam;
		this.teacherTime = teacherTime;
		this.teacherStatus = teacherStatus;
		this.processStatus = processStatus;
	}

}
