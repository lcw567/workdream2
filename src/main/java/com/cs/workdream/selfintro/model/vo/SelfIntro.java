package com.cs.workdream.selfintro.model.vo;

import java.sql.Date;

import lombok.Data;

@Data
public class SelfIntro {

	private Integer selfintroNo;
	private Integer resumeNo;
	private Integer userNo;
	private String userId;
	private String introTitle;
	private String introContent;
	private Date createDate;
	private Date modifiedDate;
	private String deleted;
	private Integer personNo;

}
