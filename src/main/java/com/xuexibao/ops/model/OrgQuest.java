package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class OrgQuest {
    private Integer orgId;

    private Long realquestId;

    private Date createTime;

    private Date updateTime;
    
    public OrgQuest(){
    	super();
    }
    public OrgQuest(Integer orgId,  Long realquestId){
       this.orgId = orgId;
       this.realquestId = realquestId;
       this.createTime = new Date();
       this.updateTime = new Date();
    }
    
}