package io.github.ordinarykai.framework.sms.core.servcie;

import io.github.ordinarykai.framework.sms.core.SmsResult;

import java.util.TreeMap;

/**
 * @author kai
 * @date 2022/3/12 13:59
 */
public interface SmsService {

    /**
     * 发送短信
     * @param phone 手机号
     * @param templateKey 自定义模板key
     * @param templatePrams 模板参数
     */
    SmsResult send(String phone, String templateKey, TreeMap<String,String> templatePrams);

}