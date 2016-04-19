package com.xuexibao.ops.model;


import lombok.Data;

@Data
public class OrcBookRates {
    private Long id;

    private Long bookid;

    private Long orcRight;

    private Long orcUndealt;

    private Long orcUpload;

    private String orcRate;

    private Books books;
    
    private UserInfo userinfo;
    
    private TikuTeam tikuTeam;
    
    private OrcBooks orcBooks;
    public OrcBookRates() {
 		super();
 	}
    public OrcBookRates(Long bookid,Long orcRight,Long orcUndealt,Long orcUpload,String orcRate) {
    	this.bookid=bookid;
    	this.orcRight=orcRight;
    	this.orcUndealt=orcUndealt;
    	this.orcUpload=orcUpload;
    	this.orcRate=orcRate;  	
 	}
    public OrcBookRates(Long id,Long bookid,Long orcRight,Long orcUndealt,Long orcUpload,String orcRate) {
    	this.id=id;
    	this.bookid=bookid;
    	this.orcRight=orcRight;
    	this.orcUndealt=orcUndealt;
    	this.orcUpload=orcUpload;
    	this.orcRate=orcRate;  	
 	}
}