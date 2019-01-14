package com.zhizhuotec.controller;

import com.zhizhuotec.common.pojo.Constant;
import com.zhizhuotec.common.pojo.Result;
import com.zhizhuotec.common.utils.AliYunSms;
import com.zhizhuotec.common.utils.UpUtils;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.entity.User;
import com.zhizhuotec.entity.UserAuths;
import com.zhizhuotec.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	// 用户登录
	@RequestMapping(value = "login", method = RequestMethod.GET)
	@ResponseBody
	public Result login(UserAuths userAuths) {
		if (Utils.verifyPhone(userAuths.getIdentifier()) && Utils.verifyPass(userAuths.getCredential())) {
			userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
			userAuths.setLoginIp(getIp());
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> map2 = userService.login(userAuths);
			int res = (int) map2.get("res");
			if (res == 1) {
				String token = getToken((String) map2.get("id"));
				if (token != null) {
					// 登录成功
					map.put("res", 1);
					map2.put("token", token);
					map.put("userInfo", map2);
				} else {
					// 登录失败
					map.put("res", 0);
				}
			} else {
				// 返回res
				map.put("res", res);
			}
			return Result.ok(map);
		} else {
			return Result.build("111", "参数校验不通过111111");
		}
	}

	// 用户注册
	@ResponseBody
	@RequestMapping(value = "register", method = RequestMethod.POST)
	public Result register(UserAuths userAuths, String code) {
		if (code != null && Utils.verifyPhone(userAuths.getIdentifier())
				&& Utils.verifyPass(userAuths.getCredential())) {
			Map<String, Object> map = new HashMap<String, Object>();
			String key = userAuths.getIdentifier() + "_" + Constant.CODE_TYPE_1.getValue();
			if (redisService.ttl(key) > 0 && code.equals(redisService.get(key))) {
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				userAuths.setRegisterIp(getIp());
				Map<String, Object> map2 = userService.register(userAuths);
				if (map2.get("id") != null) {
					String token = getToken((String) map2.get("id"));
					if (token != null) {
						// 注册成功
						map.put("res", 1);
						map2.put("token", token);
						map.put("userInfo", map2);
					} else {
						// 注册失败
						map.put("res", 0);
					}
				} else {
					// 用户已存在
					map.put("res", 3);
				}
			} else {
				// 验证码错误
				map.put("res", 2);
			}
			return Result.ok(map);
		} else {
			return Result.build("111", "参数校验不通过");
		}
	}

	// 发送验证码(type = 1/注册,修改手机 2/忘记密码)
	@RequestMapping(value = "code", method = RequestMethod.PUT)
	@ResponseBody
	public Result code(UserAuths userAuths, Integer type) {
		if (type != null && (type == 1 || type == 2) && Utils.verifyPhone(userAuths.getIdentifier())) {
			Map<String, Object> map = new HashMap<String, Object>();
			userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
			int res = (int) userService.getMsgFromIdentifier(userAuths).get("res");
			if ((type == 1 && res == 2) || (type == 2 && res == 1)) {
				String codeValue = Utils.code();
				if (sendCodeOut(userAuths.getIdentifier())) {
					if (AliYunSms.sms(userAuths.getIdentifier(), codeValue)) {
						String codeKey = userAuths.getIdentifier() + "_" + type;
						redisService.set(codeKey, codeValue);
						redisService.expire(codeKey, (int) Constant.CODE_EXPIRE.getValue());
						// 发送成功
						map.put("res", 1);
					} else {
						// 发送失败
						map.put("res", 0);
					}
				} else {
					// 每天信息发送超限
					map.put("res", 5);
				}
			} else {
				if (type == 1) {
					map.put("res", 4);
				} else {
					// 2/账号不存在 3/账号已被停用 4/账号已存在,请登录
					map.put("res", res);
				}
			}
			return Result.ok(map);
		} else {
			return Result.build("111", "参数校验不通过");
		}
	}

	// 验证验证码
	@RequestMapping(value = "verifyCode", method = RequestMethod.GET)
	@ResponseBody
	public Result verifyCode(UserAuths userAuths, String code, Integer type) {
		if (code != null && type != null && (type == 1 || type == 2) && Utils.verifyPhone(userAuths.getIdentifier())) {
			Map<String, Object> map = new HashMap<String, Object>();
			String codeKey = userAuths.getIdentifier() + "_" + type;
			if (redisService.ttl(codeKey) > 0 && code.equals(redisService.get(codeKey))) {
				// 正确
				map.put("res", 1);
			} else {
				// 错误
				map.put("res", 0);
			}
			return Result.ok(map);
		} else {
			return Result.build("111", "参数校验不通过");
		}
	}

	// 获取验证码剩余时间
	@RequestMapping(value = "codeTime", method = RequestMethod.GET)
	@ResponseBody
	public Result codeTime(UserAuths userAuths, Integer type) {
		if (type != null && (type == 1 || type == 2) && Utils.verifyPhone(userAuths.getIdentifier())) {
			Map<String, Object> map = new HashMap<String, Object>();
			String codeKey = userAuths.getIdentifier() + "_" + type;
			long ttl = redisService.ttl(codeKey);
			if (ttl > 0) {
				map.put("res", 1);
				map.put("time", ttl);
			} else {
				// 验证码不存在或已过期
				map.put("res", 0);
			}
			return Result.ok(map);
		} else {
			return Result.build("111", "参数校验不通过");
		}
	}

	// 忘记密码
	@ResponseBody
	@RequestMapping(value = "forgetPass", method = RequestMethod.PUT)
	public Result forgetPass(UserAuths userAuths, String code, String newPass) {
		if (code != null && newPass != null && Utils.verifyPhone(userAuths.getIdentifier())
				&& Utils.verifyPass(newPass)) {
			Map<String, Object> map = new HashMap<String, Object>();
			String codeKey = userAuths.getIdentifier() + "_" + Constant.CODE_TYPE_2.getValue();
			if (redisService.ttl(codeKey) > 0 && code.equals(redisService.get(codeKey))) {
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				// 返回res
				map.put("res", userService.forgetPass(userAuths, newPass));
			} else {
				// 验证码错误
				map.put("res", 4);
			}
			return Result.ok(map);
		} else {
			return Result.build("111", "参数校验不通过");
		}
	}

	// 修改密码
	@ResponseBody
	@RequestMapping(value = "editPass", method = RequestMethod.PUT)
	public Result editPass(UserAuths userAuths, String newPass) {
		if (Utils.verifyPhone(userAuths.getIdentifier()) && Utils.verifyPass(userAuths.getCredential())
				&& Utils.verifyPass(newPass)) {
			Map<String, Object> map = new HashMap<String, Object>();
			userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
			// 返回res
			map.put("res", userService.editPass(userAuths, newPass));
			return Result.ok(map);
		} else {
			return Result.build("111", "参数校验不通过");
		}
	}

	// 修改手机
	@ResponseBody
	@RequestMapping(value = "editPhone", method = RequestMethod.PUT)
	public Result editPhone(UserAuths userAuths, String token, String newPhone, String newCode) {
		String uId = getUId();
		if (uId != null) {
			if (newCode != null && Utils.verifyPass(userAuths.getCredential()) && Utils.verifyPhone(newPhone)) {
				Map<String, Object> map = new HashMap<String, Object>();
				String newCodeKey = newPhone + "_" + Constant.CODE_TYPE_1.getValue();
				if (redisService.ttl(newCodeKey) > 0 && newCode.equals(redisService.get(newCodeKey))) {
					userAuths.setUserId(uId);
					userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
					// 返回res
					map.put("res", userService.editPhone(userAuths, newPhone));
				} else {
					// error:验证码错误
					map.put("res", 6);
				}
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 用户注销
	@ResponseBody
	@RequestMapping(value = "logout", method = RequestMethod.DELETE)
	public Result logout() {
		String uId = getUId();
		if (uId != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (redisService.del(uId) > 0) {
				// 注销成功
				map.put("res", 1);
			} else {
				// 注销失败
				map.put("res", 0);
			}
			return Result.ok(map);
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 用户设置
	@ResponseBody
	@RequestMapping(value = "setting", method = RequestMethod.PUT)
	public Result setting(String data) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(data)) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 返回res
				map.put("res", userService.updateSetting(uId, data));
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 头像上传
	@ResponseBody
	@RequestMapping(value = "uploadHeader", method = RequestMethod.PUT)
	public Result uploadHeader(String imgStr) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(imgStr)) {
				Map<String, Object> map = new HashMap<String, Object>();
				String imageName = Utils.getHash(uId, "MD5");
				User user = new User();
				user.setId(uId);
				user.setAvatar(imageName);
				if (UpUtils.uploadToBase64(imgStr, Constant.HEADER_FOLDER.getValue(),
						imageName + Constant.DEFAULT_HEADER_SUFFIX.getValue()) && userService.updateByHeader(user)) {
					// 上传成功
					map.put("res", 1);
					map.put("url", Constant.BASE_IP.getValue() + File.separator + "user" + File.separator
							+ "getHeader?id=" + imageName);
				} else {
					// 上传失败
					map.put("res", 0);
				}
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 获取头像
	@ResponseBody
	@RequestMapping(value = "getHeader", method = RequestMethod.GET)
	public void getHeader(String id) {
		if (StringUtils.isNotBlank(id)) {
			try {
				byte[] b = UpUtils
						.download(Constant.BASE_PATH.getValue() + UpUtils.SEPARATOR + Constant.HEADER_FOLDER.getValue()
								+ UpUtils.SEPARATOR + id + Constant.DEFAULT_HEADER_SUFFIX.getValue());
				if (b != null) {
					response.addHeader("Content-Disposition",
							"attachment;filename=" + id + Constant.DEFAULT_HEADER_SUFFIX.getValue());
					response.setContentType("image/" + Constant.DEFAULT_HEADER_SUFFIX.getValue());
					OutputStream os = response.getOutputStream();
					os.write(b);
					os.flush();
					os.close();
				}
			} catch (Exception e) {

			}
		}
	}

	// 获取固件升级地址
	@ResponseBody
	@RequestMapping(value = "firmwareUpgrade", method = RequestMethod.GET)
	public Result firmwareUpgrade() {
		String uId = getUId();
		if (uId != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			String fileName = UpUtils.findLastModifiedFile(Constant.BASE_PATH.getValue(),
					Constant.BLUETOOTH_FOLDER.getValue());
			if (fileName != null) {
				// 返回固件最新版本
				map.put("res", 1);
				map.put("version", Integer.valueOf(fileName.substring(0, fileName.indexOf("_"))));
				map.put("url", Constant.BASE_IP.getValue() + File.separator + "user" + File.separator
						+ "getFirmware?id=" + fileName);
			} else {
				// 没有固件文件
				map.put("res", 0);
			}
			return Result.ok(map);
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 获取固件文件
	@ResponseBody
	@RequestMapping(value = "getFirmware", method = RequestMethod.GET)
	public void getFirmware(String id) {
		if (StringUtils.isNotBlank(id)) {
			try {
				byte[] b = UpUtils.download(Constant.BASE_PATH.getValue() + UpUtils.SEPARATOR
						+ Constant.BLUETOOTH_FOLDER.getValue() + UpUtils.SEPARATOR + id);
				if (b != null) {
					response.addHeader("Content-Disposition", "attachment;filename=" + id);
					response.setContentType("multipart/form-data");
					OutputStream os = response.getOutputStream();
					os.write(b);
					os.flush();
					os.close();
				}
			} catch (Exception e) {
				Utils.logger("ERROR", "Server exception：", e);
			}
		}
	}

	// 获取app升级
	@ResponseBody
	@RequestMapping(value = "getApp", method = RequestMethod.GET)
	public void getApp() {
		try {
			String fileName = UpUtils.findLastModifiedFile(Constant.BASE_PATH.getValue(),
					Constant.UPGRADE_FOLDER.getValue());
			if (fileName != null) {
				byte[] b = UpUtils.download(Constant.BASE_PATH.getValue() + UpUtils.SEPARATOR
						+ Constant.UPGRADE_FOLDER.getValue() + UpUtils.SEPARATOR + fileName);
				if (b != null) {
					response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
					response.setContentType("multipart/form-data");
					OutputStream os = response.getOutputStream();
					os.write(b);
					os.flush();
					os.close();
				}
			}
		} catch (Exception e) {
			Utils.logger("ERROR", "Server exception：", e);
		}
	}
}
