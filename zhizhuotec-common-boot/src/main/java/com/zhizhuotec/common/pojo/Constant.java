package com.zhizhuotec.common.pojo;

import com.zhizhuotec.common.utils.UpUtils;

public enum Constant {
	/**
	 * ip
	 */
	BASE_IP("http://120.78.30.114"),
	/**
	 * app base路径
	 */
	BASE_PATH(UpUtils.SEPARATOR + "usr" + UpUtils.SEPARATOR + "app"),
	/**
	 * 头像 folder
	 */
	HEADER_FOLDER("header"),
	/**
	 * 蓝牙升级固件 folder
	 */
	BLUETOOTH_FOLDER("bluetooth"),
	/**
	 * app 升级folder
	 */
	UPGRADE_FOLDER("upgrade"),
	/**
	 * ecg folder
	 */
	ECG_FOLDER("ecg"),
	/**
	 * 默认头像名字
	 */
	DEFAULT_HEADER_NAME("evangelion"),
	/**
	 * 默认头像后缀
	 */
	DEFAULT_HEADER_SUFFIX(".png"),
	/**
	 * 盐
	 */
	SALT("8eb125181ec1497aa142cd4c0489e0f0"),
	/**
	 * token过期时间(秒)
	 */
	EXPIRE(15 * 60 * 60 * 24),
	/**
	 * 验证码过期时间
	 */
	CODE_EXPIRE(10 * 60),
	/**
	 * 每个手机号每分钟最多发送验证码次数
	 */
	IDENTIFIER_COUNT1(3),
	/**
	 * 每个手机号每小时最多发送验证码次数
	 */
	IDENTIFIER_COUNT2(10),
	/**
	 * 每个手机号每天最多发送验证码次数
	 */
	IDENTIFIER_COUNT3(20),
	/**
	 * 登录类型
	 */
	IDENTITYTYPE_PHONE("phone"),
	/**
	 * 注册验证码
	 */
	CODE_TYPE_1(1),
	/**
	 * 修改密码验证码
	 */
	CODE_TYPE_2(2),
	/**
	 * 家庭消息过期时间
	 */
	FAMILY_EXPIRE(30 * 60 * 60 * 24),
	/**
	 * 家庭消息key
	 */
	FAMILY_TYPE_1("family_1");

	private final Object value;

	private Constant(Object val) {
		this.value = val;
	}

	public Object getValue() {
		return value;
	}
}
