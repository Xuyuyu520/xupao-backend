package com.xyc.xupao.exception;

// [小徐的知识主页](https://t.zsxq.com/0emozsIJh) 从 0 到 1 求职指导，斩获 offer！1 对 1 简历优化服务、200+ 真实简历和建议参考、2000+ 求职面试经验分享、25w 字前后端精选面试题

import com.xyc.xupao.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author <a href="https://github.com/Xuyuyu520">程序员小徐</a>
 * @from <a href="https://github.com/Xuyuyu520">主页知识主页</a>
 */
public class BusinessException extends RuntimeException {

    /**
     * 异常码
     */
    private final int code;

    /**
     * 描述
     */
    private final String description;

    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    // https://t.zsxq.com/0emozsIJh

    public String getDescription() {
        return description;
    }
}
