package io.github.ordinarykai.framework.auth.core;

import io.github.ordinarykai.framework.auth.config.AuthProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.handler.MappedInterceptor;

/**
 * @author wukai
 * @date 2022/8/9 14:56
 */
@Order
@AllArgsConstructor
public class InterceptorProcessor implements InstantiationAwareBeanPostProcessor {

    private AuthInterceptor authInterceptor;
    private AuthProperties authProperties;

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractHandlerMapping) {
            AbstractHandlerMapping handlerMapping = (AbstractHandlerMapping) bean;
            MappedInterceptor mappedInterceptor = new MappedInterceptor(authProperties.getAddPathPatterns().toArray(new String[0]),
                    authProperties.getExcludePathPatterns().toArray(new String[0]),
                    authInterceptor);
            handlerMapping.setInterceptors(mappedInterceptor);
        }
        return true;
    }

}
