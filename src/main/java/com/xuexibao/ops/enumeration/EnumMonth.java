package com.xuexibao.ops.enumeration;

public enum EnumMonth {
	JANUARY		(1, "1月"),
	FEBRUARY	(2, "2月"),
	MARCH		(3, "3月"),
	APRIL		(4, "4月"),
	MAY			(5, "5月"),
	JUNE		(6, "6月"),
	JULY		(7, "7月"),
	AUGUST		(8, "8月"),
	SEPTEMBER		(9, "9月"),
	OCTORBER		(10, "10月"),
	NOVEMBER		(11, "11月"),
	DECEMBER		(12, "12月");

	private EnumMonth(int id, String month) {
		this.id = id;
		this.month = month;
	}

	private int id;
	private String month;
	public int getId() {
		return id;
	}
	public String getMonth() {
		return month;
	}

}
