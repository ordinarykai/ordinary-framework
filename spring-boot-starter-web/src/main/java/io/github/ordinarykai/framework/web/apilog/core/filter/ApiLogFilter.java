package io.github.ordinarykai.framework.web.apilog.core.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import io.github.ordinarykai.framework.web.apilog.core.entity.ApiLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static io.github.ordinarykai.framework.common.constant.HeaderConstant.USER_ID;

/**
 * API 访问日志 Filter
 *
 * @author 芋道源码
 */
@Slf4j
public class ApiLogFilter extends OncePerRequestFilter {

    private final List<String> apiPrefix;

    public ApiLogFilter(List<String> apiPrefix) {
        this.apiPrefix = CollectionUtil.isEmpty(apiPrefix) ? Collections.emptyList() : apiPrefix;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 只过滤 API 请求的地址
        return !StrUtil.startWithAny(request.getRequestURI(), apiPrefix.toArray(new String[0]));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // 获得开始时间
        LocalDateTime beginTim = LocalDateTime.now();
        // 打印请求uri
        log.info("start " + request.getMethod() + " " + request.getRequestURI());
        ContentCachingRequestWrapper nativeRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper nativeResponse = new ContentCachingResponseWrapper(response);
        try {
            // 继续过滤器
            filterChain.doFilter(nativeRequest, nativeResponse);
            // 正常执行，记录日志
            createApiLog(nativeRequest, nativeResponse, beginTim);
        } catch (Exception ex) {
            // 异常执行，记录日志
            createApiLog(nativeRequest, nativeResponse, beginTim);
            throw ex;
        }
    }

    private void createApiLog(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, LocalDateTime beginTime) {
        ApiLog apiLog = new ApiLog();
        try {
            this.buildApiLog(apiLog, request, response, beginTime);
        } catch (Throwable th) {
            log.error("[createApiLog][uri({}) log({}) 发生异常]", request.getRequestURI(), apiLog, th);
        }
    }

    private void buildApiLog(ApiLog apiLog, ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, LocalDateTime beginTime) throws IOException {

        // 获得queryParams
        Map<String, String> queryParams = ServletUtil.getParamMap(request);
        // 获得requestBody
        String requestBody = "";
        if (StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            requestBody = new String(request.getContentAsByteArray());
        }
        // 获得requestParams
        MapBuilder<String, Object> builder = MapUtil.builder();
        if (!queryParams.isEmpty()) {
            builder.put("query", queryParams);
        }
        if (StringUtils.hasText(requestBody)) {
            builder.put("body", requestBody);
        }
        Map<String, Object> requestParams = builder.build();
        // 获得responseBody
        String responseBody = "";
        if (StrUtil.startWithIgnoreCase(response.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            responseBody = new String(response.getContentAsByteArray());
            response.copyBodyToResponse();
        }

        // 请求相关
        apiLog.setRequestMethod(request.getMethod());
        apiLog.setRequestUri(request.getRequestURI());
        apiLog.setRequestParams(requestParams.toString());
        apiLog.setResultData(responseBody);

        // 用户相关
        String userId = response.getHeader(USER_ID);
        apiLog.setUserId(userId != null ? userId : "");
        apiLog.setUserIp(ServletUtil.getClientIP(request));

        // 持续时间
        apiLog.setBeginTime(beginTime);
        apiLog.setEndTime(LocalDateTime.now());
        apiLog.setDuration((int) Duration.between(apiLog.getBeginTime(), apiLog.getEndTime()).toMillis());

        log.info(apiLog.toString());
    }

}
