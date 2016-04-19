package com.xuexibao.ops.enumeration;

public enum TranOpsCompleteStatus {
	NOT_COMPLETE	(0, "仅问题描述"),
	COMPLETE		(1, "完整");

	private TranOpsCompleteStatus(int id, String desc) {
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
