package com.xuexibao.ops.enumeration;

public enum OrcPictureCheckStatus {
	USOLVE	        (0, "待处理"),
	SOLVE_RIGHT  	(1, "√ 识别正确"),
	ERROR_UNRECORD	(2, "X 识别错误-未录入"),
	ERROR_RECORD	(3, "已人工处理"),
	USOLVE_ERROR	(4, "待处理-识别无结果"),
	BEST_SOLVE_RIGHT(5, "未录入-精品题目识别正确"),
	BEST_RECORD     (6, "已人工处理-精品题目识别正确");
	
	private OrcPictureCheckStatus(int id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	private int id;
	private String desc;
	public int getId() {
		return id;
	}
	public String getDesc() {
		return desc;
	}

}
