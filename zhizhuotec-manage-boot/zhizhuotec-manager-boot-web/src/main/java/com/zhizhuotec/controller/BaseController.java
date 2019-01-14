package com.zhizhuotec.controller;

import java.util.ArrayList;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.alibaba.fastjson.JSON;
import com.zhizhuotec.common.pojo.Constant;
import com.zhizhuotec.common.redis.RedisService;
import com.zhizhuotec.common.utils.Des3;
import com.zhizhuotec.common.utils.Utils;

@CrossOrigin(maxAge = 3600)
public class BaseController {

	@Autowired
	protected RedisService redisService;

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	@Autowired
	protected HttpSession session;

	/**
	 * 安卓
	 */
	public String getToken(String uId) {
		String token = null;
		if (StringUtils.isNotBlank(uId)) {
			try {
				token = Des3.encode(uId + ", " + System.currentTimeMillis());
			} catch (Exception e) {
				Utils.logger("ERROR", "Server exception：", e);
			}
			if (token != null) {
				try {
					redisService.set(uId, token);
					redisService.expire(uId, (int) Constant.EXPIRE.getValue());
					token = token.replace("+", "2B%");
				} catch (Exception e) {
					Utils.logger("ERROR", "Server exception：", e);
					return null;
				}
			}
		}
		return token;
	}

	/**
	 * 安卓
	 */
	public String getUId() {
		String token = request.getParameter("token");
		if (!(StringUtils.isBlank(token) || ("\"\"").equals(token))) {
			try {
				token = token.replace("2B%", "+");
				String[] s = Des3.decode(token).split(", ");
				if (s != null && s.length > 0 && redisService.ttl(s[0]) > 0) {
					String value = redisService.get(s[0]);
					if (value != null && value.equals(token)) {
						redisService.expire(s[0], (int) Constant.EXPIRE.getValue());
						return s[0];
					}
				}
			} catch (Exception e) {
				Utils.logger("ERROR", "Server exception：", e);
			}
		}
		return null;
	}

	/**
	 * 后台
	 */
	public String getBackstageToken(String uId) {
		String token = null;
		if (StringUtils.isNotBlank(uId)) {
			try {
				token = Des3.encode(uId + ", " + System.currentTimeMillis());
				session.setAttribute(uId, token);
				token = token.replace("+", "2B%");
			} catch (Exception e) {
				Utils.logger("ERROR", "Server exception：", e);
			}
		}
		return token;
	}

	/**
	 * 后台
	 */
	public String getBackstageUId() {
		String token = request.getParameter("token");
		if (!(StringUtils.isBlank(token) || ("\"\"").equals(token))) {
			try {
				token = token.replace("2B%", "+");
				String[] s = Des3.decode(token).split(", ");
				if (s != null && s.length > 0) {
					Object value = session.getAttribute(s[0]);
					if (value != null && value.equals(token)) {
						return s[0];
					}
				}
			} catch (Exception e) {
				Utils.logger("ERROR", "Server exception：", e);
			}
		}
		return null;
	}

	/**
	 * 获取ip
	 */
	public String getIp() {
		String ip = request.getHeader("X-Real-IP");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			return ip;
		}
		ip = request.getHeader("X-Forwarded-For");
		if (!StringUtils.isBlank(ip) && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个IP值，第一个为真实IP。
			int index = ip.indexOf(',');
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		} else {
			return request.getRemoteAddr();
		}
	}

	/**
	 * 限制每个手机号码发送验证码(每分钟3次, 每小时10次, 每天20次)
	 */
	public boolean sendCodeOut(String key) {
		boolean result = false;
		long newTime = System.currentTimeMillis();
		long oldTime = 0;
		List<Object> list = new ArrayList<Object>();
		if (redisService.ttl(key) > 0) {
			list = JSON.parseArray(redisService.get(key));
			if (list.size() > 0 && (int) list.get(4) > 0) {
				list.set(4, ((int) list.get(4)) - 1);
				oldTime = (long) list.get(1);
				if (newTime - oldTime < 60 * 60 * 1000) {
					if (((int) list.get(3)) > 0) {
						list.set(3, ((int) list.get(3)) - 1);
						oldTime = (long) list.get(0);
						if (newTime - oldTime < 60 * 1000) {
							if (((int) list.get(2)) > 0) {
								list.set(2, ((int) list.get(2)) - 1);
								result = true;
							}
						} else {
							list.set(0, newTime);
							list.set(2, (int) Constant.IDENTIFIER_COUNT1.getValue() - 1);
							result = true;
						}
					}
				} else {
					list.set(0, newTime);
					list.set(1, newTime);
					list.set(2, (int) Constant.IDENTIFIER_COUNT1.getValue() - 1);
					list.set(3, (int) Constant.IDENTIFIER_COUNT2.getValue() - 1);
					result = true;
				}
			}
			if (result) {
				redisService.set(key, JSON.toJSONString(list));
				redisService.expire(key, ((int) (Utils.endOfDay() / 1000)) + 1);
			}
		} else {
			list.add(newTime);
			list.add(newTime);
			list.add((int) Constant.IDENTIFIER_COUNT1.getValue() - 1);
			list.add((int) Constant.IDENTIFIER_COUNT2.getValue() - 1);
			list.add((int) Constant.IDENTIFIER_COUNT3.getValue() - 1);
			redisService.set(key, JSON.toJSONString(list));
			redisService.expire(key, ((int) (Utils.endOfDay() / 1000)) + 1);
			result = true;
		}
		return result;
	}
}
