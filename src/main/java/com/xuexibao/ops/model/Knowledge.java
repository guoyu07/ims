package com.xuexibao.ops.model;

import java.util.Date;

public class Knowledge {
    private Long id;

    private Byte learnPhase;

    private Byte subject;

    private String knowledgeCode;

    private String knowledgeSummary;

    private String knowledge;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Byte getLearnPhase() {
        return learnPhase;
    }

    public void setLearnPhase(Byte learnPhase) {
        this.learnPhase = learnPhase;
    }

    public Byte getSubject() {
        return subject;
    }

    public void setSubject(Byte subject) {
        this.subject = subject;
    }

    public String getKnowledgeCode() {
        return knowledgeCode;
    }

    public void setKnowledgeCode(String knowledgeCode) {
        this.knowledgeCode = knowledgeCode;
    }

    public String getKnowledgeSummary() {
        return knowledgeSummary;
    }

    public void setKnowledgeSummary(String knowledgeSummary) {
        this.knowledgeSummary = knowledgeSummary;
    }

    public String getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(String knowledge) {
        this.knowledge = knowledge;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}