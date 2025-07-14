package com.project.apt.fee.vo;

import lombok.Data;

@Data
public class FeeCompareVO {
    private int feeYear;
    private int feeMonth;
    private int userFee;
    private int aptAvgFee;
    private int userElecFee; // 추가
    private int userWaterFee; // 추가
}
