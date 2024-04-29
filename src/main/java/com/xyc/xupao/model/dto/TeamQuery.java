package com.xyc.xupao.model.dto;

import com.xyc.xupao.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 队伍封装类
 *
 * @author xuYuYu
 * @TableName team
 * @date 2024-04-29 13:25:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class TeamQuery extends PageRequest {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 队伍名称
	 */
	private String name;
	/**
	 * 搜索关键词
	 */
	private String searchText;

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

}
