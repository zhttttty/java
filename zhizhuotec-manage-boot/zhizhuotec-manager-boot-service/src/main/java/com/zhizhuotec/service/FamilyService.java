package com.zhizhuotec.service;

import java.util.List;
import java.util.Map;

import com.zhizhuotec.entity.Family;

public interface FamilyService extends BaseService<Family> {

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
	 * 添加家庭成员
	 */
	public boolean addFamily(String uId, String familysId);

	/**
	 * 修改家庭成员备注
	 */
	public int updateRemark(String uId, String familysId, String remark);

	/**
	 * 删除家庭成员
	 */
	public int deletFamily(String uId, String familysId);

}
