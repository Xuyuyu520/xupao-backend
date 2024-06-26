package com.xyc.xupao.config;

import lombok.Data;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/28 19:45
 * @Description: TODO redisson配置
 */
@Data
@Configuration
@ConfigurationProperties("spring.redis")
public class RedissonConfig {

	private String host;
	private Integer port;

	@Bean
	public RedissonClient redissonClient() {
		// 1. 创建配置
		Config config = new Config();
		String address = String.format("redis://%s:%d", host, port);
		config.useSingleServer().setAddress(address).setDatabase(0);
		// 2. 创建实例
		RedissonClient redisson = Redisson.create(config);
		return redisson;
	}
}
