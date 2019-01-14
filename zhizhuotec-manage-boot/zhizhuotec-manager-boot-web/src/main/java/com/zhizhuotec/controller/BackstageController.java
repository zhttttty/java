package com.zhizhuotec.controller;

import java.awt.Color;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhizhuotec.common.pojo.Constant;
import com.zhizhuotec.common.pojo.Result;
import com.zhizhuotec.common.utils.UpUtils;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.entity.BloodOxygenContent;
import com.zhizhuotec.entity.BloodPressure;
import com.zhizhuotec.entity.BreathingGuide;
import com.zhizhuotec.entity.HeartRate;
import com.zhizhuotec.entity.StepNumber;
import com.zhizhuotec.entity.User;
import com.zhizhuotec.entity.UserAuths;
import com.zhizhuotec.entity.UserSetting;
import com.zhizhuotec.service.BackstageService;
import com.zhizhuotec.service.BloodOxygenContentService;
import com.zhizhuotec.service.BloodPressureService;
import com.zhizhuotec.service.BreathingGuideService;
import com.zhizhuotec.service.HeartRateService;
import com.zhizhuotec.service.StepNumberService;

@Controller
@RequestMapping("backstage")
public class BackstageController extends BaseController {

	@Autowired
	private BloodOxygenContentService bloodOxygenContentService;

	@Autowired
	private BloodPressureService bloodPressureService;

	@Autowired
	private BreathingGuideService breathingGuideService;

	@Autowired
	private HeartRateService heartRateService;

	@Autowired
	private StepNumberService stepNumberService;

	@Autowired
	private BackstageService backstageService;

	// 后台管理员登录
	@RequestMapping(value = "login", method = RequestMethod.GET)
	@ResponseBody
	public Result login(UserAuths userAuths) {
		if (Utils.verifyPhone(userAuths.getIdentifier()) && Utils.verifyPass(userAuths.getCredential())) {
			userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
			userAuths.setLoginIp(getIp());
			Map<String, Object> map = backstageService.login(userAuths);
			if (map != null && map.get("msg") != null) {
				if (map.get("msg").equals("success")) {
					String token = getToken(map.get("id").toString());
					if (null != token) {
						map.put("res", 1);
						map.put("msg", "登陆成功");
						map.put("token", token);
					} else {
						map.clear();
						map.put("res", 0);
						map.put("msg", "token获取失败"); // error:token获取失败
					}
				} else {
					map.put("res", 0); // error:msg
				}
			} else {
				map.put("res", 0);
				map.put("msg", "系统异常"); // error:系统异常
			}
			return Result.build("success", "请求成功", map);
		} else {
			return Result.build("000", "参数校验不通过");
		}
	}

	// 用户注销
	@ResponseBody
	@RequestMapping(value = "logout", method = RequestMethod.DELETE)
	public Result logout() {
		String uId = getUId();
		if (uId != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			if (session.getAttribute(uId) != null) {
				session.removeAttribute(uId);
				map.put("res", 1);
				map.put("msg", "注销成功"); // success
			} else {
				map.put("res", 0);
				map.put("msg", "登录状态已失效"); // error:登录状态已失效
			}
			return Result.build("success", "请求成功", map);
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 图片验证码
	@ResponseBody
	@RequestMapping(value = "imageCode", method = RequestMethod.GET)
	public void imageCode() throws IOException {
		int width = 63;
		int height = 37;
		Random random = new Random();
		// 设置response头信息
		// 禁止缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		// 生成缓冲区image类
		BufferedImage image = new BufferedImage(width, height, 1);
		// 产生image类的Graphics用于绘制操作
		Graphics g = image.getGraphics();
		// Graphics类的样式
		g.setColor(Utils.getRandColor(200, 250));
		g.setFont(new Font("Times New Roman", 0, 28));
		g.fillRect(0, 0, width, height);
		// 绘制干扰线
		for (int i = 0; i < 40; i++) {
			g.setColor(Utils.getRandColor(130, 200));
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int x1 = random.nextInt(12);
			int y1 = random.nextInt(12);
			g.drawLine(x, y, x + x1, y + y1);
		}
		// 绘制字符
		String imageCode = "";
		for (int i = 0; i < 4; i++) {
			String rand = String.valueOf(random.nextInt(10));
			imageCode += rand;
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 28);
		}
		// 将字符保存到session中用于前端的验证
		redisService.set("imageCode", imageCode);
		redisService.expire("imageCode", 55555556);
		// session.setAttribute("imageCode", imageCode);
		g.dispose();
		ImageIO.write(image, "png", response.getOutputStream());
		response.getOutputStream().flush();
	}

	// 验证图片验证码
	@ResponseBody
	@RequestMapping(value = "verifyImageCode", method = RequestMethod.GET)
	public Result verifyImageCode(String code) {
		if (code != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			// Object imageCode = session.getAttribute("imageCode");
			Object imageCode = redisService.get("imageCode");
			if (code.equals(imageCode)) {
				map.put("res", 1);
				map.put("msg", "验证成功"); // success
			} else {
				map.put("res", 0);
				map.put("msg", "验证码错误"); // error:验证码错误
			}
			return Result.build("success", "请求成功", map);
		} else {
			return Result.build("000", "参数校验不通过");
		}
	}

	// 后台管理员获取用户数据
	@ResponseBody
	@RequestMapping(value = "info", method = RequestMethod.GET)
	public Result info(String criteria) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(criteria)) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<User> list = backstageService.info(uId, criteria);
				if (list != null && list.size() > 0) {
					JSONArray array = new JSONArray();
					Map<String, Object> map3 = null;
					for (User user : list) {
						map3 = new HashMap<String, Object>();
						map3.put("role", user.getRole());
						map3.put("status", user.getStatus());
						for (UserAuths auths : user.getUserAuths()) {
							map3.put("identifier", auths.getIdentifier());
							map3.put("loginIp", auths.getLoginIp());
							map3.put("registerTime", Utils.getTimestampStr(auths.getRegisterTime()));
						}
						for (UserSetting setting : user.getUserSetting()) {
							map3.put("nickName", setting.getNickName());
							map3.put("age", setting.getAge());
							map3.put("stature", setting.getStature());
							map3.put("weight", setting.getWeight());
							map3.put("sex", setting.getSex());
							map3.put("deviceStatus", setting.getDeviceStatus());
						}
						array.add(map3);
					}
					String s = JSON.toJSONString(array, SerializerFeature.DisableCircularReferenceDetect);
					if (s != null) {
						map.put("res", 1);
						map.put("msg", "获取数据成功"); // success
						map.put("rows", s);
					} else {
						map.put("res", 1);
						map.put("msg", "获取数据失败"); // error:获取数据失败
						map.put("rows", "");
					}
				} else {
					map.put("res", 1);
					map.put("msg", "获取数据失败"); // error:获取数据失败
					map.put("rows", "");
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 后台管理员设置用户数据
	@ResponseBody
	@RequestMapping(value = "settingInfo", method = RequestMethod.PUT)
	public Result settingInfo(String formMsg) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(formMsg)) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (backstageService.settingInfo(uId, formMsg)) {
					map.put("res", 1);
					map.put("msg", "保存成功"); // success
				} else {
					map.put("res", 0);
					map.put("msg", "保存失败"); // error:保存失败
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 后台管理员查看用户数据
	@ResponseBody
	@RequestMapping(value = "infoDetails", method = RequestMethod.GET)
	public Result infoDetails(String identifier) {
		String uId = getUId();
		if (uId != null) {
			if (Utils.verifyPhone(identifier)) {
				Map<String, Object> map = new HashMap<String, Object>();
				String s = backstageService.infoDetails(uId, identifier);
				if (null != s) {
					map.put("res", 1);
					map.put("msg", "查询成功"); // success
					map.put("rows", s);
				} else {
					map.put("res", 0);
					map.put("msg", "查询失败"); // error:查询失败
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 后台管理员设置用户设备状态备注
	@ResponseBody
	@RequestMapping(value = "setRemark", method = RequestMethod.PUT)
	public Result setRemark(String remark) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(remark)) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (backstageService.setRemark(uId, remark)) {
					map.put("res", 1);
					map.put("msg", "保存成功"); // success
				} else {
					map.put("res", 0);
					map.put("msg", "保存失败"); // error:保存失败
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 后台下载心率数据
	@RequestMapping(value = "heartRate", method = RequestMethod.GET)
	@ResponseBody
	public Result heartRate(UserAuths userAuths, String date, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			if ((Utils.verifyPhone(userAuths.getIdentifier()) && StringUtils.isNotBlank(date))
					&& (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				Map<String, Object> map = new HashMap<String, Object>();
				if (backstageService.getMsgFromUId(uId, 10000)) {
					String id = backstageService.getMsgFromIdentifier(userAuths);
					if (id != null) {
						List<HeartRate> list = null;
						if (plusType == null || plusType == 0) {
							list = heartRateService.findByDays(id, Utils.days(date, 1, plusType),
									Utils.days(date, 1, plusType));
						} else {
							list = heartRateService.findByDays(id, Utils.days(date, 1, plusType - 10),
									Utils.days(date, 1, plusType));
						}
						if (list.size() > 0) {
							map.put("res", 1);
							map.put("msg", "查询成功"); // success
							map.put("rows", JSON.toJSONString(list));
						} else {
							map.put("res", 1);
							map.put("msg", "查询数据为空"); // error:查询数据为空
							map.put("rows", "");
						}
					} else {
						map.put("res", 0);
						map.put("msg", "账号不存在"); // error:账号不存在
					}
				} else {
					map.put("res", 0);
					map.put("msg", "权限不足"); // error:权限不足
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 后台下载血氧数据
	@RequestMapping(value = "bloodOxygenContent", method = RequestMethod.GET)
	@ResponseBody
	public Result bloodOxygenContent(UserAuths userAuths, String date, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			if ((Utils.verifyPhone(userAuths.getIdentifier()) && StringUtils.isNotBlank(date))
					&& (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				Map<String, Object> map = new HashMap<String, Object>();
				if (backstageService.getMsgFromUId(uId, 10000)) {
					String id = backstageService.getMsgFromIdentifier(userAuths);
					if (id != null) {
						List<BloodOxygenContent> list = null;
						if (plusType == null || plusType == 0) {
							list = bloodOxygenContentService.findByDays(id, Utils.days(date, 1, plusType),
									Utils.days(date, 1, plusType));
						} else {
							list = bloodOxygenContentService.findByDays(id, Utils.days(date, 1, plusType - 10),
									Utils.days(date, 1, plusType));
						}
						if (list.size() > 0) {
							map.put("res", 1);
							map.put("msg", "查询成功"); // success
							map.put("rows", JSON.toJSONString(list));
						} else {
							map.put("res", 1);
							map.put("msg", "查询数据为空"); // error:查询数据为空
							map.put("rows", "");
						}
					} else {
						map.put("res", 0);
						map.put("msg", "账号不存在"); // error:账号不存在
					}
				} else {
					map.put("res", 0);
					map.put("msg", "权限不足"); // error:权限不足
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 后台下载血压数据
	@RequestMapping(value = "bloodPressure", method = RequestMethod.GET)
	@ResponseBody
	public Result bloodPressure(UserAuths userAuths, String date, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			if ((Utils.verifyPhone(userAuths.getIdentifier()) && StringUtils.isNotBlank(date))
					&& (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				Map<String, Object> map = new HashMap<String, Object>();
				if (backstageService.getMsgFromUId(uId, 10000)) {
					String id = backstageService.getMsgFromIdentifier(userAuths);
					if (id != null) {
						List<BloodPressure> list = null;
						if (plusType == null || plusType == 0) {
							list = bloodPressureService.findByDays(id, Utils.days(date, 1, plusType),
									Utils.days(date, 1, plusType));
						} else {
							list = bloodPressureService.findByDays(id, Utils.days(date, 1, plusType - 10),
									Utils.days(date, 1, plusType));
						}
						if (list.size() > 0) {
							map.put("res", 1);
							map.put("msg", "查询成功"); // success
							map.put("rows", JSON.toJSONString(list));
						} else {
							map.put("res", 1);
							map.put("msg", "查询数据为空"); // error:查询数据为空
							map.put("rows", "");
						}
					} else {
						map.put("res", 0);
						map.put("msg", "账号不存在"); // error:账号不存在
					}
				} else {
					map.put("res", 0);
					map.put("msg", "权限不足"); // error:权限不足
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 后台下载呼吸引导数据
	@RequestMapping(value = "breathingGuide", method = RequestMethod.GET)
	@ResponseBody
	public Result breathingGuide(UserAuths userAuths, String date, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			if ((Utils.verifyPhone(userAuths.getIdentifier()) && StringUtils.isNotBlank(date))
					&& (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				Map<String, Object> map = new HashMap<String, Object>();
				if (backstageService.getMsgFromUId(uId, 10000)) {
					String id = backstageService.getMsgFromIdentifier(userAuths);
					if (id != null) {
						List<BreathingGuide> list = null;
						if (plusType == null || plusType == 0) {
							list = breathingGuideService.findByDays(id, Utils.days(date, 1, plusType),
									Utils.days(date, 1, plusType));
						} else {
							list = breathingGuideService.findByDays(id, Utils.days(date, 1, plusType - 10),
									Utils.days(date, 1, plusType));
						}
						if (list.size() > 0) {
							map.put("res", 1);
							map.put("msg", "查询成功"); // success
							map.put("rows", JSON.toJSONString(list));
						} else {
							map.put("res", 1);
							map.put("msg", "查询数据为空"); // error:查询数据为空
							map.put("rows", "");
						}
					} else {
						map.put("res", 0);
						map.put("msg", "账号不存在"); // error:账号不存在
					}
				} else {
					map.put("res", 0);
					map.put("msg", "权限不足"); // error:权限不足
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 后台下载计步数据
	@RequestMapping(value = "stepNumber", method = RequestMethod.GET)
	@ResponseBody
	public Result stepNumber(UserAuths userAuths, String date, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			if ((Utils.verifyPhone(userAuths.getIdentifier()) && StringUtils.isNotBlank(date))
					&& (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				Map<String, Object> map = new HashMap<String, Object>();
				if (backstageService.getMsgFromUId(uId, 10000)) {
					String id = backstageService.getMsgFromIdentifier(userAuths);
					if (id != null) {
						List<StepNumber> list = null;
						if (plusType == null || plusType == 0) {
							list = stepNumberService.findByDays(id, Utils.days(date, 1, plusType),
									Utils.days(date, 1, plusType));
						} else {
							list = stepNumberService.findByDays(id, Utils.days(date, 1, plusType - 10),
									Utils.days(date, 1, plusType));
						}
						if (list.size() > 0) {
							map.put("res", 1);
							map.put("msg", "查询成功"); // success
							map.put("rows", JSON.toJSONString(list));
						} else {
							map.put("res", 1);
							map.put("msg", "查询数据为空"); // error:查询数据为空
							map.put("rows", "");
						}
					} else {
						map.put("res", 0);
						map.put("msg", "账号不存在"); // error:账号不存在
					}
				} else {
					map.put("res", 0);
					map.put("msg", "权限不足"); // error:权限不足
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	@RequestMapping(value = "electrocardiogram", method = RequestMethod.GET)
	@ResponseBody
	public Result electrocardiogram(UserAuths userAuths, String date, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			if ((Utils.verifyPhone(userAuths.getIdentifier()) && StringUtils.isNotBlank(date))
					&& (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				Map<String, Object> map = new HashMap<String, Object>();
				if (backstageService.getMsgFromUId(uId, 10000)) {
					String id = backstageService.getMsgFromIdentifier(userAuths);
					if (id != null) {
						Map<Long, Object> map3 = null;
						if (plusType == null || plusType == 0) {
							map3 = UpUtils.readFileByLine(id, Utils.getNioFileName(Utils.days(date, 1, plusType)),
									Utils.getNioFileName(Utils.days(date, 1, plusType)));
						} else {
							map3 = UpUtils.readFileByLine(id, Utils.getNioFileName(Utils.days(date, 1, plusType - 10)),
									Utils.getNioFileName(Utils.days(date, 1, plusType)));
						}
						if (!map3.isEmpty()) {
							map.put("res", 1); // success
							map.put("msg", "查询成功");
							map.put("rows", map3);
						} else {
							map.put("res", 1); // error:获取数据为空
							map.put("msg", "查询数据为空");
							map.put("rows", "");
						}
					} else {
						map.put("res", 0);
						map.put("msg", "账号不存在"); // error:账号不存在
					}
				} else {
					map.put("res", 0);
					map.put("msg", "权限不足"); // error:权限不足
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

	// 固件上传
	@ResponseBody
	@RequestMapping(value = "firmwareUpload", method = RequestMethod.PUT)
	public Result firmwareUpload(String imgStr, String fileName, Integer version) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(imgStr) && StringUtils.isNotBlank(fileName) && fileName.endsWith(".bin")
					&& version != null && version > 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				String firmwareName = version + "_" + fileName;
				if (UpUtils.uploadToBase64(imgStr, Constant.BLUETOOTH_FOLDER.getValue(), firmwareName)) {
					map.put("res", 1);
					map.put("msg", "上传成功"); // success
				} else {
					map.put("res", 0);
					map.put("msg", "上传失败"); // 上传失败
				}
				return Result.build("success", "请求成功", map);
			} else {
				return Result.build("000", "参数校验不通过");
			}
		} else {
			return Result.build("111", "登陆失效，请重新登录");
		}
	}

}
