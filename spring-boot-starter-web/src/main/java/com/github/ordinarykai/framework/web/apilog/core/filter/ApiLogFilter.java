package com.github.ordinarykai.framework.web.apilog.core.filter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.github.ordinarykai.framework.web.apilog.core.service.ApiLogService;
import com.github.ordinarykai.framework.web.apilog.core.entity.ApiLog;
import lombok.AllArgsConstructor;
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
import java.util.List;
import java.util.Map;

/**
 * API 访问日志 Filter
 *
 * @author 芋道源码
 */
@Slf4j
@AllArgsConstructor
public class ApiLogFilter extends OncePerRequestFilter {

    private final List<String> apiPrefix;
    private final ApiLogService apiLogService;

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

        try {
            // 继续过滤器
            filterChain.doFilter(request, response);
            // 正常执行，记录日志
            createApiLog(request, beginTim, queryString, requestBody, null);
        } catch (Exception ex) {
            // 异常执行，记录日志
            createApiLog(request, beginTim, queryString, requestBody, ex);
            throw ex;
        }
    }

    private void createApiLog(HttpServletRequest request, LocalDateTime beginTime,
                              Map<String, String> queryString, String requestBody, Exception ex) {
        ApiLog apiLog = new ApiLog();
        try {
            this.buildApiLog(apiLog, request, beginTime, queryString, requestBody, ex);
            apiLogService.createApiLog(apiLog);
        } catch (Throwable th) {
            log.error("[createApiLog][uri({}) log({}) 发生异常]", request.getRequestURI(), apiLog, th);
        }
    }

    private void buildApiLog(ApiLog apiLog, HttpServletRequest request, LocalDateTime beginTime,
                             Map<String, String> queryString, String requestBody, Exception ex) {
        // 设置访问结果
//        Result<?> result = WebFrameworkUtils.getCommonResult(request);
//        if (result != null) {
//            apiLog.setResultCode(result.getCode());
//            apiLog.setResultMsg(result.getMessage());
//        } else if (ex != null) {
//            apiLog.setResultCode(GlobalErrorCodeConstants.INTERNAL_SERVER_ERROR.getCode());
//            apiLog.setResultMsg(ExceptionUtil.getRootCauseMessage(ex));
//        } else {
//            apiLog.setResultCode(0);
//            apiLog.setResultMsg("");
//        }
        // 设置其它字段
        apiLog.setRequestUri(request.getRequestURI());
        Map<String, Object> requestParams = MapUtil.<String, Object>builder().put("query", queryString).put("body", requestBody).build();
        apiLog.setRequestParams(requestParams.toString());
        apiLog.setRequestMethod(request.getMethod());
        apiLog.setUserAgent(request.getHeader("User-Agent"));
        apiLog.setUserIp(ServletUtil.getClientIP(request));
        // 持续时间
        apiLog.setBeginTime(beginTime);
        apiLog.setEndTime(LocalDateTime.now());
        apiLog.setDuration(Duration.between(apiLog.getEndTime(), apiLog.getBeginTime()).getNano());
        log.info(apiLog.toString());
    }

}
