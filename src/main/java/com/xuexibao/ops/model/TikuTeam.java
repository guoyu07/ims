package com.xuexibao.ops.model;

import lombok.Data;

public @Data class TikuTeam {
    private Integer id;

    private String name;

    private String captain;
    
    private long transContentNum;
    
    private long transCompleteNum;
    
    private long transCheckedNum;
    
    private long transUnCheckNum;
    
    private long usersNum;
    
    private String captainName;
     
    private UserInfo userInfo;
    
    public TikuTeam(Integer id, String name, String captain, String captainName) {
    	this.id = id;
		this.name = name;
		this.captain = captain;
		this.captainName = captainName;
	}
    

    public TikuTeam() {
    	super();
	}
}
