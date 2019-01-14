package com.zhizhuotec.common.redis;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

@Component
public class RedisService {

	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * get value
	 */
	public String get(final String key) {
		return jedisCluster.get(key);
	}

	/**
	 * set value
	 */
	public String set(final String key, final String value) {
		return jedisCluster.set(key, value);
	}

	/**
	 * 获取hkey里的key对应值
	 */
	public String hget(final String hkey, final String key) {
		return jedisCluster.hget(hkey, key);
	}

	/**
	 * 设置hkey里的key对应值
	 */
	public long hset(final String hkey, final String key, final String value) {
		return jedisCluster.hset(hkey, key, value);
	}

	/**
	 * 删除key
	 */
	public long del(final String key) {
		return jedisCluster.del(key);
	}

	/**
	 * value值+1
	 */
	public long incr(final String key) {
		return jedisCluster.incr(key);
	}

	/**
	 * hkey里的key对应值+value
	 */
	public long hincr(final String hkey, final String key, final Long value) {
		return jedisCluster.hincrBy(hkey, key, value);
	}

	/**
	 * set 过期时间(秒)
	 */
	public long expire(final String key, final int second) {
		return jedisCluster.expire(key, second);
	}

	/**
	 * get 过期时间(秒)
	 */
	public long ttl(final String key) {
		return jedisCluster.ttl(key);
	}

}
