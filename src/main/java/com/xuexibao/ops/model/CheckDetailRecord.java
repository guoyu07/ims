package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class CheckDetailRecord {
    private Long id;

    private Date startTime;

    private Date endTime;

    private Integer num;

    private Integer passNum;

    private Integer unpassNum;

    private Integer teamId;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Long parentId;
    
    private String ratioStr;
    
    public CheckDetailRecord() {
		super();
	}

	public CheckDetailRecord(Date startTime, Date endTime, Integer num,
			Integer teamId, Date createTime, Long parentId) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.num = num;
		this.teamId = teamId;
		this.createTime = createTime;
		this.parentId = parentId;
	}
    
}