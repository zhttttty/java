package com.zhizhuotec.entity;

import java.io.Serializable;

public class stepNumber_total implements Serializable {

	private static final long serialVersionUID = 5359870886418108677L;
	
	private String id;
	// 总步数
	private Integer stepTotal;
	// yyyy-MM-dd
	private Long dates;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getStepTotal() {
		return stepTotal;
	}

	public void setStepTotal(Integer stepTotal) {
		this.stepTotal = stepTotal;
	}

	public Long getDates() {
		return dates;
	}

	public void setDates(Long dates) {
		this.dates = dates;
	}

}
