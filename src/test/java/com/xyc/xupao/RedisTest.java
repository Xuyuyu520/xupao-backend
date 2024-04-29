package com.xyc.xupao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/28 10:08
 * @Description: TODO
 */
@SpringBootTest
public class RedisTest {
	@Autowired
	private RedisTemplate<String,Object> redisTemplate;

	@Test
	public void setRedisTemplateTest() {
		redisTemplate.opsForValue().set("key","value");
		redisTemplate.opsForValue().set("key2","value2");
		redisTemplate.opsForValue().set("key3","value3");
	}
	@Test
	public void getRedisTemplateTest() {
		redisTemplate.opsForValue().get("key");
		redisTemplate.opsForValue().get("key2");
		redisTemplate.opsForValue().get("key3");
	}
}
