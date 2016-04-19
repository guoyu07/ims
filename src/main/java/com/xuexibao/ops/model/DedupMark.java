package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

import com.xuexibao.ops.enumeration.DedupMarkStatus;

@Data
public class DedupMark {
	
    private Long id;

    private String baseId;

    private Integer block;

    private String operator;

    private Byte status;

    private Byte finished;

    private Byte checked;

    private String result;

    private Date updateTime;

    private Date createTime;
    
    public DedupMark(DedupMarkMongo mongo, int block) {
    	this.baseId = mongo.getBase_id();
    	this.block = block;
    	this.status = DedupMarkStatus.UNPROCESSED.getId();
    	this.createTime = new Date();
    }
    
    public DedupMark() {
    }
    
    public DedupMark(String baseId, int block) {
    	this.baseId = baseId;
    	this.block = block;
    	this.status = DedupMarkStatus.UNPROCESSED.getId();
    	this.createTime = new Date();
    }    
}