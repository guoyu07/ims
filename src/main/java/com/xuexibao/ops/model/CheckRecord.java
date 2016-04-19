package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class CheckRecord {
    private Long id;

    private Date startTime;

    private Date endTime;

    private Integer num;

    private String teamIds;

    private Integer teamCount;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private String operator;
    
    private String statusString;

    public CheckRecord() {
    	super();
    }

	public CheckRecord(Date startTime, Date endTime, Integer num,
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