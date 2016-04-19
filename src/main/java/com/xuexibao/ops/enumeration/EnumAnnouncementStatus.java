package com.xuexibao.ops.enumeration;

public enum EnumAnnouncementStatus {
	OPEN		("0", "启用"),
	CLOSE 	    ("1", "禁用");

	private EnumAnnouncementStatus(String id, String desc) {
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
