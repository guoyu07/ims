package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class PictureCheckRecord {
	
    private Long id;

    private Date startTime;

    private Date endTime;

    private Integer num;

    private String teamIds;

    private Integer teamCount;

    private Integer status;
    
    private String statusString;

    private Date createTime;

    private Date updateTime;

    private String operator;
    
    public PictureCheckRecord() {
    	super();
    }

	public PictureCheckRecord(Date startTime, Date endTime, Integer num,
			String teamIds, Integer teamCount, String operator, Date createTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.num = num;
		this.teamIds = teamIds;
		this.teamCount = teamCount;
		this.operator = operator;
		this.createTime = createTime;
	}
}