package com.xuexibao.ops.enumeration;

// 0: 待标注， 1: 标注完成、待审核，2: 审核成功，3：审核失败
public enum EnumTeacherAuditStatus {
	
	PENDING				(0, "待标注"),
	COMPLETE				(1, "标注完成、待审核"),
	AUDIT_THROUGH		(2, "审核成功"),
	AUDIT_NOT_THROUGH	(3, "审核失败");

	private EnumTeacherAuditStatus(int id, String description) {
		this.id = id;
		this.description = description;
	}

	private int id;
	private String description;
	public int getId() {
		return id;
	}
	public String getDescription() {
		return description;
	}

}
