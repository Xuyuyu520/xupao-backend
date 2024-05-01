package com.xyc.xupao.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/29 19:07
 * @Description: TODO  退出队伍请求体
 */
@Data
public class TeamQuitRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 更新id
	 */
	private Long id;

}
