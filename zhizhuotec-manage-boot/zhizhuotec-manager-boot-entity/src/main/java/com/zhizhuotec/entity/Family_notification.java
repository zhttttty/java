package com.zhizhuotec.entity;

import java.io.Serializable;

// 通知消息列表
public class Family_notification implements Serializable {

	private static final long serialVersionUID = 8570908156193924933L;
	
	// 验证消息id
	private String id;
	// 验证消息
	private String msg;
	// 验证状态0/已发送,1/同意,2/拒绝
	private Integer status;
	// 验证消息编号
	private Integer number;
	// 账号
	private String identifier;
	// 昵称
	private String nickName;
	// 头像
	private String avatar;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
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

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

}
