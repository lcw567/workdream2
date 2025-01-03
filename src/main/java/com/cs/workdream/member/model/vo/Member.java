package com.cs.workdream.member.model.vo;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Member {
	private int userNo;					// 회원 고유키
	private String userType;			// 가입 유형(개인: P / 기업: B)
	private String userId;				// 유저 아이디
	private String userPwd;				// 비밀번호
	private String email;				// 이메일
	private String phone;				// 전화번호
	private String enrollDate;			// 회원 가입일
    private String modifyDate;			// 마지막 수정일
	private String status;				// 계정 상태(가입 중: Y / 탈퇴: N)
	private int businessNo;				// 기업 고유키
	private int personNo;				// 개인 고유키
	
	// 기업 회원 전용
	private String registrationNumber;	// 사업자등록번호
	private String ownerName;			// 대표자명
	private String companyName;			// 상호명
	private String logoImg;				// 로고 이미지
	
	// 개인 회원 전용
	private String userName;			// 회원 이름
	private String birthday;			// 생년월일
	private String gender;				// 성별
}
