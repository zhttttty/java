package com.zhizhuotec.entity;

import java.io.Serializable;

/**
 * 睡眠
 */
public class Sleeping implements Serializable {

	private static final long serialVersionUID = 7012222488943538504L;

	private String id;
	// 用户id
	private String userId;

	public String getId() {
		return id;
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

}