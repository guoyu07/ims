package com.xuexibao.ops.enumeration;

public enum EnumSubjectType {
	RADIO		(1, "单选"),
	CHECK		(2, "多选"),
	FILLINBLANKS(3, "填空"),
	EXPLANATION	(4, "解答");

	private EnumSubjectType(int id, String chineseName) {
		this.id = id;
		this.chineseName = chineseName;
	}

	private int id;
	private String chineseName;
	public int getId() {
		return id;
	}
	public String getChineseName() {
		return chineseName;
	}

}
