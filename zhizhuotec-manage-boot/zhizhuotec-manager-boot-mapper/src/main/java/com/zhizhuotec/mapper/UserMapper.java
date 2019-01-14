package com.zhizhuotec.mapper;

import java.util.List;

import java.util.Map;
import com.zhizhuotec.entity.User;
import com.zhizhuotec.entity.UserAuths;

// 安卓接口
public interface UserMapper {

	public List<User> findById(String id);

	/**
	 * 账户类型(phone)加手机号查询用户信息
	 */
	public List<UserAuths> findByIdentifier(UserAuths userAuths);

	/**
	 * 用户登录
	 */
	public Map<String, Object> login(UserAuths userAuths);

	/**
	 * 账户类型(phone)加uId查询用户信息
	 */
	public Map<String, Object> getMsgFromUId(UserAuths userAuths);

	/**
	 * 账户类型(phone)加手机号查询用户信息
	 */
	public Map<String, Object> getMsgFromIdentifier(UserAuths userAuths);

	/**
	 * 更新用户登录ip
	 */
	public Integer loginIp(UserAuths userAuths);

	/**
	 * 用户注册
	 */
	public Integer register(Map<String, Object> map);

	/**
	 * 修改密码
	 */
	public Integer updateByCredential(UserAuths userAuths);

	/**
	 * 修改手机号
	 */
	public Integer updateByIdentifier(UserAuths userAuths);

	/**
	 * 修改用户信息
	 */
	public Integer updateSetting(Map<String, Object> map);

	/**
	 * 修改头像
	 */
	public Integer updateAvatar(User user);
	
}
