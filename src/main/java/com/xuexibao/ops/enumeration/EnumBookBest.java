package com.xuexibao.ops.enumeration;

public enum EnumBookBest {
	NOT_BEST		(0, "非精品"),
	BEST 		(1, "精品");

	private EnumBookBest(int id, String desc) {
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
