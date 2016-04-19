package com.xuexibao.ops.enumeration;

public enum TranOpsAuditStatus {
	PENDING_AUDIT			("0", "待审核"),
	IN_PROCESSING_AUDIT		("1", "审核中"),
	AUDIT_THROUGH			("2", "审核成功"),
	AUDIT_NOT_THROUGH		("3", "审核未通过"),
	HALF_THROUGH			("4", "题干通过，但答案不通过"),
	BEST_AUDIT_THROUGH		("5", "审核成功(精品识别正确)");

	private TranOpsAuditStatus(String id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	private String id;
	private String desc;
	public String getId() {
		return id;
	}
	public String getDesc() {
		return desc;
	}

}
