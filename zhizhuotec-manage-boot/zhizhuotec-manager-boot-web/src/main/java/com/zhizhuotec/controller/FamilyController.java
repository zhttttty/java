package com.zhizhuotec.controller;

import java.io.File;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zhizhuotec.common.pojo.Constant;
import com.zhizhuotec.common.pojo.Result;
import com.zhizhuotec.common.utils.UpUtils;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.entity.Family;
import com.zhizhuotec.entity.Family_health;
import com.zhizhuotec.entity.Family_list;
import com.zhizhuotec.entity.Family_notification;
import com.zhizhuotec.entity.UserAuths;
import com.zhizhuotec.service.BloodOxygenContentService;
import com.zhizhuotec.service.BloodPressureService;
import com.zhizhuotec.service.FamilyService;
import com.zhizhuotec.service.HeartRateService;
import com.zhizhuotec.service.StepNumberService;
import com.zhizhuotec.service.UserService;

@Controller
@RequestMapping("family")
public class FamilyController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private BloodOxygenContentService bloodOxygenContentService;

	@Autowired
	private BloodPressureService bloodPressureService;

	@Autowired
	private HeartRateService heartRateService;

	@Autowired
	private StepNumberService stepNumberService;

	@Autowired
	private FamilyService familyService;

	// 获取验证消息列表
	@RequestMapping(value = "getNotificationList", method = RequestMethod.GET)
	@ResponseBody
	public Result getNotification() {
		String uId = getUId();
		if (uId != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			// 从缓存读取验证消息列表
			String data = redisService.get(uId + "_" + Constant.FAMILY_TYPE_1.getValue());
			if (StringUtils.isNotBlank(data)) {
				List<Map<String, Object>> oldList = JSON.parseObject(data,
						new TypeReference<List<Map<String, Object>>>() {
						});
				int oldListSize = oldList.size();
				if (oldListSize > 0) {
					// 获取验证消息列表人员信息
					List<Map<String, Object>> newList = familyService.getNotificationMsg(oldList);
					int newListSize = newList.size();
					for (int i = 0; i < oldListSize; i++) {
						for (int j = 0; j < newListSize; j++) {
							if (oldList.get(i).get("id").equals(newList.get(j).get("id"))) {
								// 拼接头像地址
								String avatar = Constant.BASE_IP.getValue() + File.separator + "user" + File.separator
										+ "getHeader?id=" + newList.get(j).get("avatar");
								oldList.get(i).putAll(newList.get(j));
								// 覆盖头像地址
								oldList.get(i).put("avatar", avatar);
								break;
							}
						}
					}
					// 获取验证消息列表成功
					map.put("res", 1);
					map.put("rows",
							JSON.parseObject(
									JSON.toJSONString(oldList, SerializerFeature.DisableCircularReferenceDetect),
									new TypeReference<List<Family_notification>>() {
									}));
				} else {
					// 用户还没有验证消息
					map.put("res", 0);
				}
			} else {
				// 用户还没有验证消息
				map.put("res", 0);
			}
			return Result.ok(map);
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 发送验证消息
	@RequestMapping(value = "sendNotification", method = RequestMethod.POST)
	@ResponseBody
	public Result sendNotification(String familyPhone, String msg) {
		String uId = getUId();
		if (uId != null) {
			if (Utils.verifyPhone(familyPhone)) {
				Map<String, Object> map = new HashMap<String, Object>();
				if (StringUtils.isBlank(msg)) {
					msg = "";
				}
				List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
				UserAuths userAuths = new UserAuths();
				userAuths.setIdentityType((String) Constant.IDENTITYTYPE_PHONE.getValue());
				userAuths.setIdentifier(familyPhone);
				Map<String, Object> map2 = userService.getMsgFromIdentifier(userAuths);
				// 要添加的用户是否存在
				if ((int) map2.get("res") != 2) {
					String familyUId = map2.get("userId").toString();
					if (!uId.equals(familyUId)) {
						String familyMsgKey = familyUId + "_" + Constant.FAMILY_TYPE_1.getValue();
						Map<String, Object> map3 = new HashMap<String, Object>();
						// 发送人uId
						map3.put("id", uId);
						// 验证消息
						map3.put("msg", msg);
						// 验证状态(0/已发送状态,1/同意，2/拒绝)
						map3.put("status", 0);
						try {
							String familyMsgValue = redisService.get(familyMsgKey);
							if (familyMsgValue != null) {
								list = JSON.parseObject(familyMsgValue, new TypeReference<List<Map<String, Object>>>() {
								});
								int n = 0;
								for (Map<String, Object> map4 : list) {
									if (uId.equals(map4.get("uId"))) {
										n++;
									}
								}
								// 对一个人发送最多10条验证信息
								if (n <= 10) {
									// 验证信息消息编号
									map3.put("number", list.size() + 1);
									list.add(map3);
									redisService.set(familyMsgKey,
											JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect));
								}
							} else {
								map3.put("number", 1);
								list.add(map3);
								redisService.set(familyMsgKey,
										JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect));
							}
							redisService.expire(familyMsgKey, (int) Constant.FAMILY_EXPIRE.getValue());
							// 验证信息发送成功
							map.put("res", 1);
						} catch (Exception e) {
							Utils.logger("ERROR", "Server exception：", e);
							// 验证信息发送失败
							map.put("res", 0);
						}
					} else {
						// 不能添加自己为家庭成员
						map.put("res", 3);
					}
				} else {
					// 账号不存在
					map.put("res", 2);
				}
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 获取家庭成员列表
	@RequestMapping(value = "getFamilyList", method = RequestMethod.GET)
	@ResponseBody
	public Result getFamilyList() {
		String uId = getUId();
		if (uId != null) {
			Map<String, Object> map = new HashMap<String, Object>();
			List<Family> list = familyService.findFamilyById(uId);
			if (list.size() > 0) {
				// 获取家庭成员列表
				List<Map<String, Object>> oldList = JSON.parseObject(list.get(0).getFamilyList(),
						new TypeReference<List<Map<String, Object>>>() {
						});
				int oldListSize = oldList.size();
				if (oldListSize > 0) {
					// 获取家庭成员列表信息
					List<Map<String, Object>> newList = familyService.getFamilyMsg(oldList);
					int newListSize = newList.size();
					for (int i = 0; i < newListSize; i++) {
						for (int j = 0; j < oldListSize; j++) {
							if (newList.get(i).get("id").equals(oldList.get(j).get("id"))) {
								if (oldList.get(i).get("remark") == null) {
									// 默认备注为用户昵称
									oldList.get(i).put("remark", newList.get(i).get("nickName"));
								}
								// 修改生日日期格式
								String birthday = Utils.getYMD(newList.get(i).get("birthday"));
								// 拼接头像地址
								String avatar = Constant.BASE_IP.getValue() + File.separator + "user" + File.separator
										+ "getHeader?id=" + newList.get(i).get("avatar");
								newList.get(i).put("birthday", birthday);
								newList.get(i).put("avatar", avatar);
								// 合并list
								newList.get(i).putAll(oldList.get(i));
							}
						}
					}
					// 获取家庭成员列表成功
					map.put("res", 1);
					map.put("rows",
							JSON.parseObject(
									JSON.toJSONString(newList, SerializerFeature.DisableCircularReferenceDetect),
									new TypeReference<List<Family_list>>() {
									}));
				} else {
					// 该用户还没有家庭成员
					map.put("res", 0);
				}
			} else {
				// 该用户还没有家庭成员
				map.put("res", 0);
			}
			return Result.ok(map);
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 添加家庭成员
	@RequestMapping(value = "addFamily", method = RequestMethod.POST)
	@ResponseBody
	public Result addFamily(String familysId, Integer status, Integer number) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(familysId) && (status == 1 || status == 2) && number != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				String familyKey = uId + "_" + Constant.FAMILY_TYPE_1.getValue();
				// 获取验证信息列表
				String data = redisService.get(familyKey);
				if (StringUtils.isNotBlank(data)) {
					boolean re = false;
					List<Map<String, Object>> list = JSON.parseObject(data,
							new TypeReference<List<Map<String, Object>>>() {
							});
					int size = list.size();
					for (int i = 0; i < size; i++) {
						if (familysId.equals(list.get(i).get("id")) && (int) list.get(i).get("status") == 0
								&& (int) list.get(i).get("number") == number) {
							// 更新验证状态
							list.get(i).put("status", status);
							re = true;
							break;
						}
					}
					if (re) {
						redisService.set(familyKey,
								JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect));
						if (status == 2 || (status == 1 && familyService.addFamily(uId, familysId))) {
							// 验证状态修改成功
							map.put("res", 1);
						} else {
							// 验证状态修改失败
							map.put("res", 0);
						}
					} else {
						// 验证信息已过期
						map.put("res", 3);
					}
				} else {
					// 用户还没有验证信息
					map.put("res", 2);
				}
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 修改家庭成员备注
	@RequestMapping(value = "updateRemark", method = RequestMethod.PUT)
	@ResponseBody
	public Result updateRemark(String familysId, String remark) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(familysId) && StringUtils.isNotBlank(remark)) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 返回res
				map.put("res", familyService.updateRemark(uId, familysId, remark));
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 删除家庭成员
	@RequestMapping(value = "deletFamily", method = RequestMethod.DELETE)
	@ResponseBody
	public Result deletFamily(String familysId) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(familysId)) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 返回res
				map.put("res", familyService.deletFamily(uId, familysId));
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

	// 获取家庭成员健康信息
	@RequestMapping(value = "getFamilyHealth", method = RequestMethod.GET)
	@ResponseBody
	public Result getFamilyHealth(String familysId, Long dates, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(familysId) && dates != null
					&& (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				Map<String, Object> map = new HashMap<String, Object>();
				Family_health health = new Family_health();
				if (plusType == null || plusType == 0) {
					// 获取血氧数据
					health.setBloodOxygenContents(bloodOxygenContentService.findByDays(familysId,
							Utils.days(dates, 0, plusType), Utils.days(dates, 0, plusType)));
					// 获取血压数据
					health.setBloodPressures(bloodPressureService.findByDays(familysId, Utils.days(dates, 0, plusType),
							Utils.days(dates, 0, plusType)));
					// 获取心率数据
					health.setHeartRates(heartRateService.findByDays(familysId, Utils.days(dates, 0, plusType),
							Utils.days(dates, 0, plusType)));
					// 获取计步数据
					health.setStepNumbers(stepNumberService.findByDays(familysId, Utils.days(dates, 0, plusType),
							Utils.days(dates, 0, plusType)));
					// 获取心电图数据
					health.setElectrocardiograms(
							UpUtils.readFileByLine(familysId, Utils.getNioFileName(Utils.days(dates, 0, plusType)),
									Utils.getNioFileName(Utils.days(dates, 0, plusType))));
				} else {
					health.setBloodOxygenContents(bloodOxygenContentService.findByDays(familysId,
							Utils.days(dates, 0, plusType - 10), Utils.days(dates, 0, plusType)));
					health.setBloodPressures(bloodPressureService.findByDays(familysId,
							Utils.days(dates, 0, plusType - 10), Utils.days(dates, 0, plusType)));
					health.setHeartRates(heartRateService.findByDays(familysId, Utils.days(dates, 0, plusType - 10),
							Utils.days(dates, 0, plusType)));
					health.setStepNumbers(stepNumberService.findByDays(familysId, Utils.days(dates, 0, plusType - 10),
							Utils.days(dates, 0, plusType)));
					health.setElectrocardiograms(
							UpUtils.readFileByLine(familysId, Utils.getNioFileName(Utils.days(dates, 0, plusType - 10)),
									Utils.getNioFileName(Utils.days(dates, 0, plusType))));
				}
				// 获取家庭成员健康数据成功
				map.put("res", 1);
				map.put("rows", health);
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}

}
