package com.github.ordinarykai.framework.common.result;

/**
 * 返回码枚举
 *
 * @author kai
 * @date 2022/3/12 12:19
 */
public enum ResultCode {

    // ==================== 成功 ====================
    SUCCESS(200, "Success"),

    // ==================== 客户端错误 ====================
    FAILED(400, "Failed Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),

    // ==================== 服务端错误 ====================
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    /**
     * 返回码
     */
    private final int code;
    /**
     * 返回消息
     */
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
