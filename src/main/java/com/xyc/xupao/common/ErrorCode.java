package com.xyc.xupao.common;

// 讲师 【coder_鱼_皮】 https://space.bilibili.com/12890453/

/**
 * 错误码
 *
 * @author <a href="https://github.com/Xuyuyu520">程序员小徐</a>
 * @from <a href="https://github.com/Xuyuyu520">主页知识主页</a>
 */
public enum ErrorCode {

	/** 成功 */
	SUCCESS(0, "ok", ""),
	/** 参数错误 */
	PARAMS_ERROR(40000, "请求参数错误", ""),
	/** 空错误 */
	NULL_ERROR(40001, "请求数据为空", ""),
	/** 未登录 */
	NOT_LOGIN(40100, "未登录", ""),
	/** 无身份验证 */
	NO_AUTH(40101, "无权限", ""),
	/** 禁止 */
	FORBIDDEN(40101, "禁止操作", ""),
	/** 系统错误 */
	SYSTEM_ERROR(50000, "系统内部异常", "");

	private final int code;

	/**
	 * 状态码信息
	 */
	private final String message;

	/**
	 * 状态码描述（详情）
	 */
	private final String description;

	ErrorCode(int code, String message, String description) {
		this.code = code;
		this.message = message;
		this.description = description;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	// https://t.zsxq.com/0emozsIJh

	public String getDescription() {
		return description;
	}
}
