package com.xyc.xupao;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/28 19:58
 * @Description: TODO
 */
@SpringBootTest
public class RedissonTest {
	@Resource
	RedissonClient redisson;

	@Test
	public void testRedisson() {
		System.out.println(redisson);
		//list
		val list = redisson.getList("d-0");
		list.add(0);
		list.add("11");
		list.remove(0);
		list.remove(1);
		System.out.println(list);
	}

}
