package com.xuexibao.ops.dto;

import com.xuexibao.ops.model.Knowledge;

public class KnowledgeNode {
	private String text;
	private String id;
	public KnowledgeNode() {
		super();
	}
	public KnowledgeNode(String id, String text) {
		super();
		this.id = id;
		this.text = text;
	}
	public KnowledgeNode(Knowledge knowledge) {
		super();
		this.text = knowledge.getKnowledge();
		this.id = knowledge.getKnowledgeCode();
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}