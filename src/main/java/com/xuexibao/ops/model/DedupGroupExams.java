package com.xuexibao.ops.model;

import java.util.Date;
import lombok.Data;
@Data
public class DedupGroupExams {
    private Integer groupId;

    private Long questionId;

    private Integer best;
    
    private Date createTime;

    public DedupGroupExams(){
    	super();
    }
}
