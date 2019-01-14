package com.zhizhuotec.service.impl;

import java.util.HashMap;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.entity.User;
import com.zhizhuotec.entity.UserAuths;
import com.zhizhuotec.mapper.BackstageMapper;
import com.zhizhuotec.service.BackstageService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class BackstageServiceImpl implements BackstageService {

	private BackstageMapper backstageMapper;

	@Resource
	public void setBackstageMapper(BackstageMapper backstageMapper) {
		this.backstageMapper = backstageMapper;
	}

	@Override
	public boolean getMsgFromUId(String uId, int role) {
		Map<String, Object> map = backstageMapper.getMsgFromUId(uId);
		if (map != null && !map.isEmpty() && Integer.valueOf(map.get("role").toString()) >= role
				&& map.get("status").equals(0)) {
			return true;
		}
		return false;
	}

	@Override
	public String getMsgFromIdentifier(UserAuths userAuths) {
		Map<String, Object> map = backstageMapper.getMsgFromIdentifier(userAuths);
		if (map != null && !map.isEmpty()) {
			return map.get("id").toString();
		}
		return null;
	}

	// 管理员登录
	@Override
	public Map<String, Object> login(UserAuths userAuths) {
		Map<String, Object> map = backstageMapper.login(userAuths);
		Map<String, Object> map2 = new HashMap<String, Object>();
		if (map != null && Utils.getHash(Utils.getHash(userAuths.getCredential(), "MD5"), "MD5")
				.equals(map.get("credential"))) {
			if (map.get("role") != null && Integer.valueOf(map.get("role").toString()) >= 100) {
				if (map.get("verified") != null && map.get("verified").equals(1)) {
					if (map.get("status") != null && map.get("status").equals(0)) {
						userAuths.setId(map.get("id").toString());
						userAuths.setLoginTime(Utils.getTimestamp());
						if (userAuths.getLoginIp() == null) {
							userAuths.setLoginIp(map.get("loginIp").toString());
						}
						backstageMapper.loginIp(userAuths);
						map2.put("msg", "success");
						map2.put("id", map.get("id"));
						map2.put("role", map.get("role"));
						map2.put("avatar", map.get("avatar"));
						map2.put("nickName", map.get("nickName"));
					} else {
						map2.put("msg", "账号已被停用"); // error:账号已被停用
					}
				} else {
					map2.put("msg", "账号校验失败"); // error:账号校验失败
				}
			} else {
				map2.put("msg", "用户权限不足"); // error:用户权限不足
			}
		} else {
			map2.put("msg", "账号或密码错误"); // error:账号或密码错误
		}
		return map2;
	}

	// 获取所有用户信息
	@Override
	public List<User> info(String uId, String criteria) {
		if (getMsgFromUId(uId, 100)) {
			return backstageMapper.findByUserInfo(JSON.parseObject(criteria));
		}
		return null;
	}

	// 更改用户信息
	@Override
	public boolean settingInfo(String uId, String formMsg) {
		if (getMsgFromUId(uId, 10000)) {
			Map<String, Object> map = JSON.parseObject(formMsg);
			if (map != null && map.get("identifier") != null && map.get("psw") != null) {
				String psw = map.get("psw").toString();
				map.put("psw", Utils.getHash(Utils.getHash(psw, "MD5"), "MD5"));
				if (backstageMapper.settingInfo(map) > 0) {
					return true;
				}
			}
		}
		return false;
	}

	// 查询用户详细信息
	@Override
	public String infoDetails(String uId, String identifier) {
		if (getMsgFromUId(uId, 10000)) {
			Map<String, Object> map = backstageMapper.infoDetails(identifier);
			if (null != map) {
				return JSON.toJSONString(map);
			}
		}
		return null;
	}

	// 设置设备状态备注信息
	@Override
	public boolean setRemark(String uId, String remark) {
		if (getMsgFromUId(uId, 10000)) {
			Map<String, Object> map = JSON.parseObject(remark);
			String chooseBox = map.get("chooseBox").toString();
			map.put("remark", "chooseBox:" + chooseBox + ",msgText:" + map.get("msgText").toString());
			if (chooseBox.equals("")) {
				map.put("deviceStatus", 0);
			} else {
				map.put("deviceStatus", 1);
			}
			if (null != map.get("identifier") && backstageMapper.setRemark(map) > 0) {
				return true;
			}
		}
		return false;
	}

}
