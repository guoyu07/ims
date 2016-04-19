package com.xuexibao.ops.model;

import lombok.Data;
@Data
public class DedupCheckList{
	
    private Long id;

    private Integer num;

    private Integer passNum;

    private Integer unpassNum;

    private Integer teamId;

    private Integer status;//状态--0：待检查：1已检查
    
    private String statusStr;//状态--0：待检查：1已检查
    
    private String ratioStr;//合格率
    
    private String userKey;//操作人
    
    private String name;//账号
    
    private String finshTime;//500题去重完成时间

   
	public DedupCheckList() {
		super();
	}


	public DedupCheckList(Long id, Integer num, Integer passNum, Integer unpassNum, Integer teamId, Integer status, String userKey, String name , String finshTime) {
		super();
		this.id = id;
		this.num = num;
		this.passNum = passNum;
		this.unpassNum = unpassNum;
		this.teamId = teamId;
		this.status = status;
		this.userKey = userKey;
		this.name = name;
		this.finshTime = finshTime;
	}

}