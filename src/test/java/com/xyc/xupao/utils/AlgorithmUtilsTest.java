package com.xyc.xupao.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: xuYuYu
 * @createTime: 2024/5/1 11:10
 * @Description: TODO
 */
class AlgorithmUtilsTest {

	@Test
	void minimumEditDistance() {
		AlgorithmUtils algorithmUtils = new AlgorithmUtils();
		int i = algorithmUtils.minDistance("我是谁", "你是大一学生");
		int j = algorithmUtils.minDistance("就是啊过啦黑发", "nihsodenbusuanjklajflasjd");
		System.out.println(i);
		System.out.println(j);
	}

	@Test
	void algorithmEditDistance() {
		AlgorithmUtils algorithmUtils = new AlgorithmUtils();
		int i = algorithmUtils.minDistance(Arrays.asList("我是谁1", "你是大一学生"), Arrays.asList("我是谁1", "你是大一学生"));
		int j = algorithmUtils.minDistance(Arrays.asList("我是谁", "你是大一学生", "adhadh"), Arrays.asList("我是谁", "你是大一学生"));
		System.out.println(i);
		System.out.println(j);
	}
}
