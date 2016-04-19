package com.xuexibao.ops.model;

import java.util.Date;
import lombok.Data;
@Data
public class DedupGroupCandidates {
    private Integer groupId;

    private Long questionId;

    private String knowledge;
    
    private String content;
    
    private String answer;

    private String solution;
    
    private Integer simOrder;
    
    private Integer qualityOrder;
    
    private Date createTime;

    public DedupGroupCandidates(){
    	super();
    }
}
