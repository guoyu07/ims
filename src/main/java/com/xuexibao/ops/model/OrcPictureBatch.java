package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data

public class OrcPictureBatch {

    private Long id;
    
    private String batchId;

    private String userKey;

    private Integer status;
    
    private String statusStr;

    private Date createTime;

    private Date updateTime;

    private Integer seachType;

    private String operatorName;
    
    private Date approveTime;

    private Integer operatorTeamId;
    
    private Long questionId;

    private String knowledge;
    
    private String[] knowledgeArray;

    private Integer realSubject;
    
    private String subject;
    
    private String orcPictureUrl;
    
    private String content;

    private String latex;

    private String answer;

    private String answerLatex;

    private String solution;
    private String target;
    private String targetStr;
    private String rawText;

    private String pictureId;
    private String originalFileName;
    private String recoAdditionalInfo;
    
    private Integer recolistCount;
    
    //统计状态件数
    private Long countByStatus;
    
    //统计日期
    private String create_ymd;
        
    public OrcPictureBatch() {
  		super();
  	}

	public OrcPictureBatch(Integer status, Long countByStatus) {
		super();
		this.status = status;
		this.countByStatus = countByStatus;
	}

}