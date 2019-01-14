package com.zhizhuotec.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 心电
 */
public class Electrocardiogram implements Serializable {

	private static final long serialVersionUID = -5197710379377367552L;

	private String id;
	// 用户id
	private String userId;
	// 磁盘文件名
	private String fileName;
	// yyyy-MM-dd
	private Date dates;

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

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Date getDates() {
		return dates;
	}

	public void setDates(Date dates) {
		this.dates = dates;
	}

}