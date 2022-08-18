package io.github.ordinarykai.framework.common.exception;

import io.github.ordinarykai.framework.common.result.ResultCode;

/**
 * 未认证异常
 *
 * @author kai
 * @date 2022/3/12 12:28
 */
public class UnauthorizedException extends ApiException {

    public UnauthorizedException() {
        super(ResultCode.UNAUTHORIZED);
    }

    public UnauthorizedException(String message) {
        super(ResultCode.UNAUTHORIZED, message);
    }

}
