package com.zhizhuotec.entity;

import java.io.Serializable;

/**
 * 计步
 */
public class StepNumber implements Serializable {

	private static final long serialVersionUID = 2720367575716699082L;

	private String id;
	// 用户id
	private String userId;
	// 值数组(24长度)
	private String results;
	// 当天时间戳
	private Long dates;

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

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

	public Long getDates() {
		return dates;
	}

	public void setDates(Long dates) {
		this.dates = dates;
	}

}