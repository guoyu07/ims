package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class DedupCheckRecord {
	
	private Date startTime;
	private Date endTime;
	private Long id;

	private Integer num;

	private String blockIds;

	private Integer blockCount;

	private Integer status;

	private Date createTime;

	private Date updateTime;

	private String operator;

	private String statusString;

	public DedupCheckRecord() {
		super();
	}

	public DedupCheckRecord(Integer num, String blockIds,
			Integer blockCount, String operator, Date createTime) {
		super();
		this.num = num;
		this.blockIds = blockIds;
		this.blockCount = blockCount;
		this.operator = operator;
		this.createTime = createTime;
	}

}