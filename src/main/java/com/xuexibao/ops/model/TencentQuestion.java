package com.xuexibao.ops.model;

import java.util.Date;

import com.xuexibao.ops.dto.KnowledgeNode;

import lombok.Data;

public @Data class TencentQuestion {
    private Long id;
    private Long tranid;

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

    private Date createTime;

    private Date updateTime;

    private Long orcPictureId;

    private Integer realType;

    private Integer realLearnPhase;

    private String realKnowledge;

    private Integer realDiff;

    private String questionCategory;

    private String teacher;

    private Integer teacherTeam;

    private Date teacherTime;

    private String auditReason;

    private Date teacherAuditTime;

    private String approver;

    private Integer teacherStatus;

    private Integer processStatus;
    
    private String content;

    private String answer;

    private String solution;

    private String remark;

    private String latex;

    private String answerlatex;
    
    private String[] knowledgeArray;
	private KnowledgeNode[] knowledgeNodeArray;
	private String[] questionCategoryArray;
	
	//选择题新增字段
	private String selectContent;
	private String selectOption;
	
	public TencentQuestion(Long id, Integer realLearnPhase, Integer realSubject,
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


	public TencentQuestion() {
		super();
	}
	public TencentQuestion(OrcPicture orcPicture) {
		super();
		this.content = orcPicture.getContent();
		this.latex = orcPicture.getLatex();
		this.answer = orcPicture.getAnswer();
		this.answerlatex = orcPicture.getAnswerLatex();
		this.solution = orcPicture.getSolution();
		this.knowledge = orcPicture.getKnowledge();
		this.createTime = new Date();
	}
	public TencentQuestion(TranOps tranOps) {
		super();
		this.tranid = tranOps.getId();
		this.content = tranOps.getContent();
		this.latex = tranOps.getLatex();
		this.answer = tranOps.getAnswer();
		this.answerlatex = tranOps.getAnswerlatex();
		this.solution = tranOps.getSolution();
		this.knowledge = tranOps.getKnowledge();
		this.selectContent = tranOps.getSelectContent();
		this.selectOption = tranOps.getSelectOption();
		this.createTime = new Date();
	}
}