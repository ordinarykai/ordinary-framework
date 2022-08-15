package com.github.ordinarykai.framework.web.apilog.core.service;

import com.github.ordinarykai.framework.web.apilog.core.entity.ApiLog;

/**
 * API 访问日志 Framework Service 接口
 *
 * @author 芋道源码
 */
public interface ApiLogService {

    /**
     * 创建 API 访问日志
     *
     * @param apiLog API 访问日志
     */
    void createApiLog(ApiLog apiLog);

}
