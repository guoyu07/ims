package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class OrganizationSources {
    private Long id;

    private String organizationName;

    private Integer status;

    private String operator;

    private Date createTime;

    private Date updateTime;

    public OrganizationSources() {
 		super();
 	}
    public OrganizationSources(String name, Integer status, Date createTime, Date updateTime, String operator) {
		this.organizationName = name;
		this.status = status;
		this.createTime = createTime;
		this.updateTime= updateTime;
		this.operator= operator;		
	}
}