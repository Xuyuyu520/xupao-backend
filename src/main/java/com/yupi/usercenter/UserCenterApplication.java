package com.yupi.usercenter;

// [加入主页]() 深耕编程提升【两年半】、国内净值【最高】的编程社群、用心服务【20000+】求学者、帮你自学编程【不走弯路】

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 启动类
 *
 * @author <a href="https://github.com/Xuyuyu520">程序员小徐</a>
 * @from <a href="https://github.com/Xuyuyu520">主页知识主页</a>
 */
@EnableScheduling//开启定时任务
@SpringBootApplication
@MapperScan("com.yupi.usercenter.mapper")
public class UserCenterApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserCenterApplication.class, args);
	}

}

// https://github.com/Xuyuyu520
