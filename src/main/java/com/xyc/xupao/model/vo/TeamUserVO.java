package com.xyc.xupao.model.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/29 17:16
 * @Description: TODO
 */
@Data
public class TeamUserVO implements Serializable {
	private static final long serialVersionUID = -8453260583352337567L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * 队伍名称
	 */
	private String name;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 最大人数
	 */
	private Integer maxNum;

	/**
	 * 过期时间
	 */
	private Date expireTime;

	/**
	 * 用户id（队长 id）
	 */
	private Long userId;

	/**
	 * 0 - 公开，1 - 私有，2 - 加密
	 */
	private Integer status;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 *
	 */
	private Date updateTime;
	/**
	 * 创建人用户列表
	 */
	UserVO createUser;
}
