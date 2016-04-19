package com.xuexibao.ops.model;

import lombok.Data;

@Data
public class DedupMarkMongo {
	
	private String _id;
	private String latex;
	private Integer is_base;
	private String clean_latex;
	private String content;
	private Double score;
	private String base_id;
	private String date;
	private String question_id;
	
}
