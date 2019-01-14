package com.zhizhuotec.mapper;

import java.util.List;
import java.util.Map;

import com.zhizhuotec.entity.Family;

public interface FamilyMapper extends BaseMapper<Family> {

	/**
	 * 获取家庭成员用户信息(验证消息列表)
	 */
	public List<Map<String, Object>> getNotificationMsg(List<Map<String, Object>> list);

	/**
	 * 获取家庭成员用户信息(家庭成员列表)
	 */
	public List<Map<String, Object>> getFamilyMsg(List<Map<String, Object>> list);

	/**
	 * 用uId获取家庭成员列表
	 */
	public List<Family> findFamilyById(String id);

	/**
	 * 插入家庭成员信息
	 */
	public Integer insertFamily(Family family);

	/**
	 * 更新家庭成员信息
	 */
	public Integer updateFamily(Family family);

}
