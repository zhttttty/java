package com.zhizhuotec.entity;

import java.io.Serializable;

/**
 * 家庭
 */
public class Family implements Serializable {

	private static final long serialVersionUID = -3759323244984830608L;

	private String id;
	// 用户id
	private String userId;
	// 家庭成员list
	private String familyList;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFamilyList() {
		return familyList;
	}

	public void setFamilyList(String familyList) {
		this.familyList = familyList;
	}

}