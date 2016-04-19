package com.xuexibao.ops.enumeration;

public enum EnumQuestionCategory {
	
	TONG_BU		(1, "同步"),
	QI_ZHONG	(2, "期中期末"),
	ZHEN_TI  	(3, "真题模拟");

	private EnumQuestionCategory(int id, String description) {
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
