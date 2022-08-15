package com.github.ordinarykai.framework.sms.core;

import com.github.ordinarykai.framework.sms.core.servcie.impl.HuaweiYunSmsServiceImpl;
import com.github.ordinarykai.framework.sms.core.servcie.SmsService;
import com.github.ordinarykai.framework.sms.core.servcie.impl.AliYunSmsServiceImpl;

/**
 * 短信渠道枚举
 *
 * @author wukai
 * @date 2022/8/11 14:22
 */
public enum SmsChannelEnum {

    /**
     * 阿里云
     */
    ALI_YUN(AliYunSmsServiceImpl.class),
    /**
     * 华为云
     */
    HUA_WEI_YUN(HuaweiYunSmsServiceImpl.class);

    SmsChannelEnum(Class<? extends SmsService> smsService) {
        this.smsService = smsService;
    }

    private final Class<? extends SmsService> smsService;

    public Class<? extends SmsService> getSmsService() {
        return smsService;
    }

}
