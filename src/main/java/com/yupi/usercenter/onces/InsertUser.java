package com.yupi.usercenter.onces;

import com.yupi.usercenter.mapper.UserMapper;
import com.yupi.usercenter.model.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/27 下午6:25
 * @Description: TODO
 */
@Component
public class InsertUser {

	@Resource
	private UserMapper userMapper;

	/**
	 * 批量插入用户
	 *
	 * @return
	 */
	public void insertUser() {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
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
			userMapper.insert(user);

		}
		stopWatch.stop();
		System.out.println(stopWatch.getTotalTimeMillis());
	}
}
