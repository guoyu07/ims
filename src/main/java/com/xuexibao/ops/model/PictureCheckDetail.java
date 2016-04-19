package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class PictureCheckDetail {
	
    private Long id;

    private Long tranId;

    private Date approveTime;

    private Integer cstatus;

    private String checker;

    private Date checkTime;

    private Date updateTime;

    private Long parentId;

    private Long grandParentId;

    private OrcPicture orcPicture;
    
    private String statusForShow;
    
    public PictureCheckDetail() {
		super();
	}
	
    public PictureCheckDetail(OrcPicture orcPicture, Long parentId, Long grandParentId) {
    	this.tranId = orcPicture.getId();
    	this.approveTime = orcPicture.getApproveTime();
    	this.parentId = parentId;
    	this.grandParentId = grandParentId;
    }

}