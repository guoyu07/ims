package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class DedupBaseExam {
    private Integer groupId;

    private Long questionId;

    private Integer status;

    private String userKey;
    
    private Integer groupCount;
    
    private Integer phase;
    
    private Integer round;
    
    private Integer tinyPhase;
    
    private Date createTime;

    private Date updateTime;
    
    public DedupBaseExam(){
    	super();
    }
}
