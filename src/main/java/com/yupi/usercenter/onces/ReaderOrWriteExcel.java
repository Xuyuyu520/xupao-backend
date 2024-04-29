package com.yupi.usercenter.onces;

import com.alibaba.excel.EasyExcel;

import java.io.File;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/25 17:46
 * @Description: TODO
 */
public class ReaderOrWriteExcel {
	public static void main(String[] args) {
		String fileName =  "demo.xlsx";
		// 这里默认读取第一个sheet
		EasyExcel.read(fileName, XuUserInfo.class, new TableListener()).sheet().doRead();
	}
}
