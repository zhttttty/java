package com.zhizhuotec.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BaseMapper<T> {

	/**
	 * 批量插入数据
	 */
	public Integer insertByBatch(Map<String, Object> map);

	/**
	 * 批量更新数据
	 */
	public String updateByBatch(Map<String, Object> map);

	/**
	 * 按日期批量查询
	 */
	public List<T> findByDate(Map<String, Object> map);

	/**
	 * 查询日期范围数据
	 */
	public List<T> findByDays(@Param("userId") String userId, @Param("begin") Long begin, @Param("end") Long end);

	/**
	 * 查询起始日期数据
	 */
	public List<T> download(T t);

}
