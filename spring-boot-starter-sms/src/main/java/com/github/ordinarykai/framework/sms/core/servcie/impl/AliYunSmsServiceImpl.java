package com.github.ordinarykai.framework.sms.core.servcie.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.github.ordinarykai.framework.sms.config.SmsProperties;
import com.github.ordinarykai.framework.sms.core.SmsResult;
import com.github.ordinarykai.framework.sms.core.servcie.SmsService;
import lombok.extern.slf4j.Slf4j;

import java.util.TreeMap;

/**
 * @author kai
 * @date 2022/3/12 13:59
 */
@Slf4j
public class AliYunSmsServiceImpl implements SmsService {

    private static final String OK = "OK";
    private static final String PRODUCT = "Dysmsapi";
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";
    private final SmsProperties properties;
    private final IAcsClient acsClient;

    public AliYunSmsServiceImpl(SmsProperties smsProperties) {
        this.properties = smsProperties;
        String accessKeyId = properties.getAccessKeyId();
        String accessKeySecret = properties.getAccessKeySecret();
        IClientProfile profile = DefaultProfile.getProfile(properties.getAliExtend().getEndPoint(), accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint(properties.getAliExtend().getEndPoint(), PRODUCT, DOMAIN);
        acsClient = new DefaultAcsClient(profile);
    }

    @Override
    public SmsResult send(String phone, String templateKey, TreeMap<String, String> templatePrams) {
        SendSmsRequest request = new SendSmsRequest();
        request.setSysMethod(MethodType.POST);
        request.setPhoneNumbers(phone);
        request.setSignName(properties.getSignName());
        request.setTemplateCode(properties.getTemplates().get(templateKey));
        request.setTemplateParam(JSONObject.toJSONString(templatePrams));
        try {
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            if (OK.equals(sendSmsResponse.getCode())) {
                return SmsResult.success(sendSmsResponse.getCode());
            }
            log.error("send fail[code={}, message={}]", sendSmsResponse.getCode(), sendSmsResponse.getMessage());
            return SmsResult.fail(sendSmsResponse.getCode(), sendSmsResponse.getMessage());
        } catch (Exception e) {
            log.error("send fail", e);
            return SmsResult.fail(e);
        }
    }

}