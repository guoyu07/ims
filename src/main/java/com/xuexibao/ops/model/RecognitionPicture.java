package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class RecognitionPicture {
    private Long id;

    private String fileName;

    private String filePath;

    private String operator1;

    private String operator2;
    
    private Date recognitionTime;

    private Integer status;

    private String unRecognizedReason;

    private Date createTime;

    private Date updateTime;
    
    private Date request_time;
    
    private Integer request_num;

    private RecognitionHistory recognitionHistory;

    public RecognitionPicture() {
		super();
	}
	public RecognitionPicture(Long id) {
		super();
		this.id = id;
	}

	public RecognitionPicture(String fileName, String filePath, Date createTime) {
		super();
		this.fileName = fileName;
		this.filePath = filePath;
		this.createTime = createTime;
	}

	public RecognitionPicture(Long id,String operater1, String operater2, Date recognitionTime, Integer status) {
		super();
		this.id = id;
		this.operator1 = operater1;
		this.operator2 = operater2;
		this.recognitionTime = recognitionTime;
		this.status = status;
	}
	
	
}