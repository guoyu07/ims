package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class Question {
    private Long id;

    private String subject;

    private String knowledge;

    private String learnPhase;

    private Byte realSubject;

    private Long realId;

    private Integer errorNumber;

    private Integer recordNumber;

    private Date createTime;

    private Byte emergencyStatus;

    private Integer emergencyCount;
    private String latex;

    private String content;

    private String answer;

    private String solution;

    private Byte allotCount;

    private Byte audioUploadStatus;

    private Date updateTime;

    private String source;
    public Question(){
    	super();
    }
    public Question(String subject, String knowledge,
    		String learnPhase, Byte realSubject,
    		Long realId, Integer errorNumber,
    		Byte emergencyStatus, Integer emergencyCount,Date createTime,
    		String latex, String content,
    		String answer, String solution,
    		Byte allotCount, Byte audioUploadStatus,
    		Date updateTime, String source){
    	this.subject = subject;
    	this.knowledge = knowledge;
    	this.learnPhase = learnPhase;
    	this.realSubject = realSubject;
    	this.realId = realId;
    	this.errorNumber = errorNumber;
    	this.emergencyStatus = emergencyStatus;
    	this.createTime=createTime;
    	this.emergencyCount = emergencyCount;
    	this.latex = latex;
    	this.content = content;
    	this.answer = answer;
    	this.solution = solution;
    	this.allotCount = allotCount;
    	this.audioUploadStatus = audioUploadStatus;
    	this.updateTime = updateTime;
    	this.source = source;
    }
}