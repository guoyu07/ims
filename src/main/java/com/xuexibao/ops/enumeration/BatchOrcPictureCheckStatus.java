package com.xuexibao.ops.enumeration;

public enum BatchOrcPictureCheckStatus {

	SOLVE_RIGHT  			(1, "√ 识别正确"),
	ERROR_RECO_FAILURE		(2, "X 识别错误-识别失败"),
	ERROR_SEARCH			(3, "X 识别错误-搜索失败"),
	ERROR_RECO_NORESULT		(4, "X 识别错误-识别无结果"),
	ERROR_PICTUREBAD		(5, "X 识别错误-图片不好"),
	ERROR_OTHER				(6, "X 识别错误-其他"),
	ERROR_UPLOAD_FAILURE	(7, "X 上传失败"),
	URECO	        		(9, "待识别"),
	USOLVE	        		(0, "待处理");	
	
	private BatchOrcPictureCheckStatus(int id, String desc) {
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
