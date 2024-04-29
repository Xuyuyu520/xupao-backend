package com.xyc.xupao.job;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xyc.xupao.config.RedissonConfig;
import com.xyc.xupao.model.domain.User;
import com.xyc.xupao.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author xuYuYu
 * @createTime: 2024/4/28 11:42
 * @Description: TODO  预缓存作业
 */
@Slf4j
public class PreCacheJob {
	@Resource
	private UserService userService;

	// 重点用户
	List<Long> mainUserList = Arrays.asList(1L);
	@Resource
	private RedisTemplate<String, Object> redisTemplate;
	@Resource
	private RedissonConfig redissonConfig;

	/**
	 * 定时任务 预缓存
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void doCache() {
		RLock lock = redissonConfig.redissonClient().getLock("xupao:precachejob:docache:lock");
		try {
			if (lock.tryLock(0, -1, TimeUnit.MICROSECONDS)) {
				for (Long userId : mainUserList) {

					String key = String.format("xupao:user:recommend:%s", userId);
					// 有 缓存 查询缓存
					ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
					Page<User> userPage = (Page<User>) valueOperations.get(key);
					QueryWrapper<User> queryWrapper = new QueryWrapper<>();
					userPage = userService.page(new Page<>(1, 20), queryWrapper);
					// 然后存入缓存
					try {
						valueOperations.set(key, userPage, 30000, TimeUnit.MICROSECONDS);
					} catch (Exception e) {
						log.error("存入缓存失败-redis set key Error ", e);
					}
				}
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			if (lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}

