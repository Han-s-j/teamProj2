package com.project.apt.fee.vo;

import lombok.Data;

@Data
public class FeeVO {

   private String userId;
   private int monthTotalFee;
   private int userElecFee;
   private int userWaterFee;
   private int feeYear;
   private int feeMonth;
	
}
