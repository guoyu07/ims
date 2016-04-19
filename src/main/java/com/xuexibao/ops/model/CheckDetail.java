package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class CheckDetail {

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
    
    private TranOps tranOps;
  
    private TranOpsApprove lastTranOpsApprove;
    
    
	public CheckDetail() {
		super();
	}
	
    public CheckDetail(TranOps tranOps, Long parentId, Long grandParentId) {
    	this.tranId = tranOps.getId();
    	this.approveTime = tranOps.getApproveTime();
    	this.parentId = parentId;
    	this.grandParentId = grandParentId;
    }

}