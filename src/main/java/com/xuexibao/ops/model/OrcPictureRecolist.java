package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data

public class OrcPictureRecolist {

    private Long id;
    
    private Long orcPictureBatchId;

    private Integer recoIndex;

    private Long questionId;

    private String rawText;
    
    private String content;

    private String latex;
    
    private String answer;

    private String answerLatex;
    
    private String solution;
    
    private String knowledge;
    
    private String[] knowledgeArray;
    
    private Integer realSubject;
    
    private Date createTime;

    private Date updateTime;
    
    public OrcPictureRecolist() {
  		super();
  	}
}
