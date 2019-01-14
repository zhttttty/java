package com.zhizhuotec.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 用户主表
 */
public class User implements Serializable {

	private static final long serialVersionUID = 4603053554164677827L;

	// 用户id
	private String id;
	// 头像地址
	private String avatar;
	// 用户权限
	private Integer role;
	// 账号状态 0 启用/1 禁用
	private Short status;
	// 用户验证信息list
	private List<UserAuths> userAuths;
	// 用户设置list
	private List<UserSetting> userSetting;

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Short getStatus() {
		return status;
	}

	public void setStatus(Short status) {
		this.status = status;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	public String getAvatar() {
		return this.avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public List<UserAuths> getUserAuths() {
		return userAuths;
	}

	public void setUserAuths(List<UserAuths> userAuths) {
		this.userAuths = userAuths;
	}

	public List<UserSetting> getUserSetting() {
		return userSetting;
	}

	public void setUserSetting(List<UserSetting> userSetting) {
		this.userSetting = userSetting;
	}

}