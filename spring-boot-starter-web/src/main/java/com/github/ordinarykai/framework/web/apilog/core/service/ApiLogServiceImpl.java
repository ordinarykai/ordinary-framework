package com.github.ordinarykai.framework.web.apilog.core.service;

import cn.hutool.core.util.StrUtil;
import com.github.ordinarykai.framework.web.apilog.core.entity.ApiLog;
import com.github.ordinarykai.framework.common.result.Result;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.client.RestTemplate;

/**
 * API 访问日志 Framework Service 实现类
 *
 * @author 芋道源码
 */
@Data
@AllArgsConstructor
public class ApiLogServiceImpl implements ApiLogService {

    private final RestTemplate restTemplate;
    private final String url;

    @Override
    @Async
    public void createApiLog(ApiLog apiLog) {
        if (StrUtil.isBlank(url)) {
            return;
        }
        restTemplate.postForEntity(url, apiLog, Result.class);
    }

}
