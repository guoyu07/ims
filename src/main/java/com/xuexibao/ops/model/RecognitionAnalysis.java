package com.xuexibao.ops.model;

import lombok.Data;

@Data
public class RecognitionAnalysis {
	
    private Long id;

    private String operator;

    private Integer successNum;

    private Integer failNum;

    private Integer total;

    public RecognitionAnalysis() {
		super();
	}

	public RecognitionAnalysis(String operator, Integer successNum, Integer failNum, Integer total) {
		super();
		this.operator = operator;
		this.successNum = successNum;
		this.failNum = failNum;
		this.total = total;
	}
}