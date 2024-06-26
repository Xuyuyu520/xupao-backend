package com.xyc.xupao.model.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/29 19:07
 * @Description: TODO  参加队伍请求体
 */
@Data
public class TeamJoinRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 更新id
	 */
	private Long id;
	/**
	 * 更新id
	 */
	private Long teamId;


	/**
	 * 密码
	 */
	private String password;

}
