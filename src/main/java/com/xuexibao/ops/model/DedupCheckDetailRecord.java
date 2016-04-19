package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class DedupCheckDetailRecord {
	
	private Date startTime;
	private Date endTime;
	
    private Long id;

    private Integer num;

    private Integer passNum;

    private Integer unpassNum;

    private Integer teamId;

    private Integer status;//状态--0：待检查：1已检查

    private Date createTime;

    private Date updateTime;

    private Long parentId;
    
    private String ratioStr;//合格率
    
    private String userKey;//操作人
    
    private String name;//账号
    
    private String finshTime;//500题去重完成时间
    
    private DedupMark dedupMark;
    
    public DedupCheckDetailRecord() {
		super();
	}

	public DedupCheckDetailRecord(Integer num, Integer teamId, Date createTime, Long parentId) {
		super();
		this.num = num;
		this.teamId = teamId;
		this.createTime = createTime;
		this.parentId = parentId;
	}
    
}