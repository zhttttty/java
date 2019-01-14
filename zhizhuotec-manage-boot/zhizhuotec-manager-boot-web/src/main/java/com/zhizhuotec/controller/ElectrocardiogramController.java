package com.zhizhuotec.controller;

import java.util.HashMap;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhizhuotec.common.pojo.Result;
import com.zhizhuotec.common.utils.UpUtils;
import com.zhizhuotec.common.utils.Utils;

@Controller
@RequestMapping("electrocardiogram")
public class ElectrocardiogramController extends BaseController {

	// 下载起始日期的所有心电数据
	@RequestMapping(value = "download", method = RequestMethod.GET)
	@ResponseBody
	public Result download(Long dates) {
		String uId = getUId();
		if (uId != null) {
			if (dates != null && dates > 0) {
				Map<String, Object> map = new HashMap<String, Object>();
				// 查询起始日期的所有数据
				Map<Long, Object> map2 = UpUtils.readFileByLine(uId, Utils.getNioFileName(dates));
				if (map2 != null && !map2.isEmpty()) {
					// 获取数据成功
					map.put("res", 1);
					map.put("rows", map2);
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

	// 下载日期范围年、月、周、日的心电数据
	@RequestMapping(value = "downloadRange", method = RequestMethod.GET)
	@ResponseBody
	public Result downloadRange(Long dates, Integer plusType) {
		String uId = getUId();
		if (uId != null) {
			if (dates != null && (((plusType == null || plusType == 0)) || (plusType >= 11 && plusType <= 13))) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<Long, Object> map2 = null;
				if (plusType == null || plusType == 0) {
					// 查询日期范围数据
					map2 = UpUtils.readFileByLine(uId, Utils.getNioFileName(Utils.days(dates, 0, plusType)),
							Utils.getNioFileName(Utils.days(dates, 0, plusType)));
				} else {
					// 查询日期范围数据
					map2 = UpUtils.readFileByLine(uId, Utils.getNioFileName(Utils.days(dates, 0, plusType - 10)),
							Utils.getNioFileName(Utils.days(dates, 0, plusType)));
				}
				if (map2 != null && !map2.isEmpty()) {
					// 获取数据成功
					map.put("res", 1);
					map.put("rows", map2);
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

	// 上传心电数据
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	@ResponseBody
	public Result upload(String data) {
		String uId = getUId();
		if (uId != null) {
			if (StringUtils.isNotBlank(data)) {
				Map<String, Object> map = new HashMap<String, Object>();
				Map<Long, String> newMap = JSON.parseObject(data, new TypeReference<Map<Long, String>>() {
				});
				if (!newMap.isEmpty()) {
					// 上传data
					for (Map.Entry<Long, String> entry : newMap.entrySet()) {
						UpUtils.writeFileByLine(uId, entry.getKey(), entry.getValue());
					}
					// 上传数据成功
					map.put("res", 1);
				} else {
					// 上传数据为空
					map.put("res", 1);
				}
				return Result.ok(map);
			} else {
				return Result.build("111", "参数校验不通过");
			}
		} else {
			return Result.build("222", "登陆失效,请重新登录");
		}
	}
}
