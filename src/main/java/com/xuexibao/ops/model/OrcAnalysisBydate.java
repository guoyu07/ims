package com.xuexibao.ops.model;

import lombok.Data;
@Data
public class OrcAnalysisBydate{
	

    private String operator;
    
    private Integer cnt_not;//未处理数量

    private Integer cnt_right;//识别正确数量

    private Integer cnt_finish;//已录入数量

   
	public OrcAnalysisBydate() {
		super();
	}

    public OrcAnalysisBydate(String operator, Integer cnt_not, Integer cnt_right,
			Integer cnt_finish) {
		super();
		this.operator = operator;
		this.cnt_not = cnt_not;
		this.cnt_right = cnt_right;
		this.cnt_finish = cnt_finish;
	}
    

}