package com.xuexibao.ops.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class NodupClickCntDto {
	private String userKey;
	private Integer nodupClick;
	
	public NodupClickCntDto(String userKey, Integer nodupClick) {
		super();
		this.userKey = userKey;
		this.nodupClick = nodupClick;
	}

	public NodupClickCntDto() {
		super();
	}

}
