package com.xuexibao.ops.enumeration;

public enum EnumOrcResult {
	UNFINISH	(0, "正在识别"),
	FINISH      (1, "识别完成");

	private EnumOrcResult(Integer id, String desc) {
		this.id = id;
		this.desc = desc;
	}

	private Integer id;
	private String desc;

	public Integer getId() {
		return id;
	}
	public String getDesc() {
		return desc;
	}

}
