package com.xyc.xupao.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/29 13:44
 * @Description: TODO 通用删除请求参数
 */
@Data
public class DeleteRequest implements Serializable {

	private static final long serialVersionUID = 6203956970235627264L;
	/** id */
	private Long id;

}
