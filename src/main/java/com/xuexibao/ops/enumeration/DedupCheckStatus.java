package com.xuexibao.ops.enumeration;

public enum DedupCheckStatus {
	UCHECK	    (0, "待检查"),
	ELIGIBLE 	(1, "正确"),
	UNELIGIBLE	(2, "错误");
	
	private DedupCheckStatus(int id, String desc) {
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
