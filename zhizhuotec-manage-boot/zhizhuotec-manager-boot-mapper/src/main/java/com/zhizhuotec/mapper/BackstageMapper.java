package com.zhizhuotec.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.zhizhuotec.entity.User;
import com.zhizhuotec.entity.UserAuths;
import com.zhizhuotec.entity.UserSetting;

// 后台接口
public interface BackstageMapper {

	public Map<String, Object> login(UserAuths userAuths);

	public Integer loginIp(UserAuths userAuths);

	public Map<String, Object> getMsgFromUId(@Param("uId") String uId);

	public Map<String, Object> getMsgFromIdentifier(UserAuths userAuths);

	public List<User> findByUserInfo(Map<String, Object> map);

	public Map<String, Object> infoDetails(@Param("identifier") String identifier);

	public Integer setRemark(Map<String, Object> map);

	public List<UserAuths> getUId(String identifier);

	public List<UserSetting> getNickName(String id);

	public Integer settingInfo(Map<String, Object> map);
}
