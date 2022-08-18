package io.github.ordinarykai.framework.web.apilog.core.filter;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import io.github.ordinarykai.framework.web.apilog.core.entity.ApiLog;
import io.github.ordinarykai.framework.web.web.filter.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

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
        // 提前获得参数，避免 XssFilter 过滤处理
        Map<String, String> queryString = ServletUtil.getParamMap(request);
        String requestBody = StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE) ? ServletUtil.getBody(request) : "";
        // 打印请求uri
        log.info("start "+ request.getMethod() + " " + request.getRequestURI());
        try {
            // 继续过滤器
            filterChain.doFilter(request, response);
            // 正常执行，记录日志
            createApiLog(request, response, beginTim, queryString, requestBody, null);
        } catch (Exception ex) {
            // 异常执行，记录日志
            createApiLog(request, response, beginTim, queryString, requestBody, ex);
            throw ex;
        }
    }

    private void createApiLog(HttpServletRequest request, HttpServletResponse response, LocalDateTime beginTime,
                              Map<String, String> queryString, String requestBody, Exception ex) {
        ApiLog apiLog = new ApiLog();
        try {
            this.buildApiLog(apiLog, request, response, beginTime, queryString, requestBody, ex);
        } catch (Throwable th) {
            log.error("[createApiLog][uri({}) log({}) 发生异常]", request.getRequestURI(), apiLog, th);
        }
    }

    private void buildApiLog(ApiLog apiLog, HttpServletRequest request, HttpServletResponse response, LocalDateTime beginTime,
                             Map<String, String> queryString, String requestBody, Exception ex) throws IOException {

        // 请求相关
        apiLog.setRequestMethod(request.getMethod());
        apiLog.setRequestUri(request.getRequestURI());
        Map<String, Object> requestParams = MapUtil.<String, Object>builder().put("query", queryString).put("body", requestBody).build();
        apiLog.setRequestParams(requestParams.toString());
        apiLog.setResultData(new String(new ResponseWrapper(response).getContent()));

        // 用户相关
        apiLog.setUserId(response.getHeader("auth_id"));
        apiLog.setUserIp(ServletUtil.getClientIP(request));
        apiLog.setUserAgent(request.getHeader("User-Agent"));

        // 持续时间
        apiLog.setBeginTime(beginTime);
        apiLog.setEndTime(LocalDateTime.now());
        apiLog.setDuration(Duration.between(apiLog.getEndTime(), apiLog.getBeginTime()).getNano());

        log.info(apiLog.toString());
    }

}
