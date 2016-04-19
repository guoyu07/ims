package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class TranOpsApprove {
    private Long id;

    private Long tranId;

    private String approvor;

    private Date approveTime;

    private String status;

    private String reason;
    
    private Integer complete;

    public TranOpsApprove() {
		super();
	}

	public TranOpsApprove(Long tranId, Integer complete, String approvor, String status, String reason, Date date) {
		this.tranId = tranId;
		this.complete = complete;
		this.approvor = approvor;
		this.status = status;
		this.reason = reason;
		this.approveTime = date;
	}
}