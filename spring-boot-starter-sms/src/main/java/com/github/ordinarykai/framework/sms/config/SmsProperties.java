package com.github.ordinarykai.framework.sms.config;

import com.github.ordinarykai.framework.sms.core.SmsChannelEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author wukai
 * @date 2022/8/8 17:32
 */
@Data
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {

    /**
     * 是否开启短信功能，默认true
     */
    private boolean enable = true;
    /**
     * 短信渠道，默认阿里云
     */
    private SmsChannelEnum channel = SmsChannelEnum.ALI_YUN;
    /**
     *
     */
    private String accessKeyId;
    /**
     *
     */
    private String accessKeySecret;
    /**
     * 短信签名
     */
    private String signName;
    /**
     * 短信模板（key：自定义模板名称 value：模板code）
     */
    private Map<String, String> templates;
    /**
     * 华为云扩展参数
     */
    private HuaWeiYunExtendProperties huaweiExtend;
    /**
     * 阿里云扩展参数
     */
    private ALiYunExtendProperties aliExtend = new ALiYunExtendProperties();

    @Data
    public static class HuaWeiYunExtendProperties {
        /**
         * APP接入地址(在控制台"应用管理"页面获取)+接口访问URI
         */
        public String url;
        /**
         * 国内短信签名通道号或国际/港澳台短信通道号
         */
        public String sender;
    }

    @Data
    public static class ALiYunExtendProperties {
        public String endPoint = "cn-hangzhou";
    }

}
