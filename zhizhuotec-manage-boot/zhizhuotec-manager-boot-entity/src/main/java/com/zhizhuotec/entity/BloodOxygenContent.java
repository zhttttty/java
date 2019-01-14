package com.zhizhuotec.entity;

import java.io.Serializable;

/**
 * 血氧
 */
public class BloodOxygenContent implements Serializable {

	private static final long serialVersionUID = 7460001248129294603L;

	private String id;
	// 用户id
	private String userId;
	// 时间戳数组
	private String times;
	// 值数组
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

	public String getTimes() {
		return times;
	}

	public void setTimes(String times) {
		this.times = times;
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