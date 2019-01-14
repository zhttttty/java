package com.zhizhuotec.service;

import java.util.Map;

import com.zhizhuotec.entity.User;
import com.zhizhuotec.entity.UserAuths;

public interface UserService {

	/**
	 * 账户类型(phone)加uId查询用户信息
	 */
	public Map<String, Object> getMsgFromUId(UserAuths userAuths);

	/**
	 * 账户类型(phone)加手机号查询用户信息
	 */
	public Map<String, Object> getMsgFromIdentifier(UserAuths userAuths);

	/**
	 * 用户登录
	 */
	public Map<String, Object> login(UserAuths userAuths);

	/**
	 * 用户注册
	 */
	public Map<String, Object> register(UserAuths userAuths);

	/**
	 * 修改手机
	 */
	public int editPhone(UserAuths userAuths, String newPhone);

	/**
	 * 忘记密码
	 */
	public int forgetPass(UserAuths userAuths, String newPass);

	/**
	 * 修改密码
	 */
	public int editPass(UserAuths userAuths, String newPass);

	/**
	 * 修改用户信息
	 */
	public int updateSetting(String uId, String data);

	/**
	 * 获取头像名
	 */
	public String findByHeader(String uId);

	/**
	 * 更新头像名
	 */
	public boolean updateByHeader(User user);
}
