package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class Books {
    private Long id;

    private String name;

    private String subject;

    private String grade;

    private String isbn;

    private String sourceId;
    
    private String[] sourceIdArray;

    private String publishingHouse;

    private Integer status;

    private String operator;

    private Date createTime;
    private String  createTimeForShow;

    private Date updateTime;
    
    private Integer best;
    public Books() {
 		super();
 	}
    public Books(String name, String isbn, String subject, String grade, String publishingHouse, String sourceId, Integer status, Date createTime, Date updateTime, String operator) {
		this.name = name;
		this.isbn = isbn;
		this.subject = subject;
		this.grade = grade;
		this.publishingHouse = publishingHouse;
		this.sourceId = sourceId;
		this.status=status;
		this.createTime = createTime;
		this.updateTime= updateTime;
		this.operator= operator;		
	}
    public Books(Long id, String name, String isbn, String subject, String grade, String publishingHouse, String sourceId, Integer status, Date createTime, Date updateTime, String operator) {
    	this.id = id;
    	this.name = name;
		this.isbn = isbn;
		this.subject = subject;
		this.grade = grade;
		this.publishingHouse = publishingHouse;
    	this.sourceId = sourceId;
    	this.status=status;
    	this.createTime = createTime;
		this.updateTime= updateTime;
		this.operator= operator;		
	}
	public Books(Long id, Integer best) {
		super();
		this.id = id;
		this.best = best;
	}

}