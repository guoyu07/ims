package com.xuexibao.ops.enumeration;

public enum EnumDiff {
	EASY		(1, "简单"),
	MEDIUM	(2, "中等"),
	HARD  	(3, "难");

	private EnumDiff(int id, String description) {
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
