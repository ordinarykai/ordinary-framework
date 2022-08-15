package com.github.ordinarykai.framework.sms.core;

import lombok.Data;

/**
 * @author kai
 * @date 2022/3/12 13:59
 */
@Data
public class SmsResult {

    /**
     * 短信渠道原始code
     */
    private String code;

    private boolean success;

    private String message;

    private Exception exception;

    public static SmsResult success(String code) {
        SmsResult smsResult = new SmsResult();
        smsResult.setSuccess(true);
        smsResult.setMessage("OK");
        smsResult.setCode(code);
        return smsResult;
    }

    public static SmsResult fail(String code, String message) {
        SmsResult smsResult = new SmsResult();
        smsResult.setSuccess(false);
        smsResult.setMessage(message);
        smsResult.setCode(code);
        return smsResult;
    }

    public static SmsResult fail(Exception exception) {
        SmsResult smsResult = new SmsResult();
        smsResult.setSuccess(false);
        smsResult.setMessage(exception.getMessage());
        smsResult.setException(exception);
        return smsResult;
    }

}