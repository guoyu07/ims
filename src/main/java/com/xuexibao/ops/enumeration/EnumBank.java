package com.xuexibao.ops.enumeration;

public enum EnumBank {
	ICBC	("中国工商银行", "中国工商银行"),
	CCB		("中国建设银行", "中国建设银行"),
	ABC		("中国农业银行", "中国农业银行"),
	CMBC	("招商银行", "招商银行");

	private String id;
	private String name;
	private EnumBank(String id, String name) {
		this.id = id;
		this.name = name;
	}
	public String getId() {
		return id;
	}
	public String getName() {
		return name;
	}

}
