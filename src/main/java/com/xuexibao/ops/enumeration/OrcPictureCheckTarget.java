package com.xuexibao.ops.enumeration;

public enum OrcPictureCheckTarget {
	XUEXIBAO	    (0, "学习宝"),
	XUEBAJUN  		(1, "学霸君"),
	XIAOYUANSOUTI	(2, "小猿搜题"),
	ZUOYEBANG		(3, "作业帮");
	
	private OrcPictureCheckTarget(int id, String desc) {
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
	
	static public OrcPictureCheckTarget getEnum(int key){
		OrcPictureCheckTarget result = null;
		switch (key) {
		case 0:
			result =  OrcPictureCheckTarget.XUEXIBAO;
			break;
			
		case 1:
			result =  OrcPictureCheckTarget.XUEBAJUN;
			break;
			
		case 2:
			result =  OrcPictureCheckTarget.XIAOYUANSOUTI;
			break;

		case 3:
			result =  OrcPictureCheckTarget.ZUOYEBANG;
			break;
			
		default:
			return null;
		}
		return result;
	}

}
