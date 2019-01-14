package com.zhizhuotec.service.impl;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.zhizhuotec.common.utils.Utils;
import com.zhizhuotec.mapper.BaseMapper;
import com.zhizhuotec.service.BaseService;

public class BaseServiceImpl<T> implements BaseService<T> {

	protected BaseMapper<T> mapper;

	public void setMapper(BaseMapper<T> mapper) {
		this.mapper = mapper;
	}

	@Override
	public List<T> findByDays(String userId, Long begin, Long end) {
		return this.mapper.findByDays(userId, begin, end);
	}

	@Override
	public List<T> download(T t) {
		return this.mapper.download(t);
	}

	@Override
	public int upload(String uId, String data) {
		int res = 0;
		Map<String, Object> map2 = new HashMap<String, Object>();
		try {
			// data转为list
			List<Map<String, Object>> newList = JSON.parseObject(data, new TypeReference<List<Map<String, Object>>>() {
			});
			int newListSize = newList.size();
			if (newListSize > 0) {
				Set<Long> set = new HashSet<Long>();
				map2.put("uId", uId);
				map2.put("data", newList);
				// 获取list包含的数据库所有数据
				List<T> oldList = this.mapper.findByDate(map2);
				int oldListSize = oldList.size();
				// 数据库是否有数据
				if (oldListSize > 0) {
					for (int i = 0; i < newListSize; i++) {
						for (T t : oldList) {
							Class<?> clazz = t.getClass();
							// 遍历是否有相同的dates
							if (newList.get(i).get("dates").equals(clazz.getMethod("getDates").invoke(t))) {
								// 总时间数组
								List<Long> times = new ArrayList<Long>();
								// 总值数组
								List<Integer> results = new ArrayList<Integer>();
								// 合并时间数组
								times.addAll(JSON.parseObject((String) clazz.getMethod("getTimes").invoke(t),
										new TypeReference<List<Long>>() {
										}));
								// 合并时间数组
								times.addAll(JSON.parseObject(newList.get(i).get("times").toString(),
										new TypeReference<List<Long>>() {
										}));
								// 合并值数组
								results.addAll(JSON.parseObject((String) clazz.getMethod("getResults").invoke(t),
										new TypeReference<List<Integer>>() {
										}));
								// 合并值数组
								results.addAll(JSON.parseObject(newList.get(i).get("results").toString(),
										new TypeReference<List<Integer>>() {
										}));
								int timesSize = times.size();
								int resultsSize = results.size();
								// 时间数组长度不等于值数组长度需要处理
								if (timesSize != resultsSize) {
									if (timesSize > resultsSize) {
										// 时间数组长度大于值数组长度
										for (int j = resultsSize; j < timesSize; j++) {
											// 删除
											times.remove(j);
											j--;
											timesSize--;
										}
									} else {
										// 值数组长度大于时间数组长度
										for (int j = timesSize; j < resultsSize; j++) {
											// 删除
											results.remove(j);
											j--;
											resultsSize--;
										}
									}
								}
								for (int j = timesSize - 1; j >= 0; j--) {
									// 去掉重复的时间(所对应的值取第一个)
									if (!set.add(times.get(j))) {
										times.remove(j);
										results.remove(j);
									}
								}
								set.clear();
								// 处理后的时间数组覆盖原list
								newList.get(i).put("times", JSON.toJSONString(times));
								// 处理后的值数组覆盖原list
								newList.get(i).put("results", JSON.toJSONString(results));
							} else {
								// 如果不相等同上
								List<Long> times = JSON.parseObject(newList.get(i).get("times").toString(),
										new TypeReference<List<Long>>() {
										});
								List<Integer> results = JSON.parseObject(newList.get(i).get("results").toString(),
										new TypeReference<List<Integer>>() {
										});
								int timesSize = times.size();
								int resultsSize = results.size();
								if (timesSize != resultsSize) {
									if (timesSize > resultsSize) {
										for (int j = resultsSize; j < timesSize; j++) {
											times.remove(j);
											j--;
											timesSize--;
										}
									} else {
										for (int j = timesSize; j < resultsSize; j++) {
											results.remove(j);
											j--;
											resultsSize--;
										}
									}
								}
								for (int j = timesSize - 1; j >= 0; j--) {
									if (!set.add(times.get(j))) {
										times.remove(j);
										results.remove(j);
									}
								}
								set.clear();
								newList.get(i).put("times", JSON.toJSONString(times));
								newList.get(i).put("results", JSON.toJSONString(results));
							}
						}
					}
				} else {
					// 没有相同的dates(逻辑同上)
					for (int i = 0; i < newListSize; i++) {
						List<Long> times = JSON.parseObject(newList.get(i).get("times").toString(),
								new TypeReference<List<Long>>() {
								});
						List<Integer> results = JSON.parseObject(newList.get(i).get("results").toString(),
								new TypeReference<List<Integer>>() {
								});
						int timesSize = times.size();
						int resultsSize = results.size();
						if (timesSize != resultsSize) {
							if (timesSize > resultsSize) {
								for (int j = resultsSize; j < timesSize; j++) {
									times.remove(j);
									j--;
									timesSize--;
								}
							} else {
								for (int j = timesSize; j < resultsSize; j++) {
									results.remove(j);
									j--;
									resultsSize--;
								}
							}
						}
						for (int j = timesSize - 1; j >= 0; j--) {
							if (!set.add(times.get(j))) {
								times.remove(j);
								results.remove(j);
							}
						}
						set.clear();
						newList.get(i).put("times", JSON.toJSONString(times));
						newList.get(i).put("results", JSON.toJSONString(results));
					}
				}
				// 处理后的data覆盖原data
				map2.put("data", newList);
				// 更新data返回被更新的所有dates字段
				String s = this.mapper.updateByBatch(map2);
				// 数据库是否有和data存在相同dates字段的数据
				if (s != null) {
					String[] dates = s.split(",");
					int len = dates.length;
					if (len < newListSize) {
						for (int i = 0; i < newListSize; i++) {
							for (int j = 0; j < len; j++) {
								// 被更新的所有dates字段与data遍历
								if (Long.valueOf(dates[j]).equals(newList.get(i).get("dates"))) {
									// 移除被更新的所有dates字段数据
									newList.remove(i);
									i--;
									newListSize--;
									break;
								}
							}
						}
						// 处理后的list覆盖原数据
						map2.put("data", newList);
						// 执行插入
						this.mapper.insertByBatch(map2);
					}
					res = 1;
				} else {
					// 直接执行插入
					if (this.mapper.insertByBatch(map2) > 0) {
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