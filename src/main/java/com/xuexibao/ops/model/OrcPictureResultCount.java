package com.xuexibao.ops.model;

import lombok.Data;
@Data

public class OrcPictureResultCount {

    	//统计日期
    	private String create_ymd;
	    
    	//状态值
    	private Integer status;
    	
    	//计数
    	private Long countByStatus;    	
    	
	    //总数
	    private Long total_count;
	    
	    //未判断数
	    private Long unjudged_count;    	
    	
	    //正确数
	    private Long success_count;
	    
	    //失败数
	    private Long failure_count;

	    //正确率
	    private String success_rate;	    
	    
	    public OrcPictureResultCount() {
	  		super();
	  	}
	    
		public OrcPictureResultCount(String create_ymd, Integer status, Long countByStatus) {
			super();
			this.create_ymd = create_ymd;
			this.status = status;
			this.countByStatus = countByStatus;
		}	    

}
