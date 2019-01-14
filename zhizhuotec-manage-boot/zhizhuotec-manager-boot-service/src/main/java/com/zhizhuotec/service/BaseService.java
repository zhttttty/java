package com.zhizhuotec.service;

import java.util.List;

public interface BaseService<T> {

	/**
	 * 查询日期范围数据
	 */
	public List<T> findByDays(String userId, Long begin, Long end);

	/**
	 * 查询起始日期的所有数据
	 */
	public List<T> download(T t);

	/**
	 * 上传数据
	 */
	public int upload(String uId, String data);

}
