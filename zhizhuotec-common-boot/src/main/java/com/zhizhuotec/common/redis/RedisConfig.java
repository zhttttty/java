package com.zhizhuotec.common.redis;

import java.util.HashSet;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	@Value("${spring.redis.cluster.nodes}")
	private String clusterNodes;

	@Value("${spring.redis.cluster.max-redirects}")
	private int maxAttempts;

	private String password;

	@Value("${spring.redis.timeout}")
	private int timeout;

	@Value("${spring.redis.jedis.pool.max-active}")
	private int maxTotal;

	@Value("${spring.redis.jedis.pool.max-wait}")
	private long maxWaitMillis;

	@Value("${spring.redis.jedis.pool.max-idle}")
	private int maxIdle;

	@Value("${spring.redis.jedis.pool.min-idle}")
	private int minIdle;

	@Bean
	public JedisCluster getJedisCluster() {
		String[] cNodes = clusterNodes.split(",");
		Set<HostAndPort> nodes = new HashSet<>();
		// 分割出集群节点
		for (String node : cNodes) {
			String[] hp = node.split(":");
			nodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
		}
		JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
		jedisPoolConfig.setMaxTotal(maxTotal);
		jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
		jedisPoolConfig.setMaxIdle(maxIdle);
		jedisPoolConfig.setMinIdle(minIdle);
		// 创建集群对象
		return new JedisCluster(nodes, timeout, timeout, maxAttempts, jedisPoolConfig);
	}

	/**
	 * 设置数据存入redis 的序列化方式 </br>
	 * redisTemplate序列化默认使用的jdkSerializeable,存储二进制字节码,导致key会出现乱码，所以自定义 序列化类
	 *
	 * @paramredisConnectionFactory
	 */
	// @Bean
	// public RedisTemplate<Object,Object> redisTemplate(RedisConnectionFactory
	// redisConnectionFactory)throws UnknownHostException {
	// RedisTemplate<Object,Object> redisTemplate = newRedisTemplate<>();
	// redisTemplate.setConnectionFactory(redisConnectionFactory);
	// Jackson2JsonRedisSerializer jackson2JsonRedisSerializer =new
	// Jackson2JsonRedisSerializer(Object.class);
	// ObjectMapper objectMapper =new ObjectMapper();
	// objectMapper.setVisibility(PropertyAccessor.ALL,JsonAutoDetect.Visibility.ANY);
	// objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
	// jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
	//
	// redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
	// redisTemplate.setKeySerializer(newStringRedisSerializer());
	//
	// redisTemplate.afterPropertiesSet();
	//
	// return redisTemplate;
	// }

}
