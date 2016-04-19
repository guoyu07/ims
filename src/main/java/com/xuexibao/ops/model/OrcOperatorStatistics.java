package com.xuexibao.ops.model;

import java.util.Date;

import lombok.Data;
@Data
public class OrcOperatorStatistics {
    private Long id;

    private String operator;

    private Date orcTime;

    private Long orcRight;

    private Long orcErrorUndealt;

    private Long orcErrorDealt;

    private Long orcUndealt;

    private Long orcUpload;

}