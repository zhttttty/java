package com.zhizhuotec.common.redis;

import redis.clients.jedis.Jedis;

public interface RedisCallback<T> {
	public T doInRedis(Jedis jedis);
}
