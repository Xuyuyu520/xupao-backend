package com.xyc.xupao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/29 13:44
 * @Description: TODO 通用分页请求参数
 */
@Data
public class PageRequest implements Serializable {

	private static final long serialVersionUID = 6203956970235627264L;
	/** 页面大小 */
	private int pageSize = 10;
	/** 页码 */
	private int pageNum = 1;

}
