package com.xyc.xupao.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/28 10:43
 * @Description: TODO
 */
@Configuration
public class RedisTemplateConfig {

	@Bean
	public RedisTemplate<String,Object>  redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		return redisTemplate;
	}
}
