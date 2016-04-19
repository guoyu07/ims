package com.xuexibao.ops.model;

import lombok.Data;

@Data
public class OrcOtherList {
    private Long orcId;

    private String orcXueba;

    private String orcXiaoyuan;
    public OrcOtherList() {
  		super();
  	}
    
    public OrcOtherList(Long orcId,String orcXueba,String orcXiaoyuan) {
  		this.orcId=orcId;
  		this.orcXueba=orcXueba;
  		this.orcXiaoyuan=orcXiaoyuan;
  	}
}