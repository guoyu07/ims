package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class RecognitionPictureMedium {
	private Integer correctNum;

	private Date createTime;

	private Integer disunityNum;

	private Long fileId;

	private String fileName;

	private String filePath;

	private Long id;

	private String operator;

	private Date recognitionTime;

	private Integer requestNum;

	private Date requestTime;
	
	private Integer status;
	
	private Integer totalNum;
	
	private Integer unrecNum;
	
	private Date updateTime;

	public RecognitionPictureMedium() {
		super();
	}

	public RecognitionPictureMedium(Long fileId, String fileName, String filePath, String operator, Date recognitionTime, Integer status,
			Date createTime, Date updateTime, Date requestTime, Integer requestNum) {
		super();
		this.fileId = fileId;
		this.fileName = fileName;
		this.filePath = filePath;
		this.operator = operator;
		this.recognitionTime = recognitionTime;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.requestTime = requestTime;
		this.requestNum = requestNum;
	}

}