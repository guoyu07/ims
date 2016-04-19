package com.xuexibao.ops.enumeration;

public enum EnumRecognitionResult {
	USER_MARK		(3, "用户标记"),
	REC_RESULT_DISUNITY 	(4, "识别结果不统一");

	private EnumRecognitionResult(Integer id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	private Integer id;
	private String desc;

	public Integer getId() {
		return id;
	}
	public String getDesc() {
		return desc;
	}

}
