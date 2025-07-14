package com.project.apt.user.vo;

import lombok.Data;

@Data
public class UserVO {
   
   private String userId;
   private String userPw;
   private String nickname;
   private String kaptCode;
   private String userBlock;
   private int userFloor;
   //아파트명 추가
   private String kaptName;
}
