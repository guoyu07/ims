package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class QuestionKnowledge {
    private Long id;

    private Long tranOpsId;

    private String knowledgeCode;

    private Date createTime;

    private Date updateTime;

    public Long getId() {
        return id;
    }

	public QuestionKnowledge(Long tranOpsId, String knowledgeCode,
			Date createTime) {
		super();
		this.tranOpsId = tranOpsId;
		this.knowledgeCode = knowledgeCode;
		this.createTime = createTime;
	}

	public QuestionKnowledge(Long tranOpsId, String knowledgeCode) {
		super();
		this.tranOpsId = tranOpsId;
		this.knowledgeCode = knowledgeCode;
	}

    
}