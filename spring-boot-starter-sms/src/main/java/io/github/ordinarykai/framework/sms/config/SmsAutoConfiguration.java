package io.github.ordinarykai.framework.sms.config;

import io.github.ordinarykai.framework.sms.core.servcie.SmsService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.InvocationTargetException;

/**
 * @author wukai
 * @date 2022/8/8 17:32
 */
@Configuration
@EnableConfigurationProperties(SmsProperties.class)
@ConditionalOnProperty(prefix = "sms", name = {"enable"}, havingValue = "true")
public class SmsAutoConfiguration {

    @Bean
    public SmsService smsService(SmsProperties smsProperties) throws InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<? extends SmsService> smsService = smsProperties.getChannel().getSmsService();
        return smsService.getConstructor(SmsProperties.class).newInstance(smsProperties);
    }

}
