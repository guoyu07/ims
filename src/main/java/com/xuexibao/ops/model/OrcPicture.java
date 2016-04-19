package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class OrcPicture {
    private Long id;

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
    
    private Long bookId;
    private String bookName;
    private Books books;
    
    private TranOps tranOps;
    
    private Long tran_ops_id;//录题id
    private Long orc_picture_id;//图片id
    private Long book_id;//图书id
    private String books_source_id;//来源机构id
    
    private Integer pushstatus;
    
	public OrcPicture(Long orc_picture_id, Long tran_ops_id, Long book_id, String books_source_id) {
		super();
		this.orc_picture_id=orc_picture_id;
		this.tran_ops_id=tran_ops_id;
		this.book_id=book_id;
		this.books_source_id=books_source_id;	    
	}
	
    public OrcPicture() {
  		super();
  	}

}