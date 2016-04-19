package com.xuexibao.ops.enumeration;

public enum TranOpsAuditReason {
	SHI_BIE_JIE_GUO_ZHENG_QUE	(0, "识别结果正确，不该录"),
	WEN_ZI_GE_SHI				(1, "文字格式问题"),
	TU_PIAN_BU_XIAN_SHI			(2, "图片有问题"),
	XUE_KE_XUAN_ZE_CUO_WU		(3, "学科选择错误"),
	JIE_TI_SI_LU_ZHI_SHI_DIAN	(4, "请补充解题思路&知识点"),
	OTHER						(5, "其他（可填写）");

	private TranOpsAuditReason(int id, String desc) {
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
