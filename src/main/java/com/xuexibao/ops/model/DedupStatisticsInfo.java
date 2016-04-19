package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class DedupStatisticsInfo {
    private Long id;

    private String dateStr;

    private String userKey;

    private Integer operatCnt;

    private Integer clickCnt;

    private Integer analyzeCnt;

    private Integer nodupClickCnt;

    private Integer validCnt;

    private Date updateTime;

    private Date creatTime;

    public DedupStatisticsInfo(String userKey, Integer operatCnt,
			Integer clickCnt, Integer analyzeCnt) {
		super();
		this.userKey = userKey;
		this.operatCnt = operatCnt;
		this.clickCnt = clickCnt;
		this.analyzeCnt = analyzeCnt;
		this.creatTime = new Date();
	}

	public DedupStatisticsInfo() {
		super();
	}

	public DedupStatisticsInfo(String dateStr, String userKey, Integer nodupClickCnt, Date creatTime) {
		super();
		this.dateStr = dateStr;
		this.userKey = userKey;
		this.nodupClickCnt = nodupClickCnt;
		this.creatTime = creatTime;
	}
	public DedupStatisticsInfo(Integer validCnt, String dateStr, String userKey, Date creatTime) {
		super();
		this.validCnt = validCnt;
		this.dateStr = dateStr;
		this.userKey = userKey;
		this.creatTime = creatTime;
	}

}