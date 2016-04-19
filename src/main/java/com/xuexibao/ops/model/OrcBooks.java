package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class OrcBooks {
    private Long id;

    private Long bookid;

    private String operatorName;

    private Date operatorStartime;

    private Date operatorEndtime;

    private Integer status;
    
    private String statusStr;

    private String operator;

    private Date createTime;

    private Date updateTime;
    
    private Books books;
    
    private UserInfo userinfo;
    
    private TikuTeam tikuTeam;
    
    private String teamName;
    public OrcBooks() {
 		super();
 	}
    public OrcBooks(Long bookid, String operatorName,Date operatorStartime,
    		Date operatorEndtime,Integer status,String operator
    		,Date createTime,Date updateTime) {
 		this.bookid=bookid;
 		this.operatorName=operatorName;
 		this.operatorStartime=operatorStartime;
 		this.operatorEndtime=operatorEndtime;
 		this.status=status;
 		this.operator=operator;
 		this.createTime=createTime;
 		this.updateTime=updateTime;
 	}
    public OrcBooks(Long id,Long bookid, String operatorName,Date operatorStartime,
    		Date operatorEndtime,Integer status,String operator
    		,Date createTime,Date updateTime) {
    	this.id=id;
 		this.bookid=bookid;
 		this.operatorName=operatorName;
 		this.operatorStartime=operatorStartime;
 		this.operatorEndtime=operatorEndtime;
 		this.status=status;
 		this.operator=operator;
 		this.createTime=createTime;
 		this.updateTime=updateTime;
 	}

}