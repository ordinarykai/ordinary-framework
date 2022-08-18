package io.github.ordinarykai.framework.web.apilog.core.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * API 访问日志
 *
 * @author 芋道源码
 */
@Data
public class ApiLog {
    /**
     * 请求方法
     */
    private String requestMethod;
    /**
     * 访问地址
     */
    private String requestUri;
    /**
     * 请求参数
     */
    private String requestParams;
    /**
     * 返回结果
     */
    private String resultData;
    /**
     * 用户 ID
     */
    private String userId;
    /**
     * 用户 IP
     */
    private String userIp;
    /**
     * 浏览器 UA
     */
    private String userAgent;
    /**
     * 开始请求时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束请求时间
     */
    private LocalDateTime endTime;
    /**
     * 执行时长，单位：毫秒
     */
    private Integer duration;

    @Override
    public String toString() {
        return "end " + requestMethod + " " + requestUri + "\n" +
                "==================================================\n" +
                "user = {\"userId\":\"" + userId + "\",\"userIp\":\"" + userIp + "\",\"userAgent\":\"" + userAgent + "\"}\n" +
                "time = {\"beginTime\":\"" + beginTime + "\",\"endTime\":\"" + endTime + "\",\"duration\":\"" + duration + "\"}\n" +
                "params = " + requestParams + "\n" +
                "result = " + resultData + "\n" +
                "==================================================";
    }

}
