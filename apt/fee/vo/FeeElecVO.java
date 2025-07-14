package com.project.apt.fee.vo;

import lombok.Data;

@Data
public class FeeElecVO {
// 전기요금 VO
	private int privateElecFee;
	private int publicElecFee;
	
	public int getTotalElecFee() {
        return privateElecFee + publicElecFee;
    }
}
