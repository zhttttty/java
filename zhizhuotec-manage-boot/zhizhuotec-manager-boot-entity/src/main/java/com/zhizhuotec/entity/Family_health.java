package com.zhizhuotec.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Family_health implements Serializable {

	private static final long serialVersionUID = 5547655939341723032L;
	
	// 血氧
	private List<BloodOxygenContent> bloodOxygenContents;
	// 血压
	private List<BloodPressure> bloodPressures;
	// 心电
	private Map<Long, Object> electrocardiograms;
	// 心率
	private List<HeartRate> heartRates;
	// 计步
	private List<StepNumber> stepNumbers;

	public List<BloodOxygenContent> getBloodOxygenContents() {
		return bloodOxygenContents;
	}

	public void setBloodOxygenContents(List<BloodOxygenContent> bloodOxygenContents) {
		this.bloodOxygenContents = bloodOxygenContents;
	}

	public List<BloodPressure> getBloodPressures() {
		return bloodPressures;
	}

	public void setBloodPressures(List<BloodPressure> bloodPressures) {
		this.bloodPressures = bloodPressures;
	}

	public Map<Long, Object> getElectrocardiograms() {
		return electrocardiograms;
	}

	public void setElectrocardiograms(Map<Long, Object> electrocardiograms) {
		this.electrocardiograms = electrocardiograms;
	}

	public List<HeartRate> getHeartRates() {
		return heartRates;
	}

	public void setHeartRates(List<HeartRate> heartRates) {
		this.heartRates = heartRates;
	}

	public List<StepNumber> getStepNumbers() {
		return stepNumbers;
	}

	public void setStepNumbers(List<StepNumber> stepNumbers) {
		this.stepNumbers = stepNumbers;
	}

}
