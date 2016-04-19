package com.xuexibao.ops.dto;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeTree {
	private String text;
	
	private List<KnowledgeNode> nodes = new ArrayList<>();
	
	public KnowledgeTree() {
		super();
	}
	public KnowledgeTree(String text) {
		super();
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<KnowledgeNode> getNodes() {
		return nodes;
	}
	public void setNodes(List<KnowledgeNode> nodes) {
		this.nodes = nodes;
	}
}
