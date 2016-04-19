package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;

@Data
public class UserSignUp {
	
    private Long id;

    private String name;

    private String idNumber;

    private String cityName;

    private String provinceName;

    private String mobile;

    private Date createTime;

    private Date updateTime;
    
    public UserSignUp() {
		super();
	}

	public UserSignUp(String name, String mobile, String idNumber,
			String cityName, String provinceName, Date createTime) {
		super();
		this.name = name;
		this.mobile = mobile;
		this.idNumber = idNumber;
		this.cityName = cityName;
		this.provinceName = provinceName;
		this.createTime = createTime;
	}
    
}