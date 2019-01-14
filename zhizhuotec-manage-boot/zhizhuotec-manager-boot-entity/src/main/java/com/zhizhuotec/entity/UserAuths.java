package com.zhizhuotec.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 用户验证信息
 */
public class UserAuths implements Serializable {

	private static final long serialVersionUID = -1806448310954745314L;

	private String id;
	// 用户id
	private String userId;
	// 账号类型
	private String identityType;
	// 账号
	private String identifier;
	// 密码
	private String credential;
	// 注册步骤验证(暂未使用)
	private Short verified;
	// 注册时间
	private Timestamp registerTime;
	// 注册ip
	private String registerIp;
	// 登录时间
	private Timestamp loginTime;
	// 登录ip
	private String loginIp;

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

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getCredential() {
		return credential;
	}

	public void setCredential(String credential) {
		this.credential = credential;
	}

	public Short getVerified() {
		return verified;
	}

	public void setVerified(Short verified) {
		this.verified = verified;
	}

	public Timestamp getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Timestamp registerTime) {
		this.registerTime = registerTime;
	}

	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

}