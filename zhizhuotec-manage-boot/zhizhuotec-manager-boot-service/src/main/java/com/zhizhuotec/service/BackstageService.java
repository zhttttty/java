package com.zhizhuotec.service;

import java.util.List;
import java.util.Map;

import com.zhizhuotec.entity.User;
import com.zhizhuotec.entity.UserAuths;

public interface BackstageService {
	
	public boolean getMsgFromUId(String uId, int role);
	
	public String getMsgFromIdentifier(UserAuths userAuths);
	
	public Map<String, Object> login(UserAuths userAuths);
	
	public List<User> info(String uId, String criteria);
	
	public boolean settingInfo(String uId, String formMsg);
	
	public String infoDetails(String uId, String identifier);
	
	public boolean setRemark(String uId, String remark);
	
}
