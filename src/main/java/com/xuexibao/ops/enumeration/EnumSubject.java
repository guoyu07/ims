package com.xuexibao.ops.enumeration;

public enum EnumSubject {
	MATH		(1, "数学", "math"),
	CHINESE		(2, "语文", "chinese"),
	ENGLISH		(3, "英语", "english"),
	POLITICS	(4, "政治", "politics"),
	HISTORY		(5, "历史", "history"),
	GEOGRAPHY	(6, "地理", "geography"),
	PHYSICS		(7, "物理", "physics"),
	CHEMISTRY	(8, "化学", "chemistry"),
	BIOLOGY		(9, "生物", "biology");

	private EnumSubject(int id, String chineseName, String englishName) {
		this.id = id;
		this.chineseName = chineseName;
		this.englishName = englishName;
	}

	private int id;
	private String chineseName;
	private String englishName;
	public int getId() {
		return id;
	}
	public String getChineseName() {
		return chineseName;
	}
	public String getEnglishName() {
		return englishName;
	}

}
