package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class RecognitionHistory {
	private Long id;

	private Long fileId;

	private String operator;

	private Date recognitionTime;

	private String result;

	private Integer status;

	private RecognitionPicture recognitionPicture;

	public RecognitionHistory() {
		super();
	}

	public RecognitionHistory(Long fileId, String operator, Date recognitionTime, String result, Integer status) {
		super();
		this.fileId = fileId;
		this.operator = operator;
		this.recognitionTime = recognitionTime;
		this.result = result;
		this.status = status;
	}
}