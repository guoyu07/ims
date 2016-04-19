package com.xuexibao.ops.enumeration;

public enum DedupMarkStatus {

	UNPROCESSED(new Integer(0).byteValue(), "未处理"),
	PROCESSED(new Integer(1).byteValue(), "已处理"),
	ABOLISHED(new Integer(2).byteValue(), "已废除");

	private DedupMarkStatus(Byte id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	private Byte id;
	private String desc;

	public Byte getId() {
		return id;
	}

	public String getDesc() {
		return desc;
	}
}
