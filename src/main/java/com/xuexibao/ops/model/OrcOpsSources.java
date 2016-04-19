package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class OrcOpsSources {
    private Long id;

    private Long orcPictureId;

    private Long bookid;

    private String sourceId;

    private Long tranOpsId;

    private Long questionid;

    private Integer status;

    private String operator;

    private Date createTime;

    private Date updateTime;



}