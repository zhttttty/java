package com.zhizhuotec.service.impl;

import java.io.File;

import java.sql.Date;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.zhizhuotec.common.pojo.Constant;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.entity.User;
import com.zhizhuotec.entity.UserAuths;
import com.zhizhuotec.mapper.UserMapper;
import com.zhizhuotec.service.UserService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class UserServiceImpl implements UserService {

	private UserMapper userMapper;

	@Resource
	public void setUserMapper(UserMapper userMapper) {
		this.userMapper = userMapper;
	}

	@Override
	public Map<String, Object> getMsgFromUId(UserAuths userAuths) {
		Map<String, Object> map = userMapper.getMsgFromUId(userAuths);
		if (map != null && !map.isEmpty()) {
			// 验证账号是否被停用
			if (map.get("status") != null && map.get("status").equals(0)) {
				map.put("res", 1);
			} else {
				// 账号已被停用
				map.put("res", 3);
			}
		} else {
			map = new HashMap<String, Object>();
			// 用户不存在
			map.put("res", 2);
		}
		return map;
	}

	@Override
	public Map<String, Object> getMsgFromIdentifier(UserAuths userAuths) {
		Map<String, Object> map = userMapper.getMsgFromIdentifier(userAuths);
		if (map != null && !map.isEmpty()) {
			// 验证账号是否被停用
			if (map.get("status") != null && map.get("status").equals(0)) {
				map.put("res", 1);
			} else {
				// error:账号已被停用
				map.put("res", 3);
			}
		} else {
			map = new HashMap<String, Object>();
			// error:账号不存在
			map.put("res", 2);
		}
		return map;
	}

	@Override
	public Map<String, Object> login(UserAuths userAuths) {
		Map<String, Object> map = userMapper.login(userAuths);
		Map<String, Object> map2 = new HashMap<String, Object>();
		// 验证密码是否正确
		if (map != null && Utils.getHash(Utils.getHash(userAuths.getCredential(), "MD5"), "MD5")
				.equals(map.get("credential"))) {
			// 验证账号是否被停用
			if (map.get("status") != null && map.get("status").equals(0)) {
				userAuths.setId((String) map.get("id"));
				userAuths.setLoginTime(Utils.getTimestamp());
				if (userAuths.getLoginIp() == null) {
					userAuths.setLoginIp((String) map.get("loginIp"));
				}
				// 更新登录ip
				userMapper.loginIp(userAuths);
				// 登陆成功
				map2.put("res", 1);
				map2.put("id", map.get("id"));
				map2.put("avatar", Constant.BASE_IP.getValue() + File.separator + "user" + File.separator
						+ "getHeader?id=" + map.get("avatar"));
				map2.put("identifier", map.get("identifier"));
				map2.put("nickName", map.get("nickName"));
				map2.put("age", map.get("age"));
				map2.put("stature", map.get("stature"));
				map2.put("weight", map.get("weight"));
				map2.put("birthday", Utils.getYMD(map.get("birthday")));
				map2.put("sex", map.get("sex"));
				map2.put("userAimStep", map.get("userAimStep"));
			} else {
				// 账号已被停用
				map2.put("res", 3);
			}
		} else {
			// 账号或密码错误
			map2.put("res", 2);
		}
		return map2;
	}

	@Override
	public Map<String, Object> register(UserAuths userAuths) {
		Map<String, Object> map2 = new HashMap<String, Object>();
		List<UserAuths> list = userMapper.findByIdentifier(userAuths);
		// 验证账号是否存在
		if (list.size() == 0) {
			String id = Utils.getUUID();
			Map<String, Object> map = new HashMap<String, Object>();
			String avatar = Utils.getHash((String) Constant.DEFAULT_HEADER_NAME.getValue(), "MD5");
			map.put("uId", id);
			map.put("avatar", avatar);
			map.put("role", 10);
			map.put("status", 0);
			map.put("b_id", Utils.getUUID());
			map.put("identityType", userAuths.getIdentityType());
			map.put("identifier", userAuths.getIdentifier());
			map.put("credential", Utils.getHash(Utils.getHash(userAuths.getCredential(), "MD5"), "MD5"));
			map.put("verified", 1);
			map.put("registerTime", Utils.getTimestamp());
			if (userAuths.getRegisterIp() != null) {
				map.put("registerIp", userAuths.getRegisterIp());
				map.put("loginIp", userAuths.getRegisterIp());
			} else {
				map.put("registerIp", "");
				map.put("loginIp", "");
			}
			map.put("loginTime", Utils.getTimestamp());
			map.put("c_id", Utils.getUUID());
			map.put("nickName", userAuths.getIdentifier());
			map.put("age", 18);
			map.put("stature", 0);
			map.put("weight", 0);
			map.put("birthday", Date.valueOf("1971-01-01"));
			map.put("sex", 0);
			map.put("hand", 0);
			map.put("userAimStep", 0);
			map.put("deviceStatus", 0);
			map.put("remark", "");
			// 是否注册成功
			if (userMapper.register(map) > 0) {
				map2.put("id", id);
				map2.put("avatar", Constant.BASE_IP.getValue() + File.separator + "user" + File.separator
						+ "getHeader?id=" + avatar);
				map2.put("identifier", userAuths.getIdentifier());
				map2.put("nickName", userAuths.getIdentifier());
				map2.put("age", 18);
				map2.put("stature", 0);
				map2.put("weight", 0);
				map2.put("birthday", "1971-01-01");
				map2.put("sex", 0);
				map2.put("userAimStep", 0);
			}
		}
		return map2;
	}

	@Override
	public int editPhone(UserAuths userAuths, String newPhone) {
		int res = 0;
		Map<String, Object> map = getMsgFromUId(userAuths);
		res = (int) map.get("res");
		if (res == 1) {
			// 验证密码是否正确
			if (Utils.getHash(Utils.getHash(userAuths.getCredential(), "MD5"), "MD5").equals(map.get("credential"))) {
				userAuths.setId((String) map.get("id"));
				userAuths.setIdentifier(newPhone);
				map = getMsgFromIdentifier(userAuths);
				// 未被注册的手机号才可以修改
				if ((int) map.get("res") == 2) {
					if (userMapper.updateByIdentifier(userAuths) > 0) {
						res = 1;
					} else {
						// 手机号修改失败
						res = 0;
					}
				} else {
					// 该手机号码已被注册
					res = 5;
				}
			} else {
				// 账号或密码错误
				res = 4;
			}
		}
		return res;
	}

	@Override
	public int editPass(UserAuths userAuths, String newPass) {
		int res = 0;
		Map<String, Object> map = getMsgFromIdentifier(userAuths);
		res = (int) map.get("res");
		if (res == 1) {
			// 验证密码是否正确
			if (Utils.getHash(Utils.getHash(userAuths.getCredential(), "MD5"), "MD5").equals(map.get("credential"))) {
				userAuths.setId((String) map.get("id"));
				userAuths.setCredential(Utils.getHash(Utils.getHash(newPass, "MD5"), "MD5"));
				if (userMapper.updateByCredential(userAuths) > 0) {
					res = 1;
				} else {
					// 密码修改失败
					res = 0;
				}
			} else {
				// 密码错误
				res = 4;
			}
		}
		return res;
	}

	@Override
	public int forgetPass(UserAuths userAuths, String newPass) {
		int res = 0;
		Map<String, Object> map = getMsgFromIdentifier(userAuths);
		res = (int) map.get("res");
		// 账户存在并且状态正常
		if (res == 1) {
			userAuths.setId((String) map.get("id"));
			userAuths.setCredential(Utils.getHash(Utils.getHash(newPass, "MD5"), "MD5"));
			// 修改密码
			if (userMapper.updateByCredential(userAuths) > 0) {
				res = 1;
			} else {
				// 密码修改失败
				res = 0;
			}
		}
		return res;
	}

	@Override
	public int updateSetting(String uId, String data) {
		int res = 0;
		Map<String, Object> map = JSON.parseObject(data);
		if (!map.isEmpty()) {
			res = 1;
			if (map.get("nickName") != null) {
				Pattern pattern = Pattern.compile("[a-zA-Z0-9_\u4e00-\u9fa5]{1,11}");
				Matcher c = pattern.matcher((String) map.get("nickName"));
				if (!c.matches()) {
					// 昵称设置错误，由中文或字符组成 长度(1,10)
					res = 2;
				}
			}
			if (map.get("age") != null) {
				if ((int) map.get("age") < 0 || (int) map.get("age") > 666) {
					// 年龄设置错误 范围(0,666)
					res = 3;
				}
			}
			if (map.get("stature") != null) {
				if ((int) map.get("stature") < 0 || (int) map.get("stature") > 666) {
					// 身高设置错误 范围(0,666)
					res = 4;
				}
			}
			if (map.get("weight") != null) {
				if ((int) map.get("weight") < 0 || (int) map.get("weight") > 666) {
					// 体重设置错误 范围(0,666)
					res = 5;
				}
			}
			if (map.get("sex") != null) {
				if ((int) map.get("sex") != 0 && (int) map.get("sex") != 1) {
					// 性别设置错误 0女/1男
					res = 6;
				}
			}
			if (map.get("hand") != null) {
				if ((int) map.get("hand") != 0 && (int) map.get("hand") != 1) {
					// 穿戴左右设置错误 0右/1左
					res = 7;
				}
			}
			if (map.get("userAimStep") != null) {
				if ((int) map.get("userAimStep") < 0 || (int) map.get("userAimStep") > 2147483647) {
					// 目标步数设置错误 范围(0,2147483647)
					res = 8;
				}
			}
			if (res == 1) {
				map.put("uId", uId);
				if (!(userMapper.updateSetting(map) > 0)) {
					// 用户设置数据失败
					res = 0;
				}
			}
		}
		return res;
	}

	@Override
	public String findByHeader(String uId) {
		List<User> list = userMapper.findById(uId);
		if (list.size() > 0) {
			return list.get(0).getAvatar();
		}
		return null;
	}

	@Override
	public boolean updateByHeader(User user) {
		return userMapper.updateAvatar(user) > 0;
	}
	
}
