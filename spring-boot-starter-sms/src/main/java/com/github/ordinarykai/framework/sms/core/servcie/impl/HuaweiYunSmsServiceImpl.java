package com.github.ordinarykai.framework.sms.core.servcie.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSONObject;
import com.github.ordinarykai.framework.sms.config.SmsProperties;
import com.github.ordinarykai.framework.sms.core.SmsResult;
import com.github.ordinarykai.framework.sms.core.servcie.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author kai
 * @date 2022/3/12 13:59
 */
@Slf4j
public class HuaweiYunSmsServiceImpl implements SmsService {

    /**
     * 无需修改,用于格式化鉴权头域,给"X-WSSE"参数赋值
     */
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";
    /**
     * 用于格式化鉴权头域,给"Authorization"参数赋值
     */
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    private final SmsProperties properties;

    public HuaweiYunSmsServiceImpl(SmsProperties smsProperties) {
        this.properties = smsProperties;
    }

    @Override
    public SmsResult send(String phone, String templateKey, TreeMap<String, String> templatePrams) {

        String url = properties.getHuaweiExtend().getUrl();
        String appKey = properties.getAccessKeyId();
        String appSecret = properties.getAccessKeySecret();
        String sender = properties.getHuaweiExtend().getSender();
        String templateId = properties.getTemplates().get(templateKey);
        String signature = properties.getSignName();
        String templateParas = MapUtil.isEmpty(templatePrams) ? "" : "[\"" + String.join("\"", templatePrams.values()) + "\"]";

        // 请求Body
        String body = buildRequestBody(sender, phone, templateId, templateParas, signature);
        // 请求Headers中的X-WSSE参数值
        String wsseHeader = null;
        try {
            wsseHeader = buildWsseHeader(appKey, appSecret);
        } catch (NoSuchAlgorithmException e) {
            SmsResult.fail(e);
        }

        // 发送短信
        HttpRequest httpRequest = HttpRequest.post(url)
                .body(body)
                .header(Header.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .header("X-WSSE", wsseHeader)
                .header(Header.PROXY_AUTHORIZATION, AUTH_HEADER_VALUE);
        HttpResponse httpResponse = httpRequest.execute();
        String response = httpResponse.body();
        if (StringUtils.isNotBlank(response)) {
            String code = JSONObject.parseObject(response).getString("code");
            String description = JSONObject.parseObject(response).getString("description");
            if ("000000".equals(code)) {
                return SmsResult.success(code);
            }
            return SmsResult.fail(code, description);
        }
        return SmsResult.fail(String.valueOf(httpResponse.getStatus()), httpResponse.body());
    }

    /**
     * 构造请求Body体
     */
    private static String buildRequestBody(String sender,
                                   String receiver,
                                   String templateId,
                                   String templateParas,
                                   String signature) {
        Map<String, String> map = new HashMap<>();
        map.put("from", sender);
        map.put("to", receiver);
        map.put("templateId", templateId);
        map.put("templateParas", templateParas);
        map.put("signature", signature);
        return JSONObject.toJSONString(map);
    }

    /**
     * 构造X-WSSE参数值
     */
   private static String buildWsseHeader(String appKey, String appSecret) throws NoSuchAlgorithmException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date());
        String nonce = UUID.randomUUID().toString().replace("-", "");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((nonce + time + appSecret).getBytes());
        byte[] passwordDigest = md.digest();
        String passwordDigestBase64Str = Base64.getEncoder().encodeToString(passwordDigest);
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }

}