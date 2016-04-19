package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class DedupGroupSelected {
    private Long id;

    private Integer groupId;

    private Integer round;

    private Integer tinyPhase;
    
    private String userKey;
    
    private String questionIds;
    
    private Integer analyzeCount;
    
    private Date createTime;
    
    private Integer questionIdsLen;
    
    private String dateStr;
    
    private Integer validQstnLen;
    
    private String finishDateStr;

    public DedupGroupSelected(){
    	super();
    }
}
