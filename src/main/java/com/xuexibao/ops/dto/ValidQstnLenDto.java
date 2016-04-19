package com.xuexibao.ops.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ValidQstnLenDto {
	private String userKey;
	private Integer validCnt;
	
	public ValidQstnLenDto(String userKey, Integer validCnt) {
		super();
		this.userKey = userKey;
		this.validCnt = validCnt;
	}

	public ValidQstnLenDto() {
		super();
	}

}
