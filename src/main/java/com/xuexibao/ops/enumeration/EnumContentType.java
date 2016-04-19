package com.xuexibao.ops.enumeration;

public enum EnumContentType {

	/**
	 * 音频
	 */
	AUDIO(1, "音频");

	private EnumContentType(Integer type, String desc) {
		this.type = type;
		this.desc = desc;
	}

	private Integer type;
	private String desc;

	public Integer getType() {
		return type;
	}

	public String getDesc() {
		return desc;
	}
}
