package com.zhizhuotec.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhizhuotec.common.pojo.Result;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.entity.BloodPressure;
import com.zhizhuotec.service.BloodPressureService;

@Controller
@RequestMapping("bloodPressure")
public class BloodPressureController extends BaseController {

	@Autowired
	private BloodPressureService bloodPressureService;

	// 下载起始日期的所有血压数据
	@RequestMapping(value = "download", method = RequestMethod.GET)
	@ResponseBody
	public Result download(BloodPressure bloodPressure) {
		String uId = getUId();
		if (uId != null) {
			if (bloodPressure.getDates() != null) {
				Map<String, Object> map = new HashMap<String, Object>();
				bloodPressure.setUserId(uId);
				// 查询起始日期的所有数据
				List<BloodPressure> list = bloodPressureService.download(bloodPressure);
				if (list.size() > 0) {
					// 获取数据成功
					map.put("res", 1);
					map.put("rows", list);
				} else {
					// 获取数据为空
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

	// 下载日期范围年、月、周、日的血压数据
	@RequestMapping(value = "downloadRange", method = RequestMethod.GET)
	@ResponseBody
	public Result downloadRange(BloodPressure bloodPressure, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			Long date = bloodPressure.getDates();
			if ((date != null) && (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				Map<String, Object> map = new HashMap<String, Object>();
				List<BloodPressure> list = null;
				if (plusType == null || plusType == 0) {
					// 查询日期范围数据
					list = bloodPressureService.findByDays(uId, Utils.days(date, 0, plusType),
							Utils.days(date, 0, plusType));
				} else {
					// 查询日期范围数据
					list = bloodPressureService.findByDays(uId, Utils.days(date, 0, plusType - 10),
							Utils.days(date, 0, plusType));
				}
				if (list.size() > 0) {
					// 获取数据成功
					map.put("res", 1);
					map.put("rows", list);
				} else {
					// 获取数据为空
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

	// 上传血压数据
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Result upload(String data) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(data)) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 上传data 返回res
				map.put("res", bloodPressureService.upload(uId, data));
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}
}
