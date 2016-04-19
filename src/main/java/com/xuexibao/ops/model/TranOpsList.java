package com.xuexibao.ops.model;

import java.util.Date;

import com.xuexibao.ops.enumeration.TranOpsCompleteStatus;

import lombok.Data;

@Data
public class TranOpsList {

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
    
    private Integer complete;
    
    private String completeForShow;
    

    
    private String teamNameForShow;
    
    
    private Long orcPictureId;
    private String pictureUrl;
    private Integer orcType;
    private Integer orcpushstatus;
    
    private Integer target;
    private Integer realType;
    private Integer realLearnPhase;

    
    public TranOpsList() {
		super();
	}
    public TranOpsList(Integer complete, Integer subject, Integer type, String content, String latex,
			String answer, String answerLatex, String solution,
			String knowledge, String operatorName, Integer operatorTeamId, Date date , Long orcPictureId,
			String pictureUrl, Integer orcType , Integer target , Integer realType , Integer realLearnPhase) {
    	this.complete = complete;
		this.realSubject = subject;
		this.content = content;
		this.latex = latex;
		this.answer = answer;
		this.answerlatex = answerLatex;
		this.solution = solution;
		this.knowledge = knowledge;
		
		this.contentOperatorName = operatorName;
		this.contentOperatorTeamId = operatorTeamId;
		if (TranOpsCompleteStatus.COMPLETE.getId() == complete) {
			this.operatorName = operatorName;
			this.operatorTeamId = operatorTeamId;
		}
		this.createTime = date;
		this.updateTime = date;
		
		this.orcPictureId=orcPictureId;
		this.pictureUrl=pictureUrl;
		this.orcType=orcType;
		this.target=target;
		this.realType=realType;
		this.realLearnPhase=realLearnPhase;
		
		
	}
    public TranOpsList(Integer complete, Integer subject, Integer target , Integer realType , Integer realLearnPhase, String content, String latex,
			String answer, String answerLatex, String solution,
			String knowledge, String operatorName, Integer operatorTeamId, Date date , Long orcPictureId,
			String pictureUrl, Integer orcType) {
    	this.complete = complete;
		this.realSubject = subject;

		this.target=target;
		this.realType=realType;
		this.realLearnPhase=realLearnPhase;

		this.content = content;
		this.latex = latex;
		this.answer = answer;
		this.answerlatex = answerLatex;
		this.solution = solution;
		this.knowledge = knowledge;
		
		this.contentOperatorName = operatorName;
		this.contentOperatorTeamId = operatorTeamId;
		if (TranOpsCompleteStatus.COMPLETE.getId() == complete) {
			this.operatorName = operatorName;
			this.operatorTeamId = operatorTeamId;
		}
		this.createTime = date;
		this.updateTime = date;
		
		this.orcPictureId=orcPictureId;
		this.pictureUrl=pictureUrl;
		this.orcType=orcType;
		
		
	}

    public TranOpsList(Integer complete, Integer subject, Integer target , Integer realType , Integer realLearnPhase, String content, String latex,

			String answer, String answerLatex, String solution,
			String knowledge, String operatorName, Integer operatorTeamId, Date date) {
    	this.complete = complete;
		this.realSubject = subject;

		this.target=target;
		this.realType=realType;
		this.realLearnPhase=realLearnPhase;

		this.content = content;
		this.latex = latex;
		this.answer = answer;
		this.answerlatex = answerLatex;
		this.solution = solution;
		this.knowledge = knowledge;
		
		this.contentOperatorName = operatorName;
		this.contentOperatorTeamId = operatorTeamId;
		if (TranOpsCompleteStatus.COMPLETE.getId() == complete) {
			this.operatorName = operatorName;
			this.operatorTeamId = operatorTeamId;
		}
		this.createTime = date;
		this.updateTime = date;
	}

    public TranOpsList(Long id, Integer complete, Integer subject, Integer target , Integer realType , Integer realLearnPhase, String content, String latex,

    		String answer, String answerLatex, String solution,
    		String knowledge, String operatorName, Integer operatorTeamId, Date date) {
    	this.id = id;
    	this.complete = complete;
    	this.realSubject = subject;

		this.target=target;
		this.realType=realType;
		this.realLearnPhase=realLearnPhase;

    	this.content = content;
    	this.latex = latex;
    	this.answer = answer;
    	this.answerlatex = answerLatex;
    	this.solution = solution;
    	this.knowledge = knowledge;
    	if (TranOpsCompleteStatus.NOT_COMPLETE.getId() == complete) {
			this.contentOperatorName = operatorName;
			this.contentOperatorTeamId = operatorTeamId;
		} else {
			this.operatorName = operatorName;
			this.operatorTeamId = operatorTeamId;
		}
    	this.createTime = date;
    	this.updateTime = date;
    }

    public TranOpsList(Integer subject, String content, String latex,
    		String answer, String answerLatex, String solution,
    		String knowledge, String operatorName, Integer operatorTeamId, Date date,Integer target) {
    	this.target = target;
    	this.realSubject = subject;
    	this.content = content;
    	this.latex = latex;
    	this.answer = answer;
    	this.answerlatex = answerLatex;
    	this.solution = solution;
    	this.knowledge = knowledge;
		this.contentOperatorName = operatorName;
		this.contentOperatorTeamId = operatorTeamId;
		this.operatorName = operatorName;
		this.operatorTeamId = operatorTeamId;
    	this.createTime = date;
    	this.updateTime = date;
    }	
    
}