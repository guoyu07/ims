package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class DedupCheckDetail {

	private Long id;

    private Long tranId;

    private Date approveTime;
    
    private String statusForShow;
    
    private Integer cstatus;

    private String creason;

    private String checker;

    private Date checkTime;

    private Date updateTime;

    private Long parentId;
    
    private Long grandParentId;
    
    private DedupMark dedupMark;
    
    private UserInfo userInfo;
    
 
    
	public DedupCheckDetail() {
		super();
	}
	
    public DedupCheckDetail(DedupMark dedupMark, Long parentId, Long grandParentId) {
//    	this.tranId = Long.valueOf(dedupMark.getBaseId());
    	this.tranId = Long.valueOf(dedupMark.getId());
    	this.approveTime = dedupMark.getUpdateTime();
    	this.parentId = parentId;
    	this.grandParentId = grandParentId;
    }

}