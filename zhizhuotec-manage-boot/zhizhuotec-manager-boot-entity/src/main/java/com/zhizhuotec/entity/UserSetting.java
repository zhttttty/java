package com.zhizhuotec.entity;

import java.io.Serializable;
import java.sql.Date;

/**
 * 用户个人设置信息表
 */
public class UserSetting implements Serializable {

	private static final long serialVersionUID = -5903157802967724105L;

	private String id;
	// 用户id
	private String userId;
	// 昵称
	private String nickName;
	// 年龄
	private Integer age;
	// 身高
	private Integer stature;
	// 体重
	private Integer weight;
	// 生日
	private Date birthday;
	// 性别 0女/1男
	private byte sex;
	// 穿戴左右 0右/1左
	private byte hand;
	// 目标步数
	private Integer userAimStep;
	// 设备状态 (后台)
	private byte deviceStatus;
	// 设备状态备注信息 (后台)
	private String remark;

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

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Integer getStature() {
		return stature;
	}

	public void setStature(Integer stature) {
		this.stature = stature;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

	public byte getHand() {
		return hand;
	}

	public void setHand(byte hand) {
		this.hand = hand;
	}

	public Integer getUserAimStep() {
		return userAimStep;
	}

	public void setUserAimStep(Integer userAimStep) {
		this.userAimStep = userAimStep;
	}

	public byte getDeviceStatus() {
		return deviceStatus;
	}

	public void setDeviceStatus(byte deviceStatus) {
		this.deviceStatus = deviceStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}