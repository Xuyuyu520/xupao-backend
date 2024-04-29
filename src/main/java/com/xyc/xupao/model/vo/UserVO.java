package com.xyc.xupao.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author: xuYuYu
 * @createTime: 2024/4/29 17:19
 * @Description: TODO
 */
@Data
public class UserVO implements Serializable {
	/**
	 * id
	 */
	private Long id;

	/**
	 * 用户昵称
	 */
	private String username;

	/**
	 * 账号
	 */
	private String userAccount;

	/**
	 * 用户头像
	 */
	private String avatarUrl;

	/**
	 * 性别
	 */
	private Integer gender;

	/**
	 * 电话
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 状态 0 - 正常
	 */
	private Integer userStatus;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 *
	 */
	private Date updateTime;


	/**
	 * 用户角色 0 - 普通用户 1 - 管理员
	 */
	private Integer userRole;

	/**
	 * 主页编号
	 */
	private String planetCode;

	/**
	 * 用户标签
	 */
	private String tags;

	// https://github.com/Xuyuyu520

	private static final long serialVersionUID = -4630054887642979314L;
}
