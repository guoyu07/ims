package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class Announcement {
    private Integer id;

    private Integer status;

    private String operator;

    private Date createTime;

    private Date updateTime;

    private String text;
    public Announcement() {
		super();
	}
    public Announcement(Integer status, String text, String operator,  Date date) {
    	this.status = status;
    	this.text = text;
    	this.operator = operator;  	
    	this.createTime = date;
		this.updateTime = date;
	}
    public Announcement(Integer id, Integer status, String text, String operator,  Date date) {
    	this.id=id;
    	this.status = status;
    	this.text = text;
    	this.operator = operator;  	
    	this.createTime = date;
		this.updateTime = date;
	}
}