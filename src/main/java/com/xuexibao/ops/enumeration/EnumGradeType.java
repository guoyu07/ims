package com.xuexibao.ops.enumeration;

public enum EnumGradeType {
	PRIMARY	(1, "小学"),
	JUNIOR	(2, "初中"),
	SENIOR  (3, "高中");

	private EnumGradeType(int id, String chineseName) {
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
