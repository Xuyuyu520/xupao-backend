package com.xyc.xupao.onces;

import com.xyc.xupao.model.domain.User;
import com.xyc.xupao.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/27 下午6:50
 * @Description: TODO
 */
@SpringBootTest
class InsertUserTest {

	@Resource
	private UserService userService;

	@Test
	void insertUser() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		List<User> userList = new ArrayList<>();
		for (int i = 0; i < 10000; i++) {
			User user = new User();
			user.setUsername("user" + i);
			user.setUserAccount("user" + i);
			user.setAvatarUrl("https://avatars.githubusercontent.com/u/122147109?v=4");
			user.setGender(0);
			user.setUserPassword("12345678");
			user.setPhone("123456" + i);
			user.setEmail(i + "@gmail.com");
			user.setUserStatus(0);
			user.setUserRole(0);
			user.setPlanetCode("" + i);
			user.setTags("java");
			userList.add(user);

		}
		userService.saveBatch(userList, 100);
		stopWatch.stop();
		System.out.println(stopWatch.getTotalTimeMillis());

	}

	@Test
	void doConcurrencyInsertUser() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		final int IN = 100000;
		int j = 0;
		List<CompletableFuture<Void>> futureList = new ArrayList<>();
		for (int i = 0; i < IN; i++) {
			List<User> userList = new ArrayList<>();
			while (true) {
				j++;
				User user = new User();
				user.setUsername("user" + i);
				user.setUserAccount("user" + i);
				user.setAvatarUrl("https://avatars.githubusercontent.com/u/122147109?v=4");
				user.setGender(0);
				user.setUserPassword("12345678");
				user.setPhone("123456" + i);
				user.setEmail(i + "@gmail.com");
				user.setUserStatus(0);
				user.setUserRole(0);
				user.setPlanetCode("" + i);
				user.setTags("java");
				userList.add(user);
				if (j%1000 ==0){
					break;
				}
			}
			//异步执行
			CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
				userService.saveBatch(userList, 100);
			});
			futureList.add(completableFuture);
		}
		CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
		stopWatch.stop();
		System.out.println(stopWatch.getTotalTimeMillis());

	}
}
