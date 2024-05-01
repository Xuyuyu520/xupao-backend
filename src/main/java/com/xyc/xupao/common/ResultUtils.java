package com.xyc.xupao.common;

/**
 * 返回工具类
 *
 * @author <a href="https://github.com/Xuyuyu520">程序员小徐</a>
 * @from <a href="https://github.com/Xuyuyu520">主页知识主页</a>
 */
public class ResultUtils {

	/**
	 * 成功
	 *
	 * @param data
	 * @param <T>
	 * @return
	 */
	public static <T> BaseResponse<T> success(T data) {
		return new BaseResponse<>(0, data, "ok");
	}

	public static <T> BaseResponse<T> success(T data, String description) {
		return new BaseResponse<>(0, data, "ok", description);
	}

	/**
	 * 失败
	 *
	 * @param errorCode
	 * @return
	 */
	public static BaseResponse error(ErrorCode errorCode) {
		return new BaseResponse<>(errorCode);
	}

	/**
	 * 失败
	 *
	 * @param code
	 * @param message
	 * @param description
	 * @return
	 */
	public static BaseResponse error(int code, String message, String description) {
		return new BaseResponse(code, null, message, description);
	}

	/**
	 * 失败
	 *
	 * @param errorCode
	 * @return
	 */
	public static BaseResponse error(ErrorCode errorCode, String message, String description) {
		return new BaseResponse(errorCode.getCode(), null, message, description);
	}

	// https://space.bilibili.com/12890453/

	/**
	 * 失败
	 *
	 * @param errorCode
	 * @return
	 */
	public static BaseResponse error(ErrorCode errorCode, String description) {
		return new BaseResponse(errorCode.getCode(), errorCode.getMessage(), description);
	}
}

// [程序员交流园地]() 从 0 到 1 求职指导，斩获 offer！1 对 1 简历优化服务、200+ 真实简历和建议参考、25w 字前后端精选面试题、2000+ 求职面试经验分享
