package com.zhizhuotec.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.entity.StepNumber;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.mapper.StepNumberMapper;
import com.zhizhuotec.service.StepNumberService;

@Service
@Transactional(rollbackFor = Exception.class, readOnly = false)
public class StepNumberServiceImpl extends BaseServiceImpl<StepNumber> implements StepNumberService {

	@Override
	@Resource
	public void setMapper(BaseMapper<StepNumber> mapper) {
		super.setMapper(mapper);
	}

	private StepNumberMapper stepNumberMapper;

	@Resource
	public void setStepNumberMapper(StepNumberMapper stepNumberMapper) {
		this.stepNumberMapper = stepNumberMapper;
	}

	@Override
	public int upload(String uId, String data) {
		int res = 0;
		Map<String, Object> map2 = new HashMap<String, Object>();
		try {
			// data转为list
			List<Map<String, Object>> list = JSON.parseObject(data, new TypeReference<List<Map<String, Object>>>() {
			});
			int size = list.size();
			map2.put("uId", uId);
			map2.put("data", list);
			// 获取list包含的数据库所有数据
			List<StepNumber> array = stepNumberMapper.findByDate(map2);
			// 数据库是否有数据
			if (array.size() > 0) {
				for (int i = 0; i < size; i++) {
					for (StepNumber stepNumber : array) {
						if (stepNumber.getDates().equals(list.get(i).get("dates"))) {
							// 数据库获取的值数组
							List<Integer> oldResults = JSON.parseObject(stepNumber.getResults(),
									new TypeReference<List<Integer>>() {
									});
							// data的值数组
							List<Integer> newResults = JSON.parseObject(list.get(i).get("results").toString(),
									new TypeReference<List<Integer>>() {
									});
							int newResultsSize = newResults.size();
							for (int j = 0; j < newResultsSize; j++) {
								if (newResults.get(j) > oldResults.get(j)) {
									// 取数值大的
									oldResults.set(j, newResults.get(j));
								}
							}
							// 处理后的值数组覆盖原list
							list.get(i).put("results", JSON.toJSONString(oldResults));
						}
					}
				}
			}
			if (size > 0) {
				// 处理后的data覆盖原data
				map2.put("data", list);
				// 更新data返回被更新的所有dates字段
				String s = stepNumberMapper.updateByBatch(map2);
				// 数据库是否有和data存在相同dates字段的数据
				if (s != null) {
					String[] dates = s.split(",");
					int len = dates.length;
					if (len < size) {
						for (int i = 0; i < size; i++) {
							for (int j = 0; j < len; j++) {
								// 被更新的所有dates字段与data遍历
								if (Long.valueOf(dates[j]).equals(list.get(i).get("dates"))) {
									// 移除被更新的所有dates字段数据
									list.remove(i);
									i--;
									size--;
									break;
								}
							}
						}
						// 处理后的list覆盖原数据
						map2.put("data", list);
						// 执行插入
						stepNumberMapper.insertByBatch(map2);
					}
					res = 1;
				} else {
					// 直接执行插入
					if (stepNumberMapper.insertByBatch(map2) > 0) {
						res = 1;
					} else {
						res = 0;
					}
				}
			} else {
				// 如果data为空，直接返回成功
				res = 1;
			}
		} catch (Exception e) {
			res = 0;
			Utils.logger("ERROR", "Server exception：", e);
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return res;
	}
}