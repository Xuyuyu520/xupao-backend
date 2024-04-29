package com.yupi.usercenter.onces;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class XuUserInfo {

	/**
	 * 用名字去匹配，这里需要注意，如果名字重复，会导致只有一个字段读取到数据
	 */
	@ExcelProperty("注册码")
	private String planCode;
	@ExcelProperty("成员名称")
	private String username;
}
