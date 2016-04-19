package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class RecognitionAnalysisBydate{
	
    private Long id;

    private String operator;
    
    private Integer recognitionCorrectCount;

    private Integer recognitionCount;

    private Integer recognitionDisunityCount;

    private Date recognitionTime;

	private Integer recognitionUnrecognitionCount;

	public RecognitionAnalysisBydate() {
		super();
	}

    public RecognitionAnalysisBydate(String operator, Integer recognitionCount, Integer recognitionCorrectCount,
			Integer recognitionUnrecognitionCount, Integer recognitionDisunityCount, Date recognitionTime) {
		super();
		this.operator = operator;
		this.recognitionCount = recognitionCount;
		this.recognitionCorrectCount = recognitionCorrectCount;
		this.recognitionUnrecognitionCount = recognitionUnrecognitionCount;
		this.recognitionDisunityCount = recognitionDisunityCount;
		this.recognitionTime = recognitionTime;
	}
    

}