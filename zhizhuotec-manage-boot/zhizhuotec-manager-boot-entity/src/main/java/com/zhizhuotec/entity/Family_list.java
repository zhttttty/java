package com.zhizhuotec.entity;

import java.io.Serializable;

// 家庭成员列表
public class Family_list implements Serializable {

	private static final long serialVersionUID = 7373434614777463997L;
	
	// 家庭成员id
	private String id;
	// 家庭成员头像
	private String avatar;
	// 家庭成员备注
	private String remark;
	// 家庭成员账号
	private String identifier;
	// 家庭成员昵称
	private String nickName;
	// 家庭成员年龄
	private Integer age;
	// 家庭成员身高
	private Integer stature;
	// 家庭成员体重
	private Integer weight;
	// 家庭成员生日
	private String birthday;
	// 家庭成员性别 0女/1男
	private byte sex;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
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

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public byte getSex() {
		return sex;
	}

	public void setSex(byte sex) {
		this.sex = sex;
	}

}
