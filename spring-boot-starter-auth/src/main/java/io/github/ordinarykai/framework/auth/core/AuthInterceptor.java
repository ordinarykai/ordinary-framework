package io.github.ordinarykai.framework.auth.core;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import io.github.ordinarykai.framework.auth.config.AuthProperties;
import io.github.ordinarykai.framework.common.result.ResultCode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static io.github.ordinarykai.framework.common.constant.HeaderConstant.USER_ID;

/**
 * 用户认证拦截器
 *
 * @author kai
 * @date 2022/3/12 14:21
 */
@Slf4j
@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    /**
     * 认证相关配置
     */
    private final AuthProperties authProperties;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        String token = request.getHeader(authProperties.getTokenName());
        // token为空，认证失败
        if (StrUtil.isBlank(token)) {
            return failed(response, ResultCode.UNAUTHORIZED);
        }
        AuthInfo authInfo = AuthUtil.get(token);
        // token对应redis缓存无数据，认证失败
        if (authInfo == null) {
            return failed(response, ResultCode.UNAUTHORIZED);
        }
        // 判断登录用户是否有权限访问该路径
        PreAuthorize preAuthorize = handler.getClass().getAnnotation(PreAuthorize.class);
        if (preAuthorize != null && CollectionUtil.isNotEmpty(authInfo.getPermissions()) && authInfo.getPermissions().contains(preAuthorize.value())) {
            return failed(response, ResultCode.FORBIDDEN);
        }
        // 响应头添加用户ID
        response.setHeader(USER_ID, String.valueOf(authInfo.getId()));
        return true;
    }

    /**
     * 认证授权失败，返回提示信息
     */
    private boolean failed(HttpServletResponse response, ResultCode resultCode) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        JSONObject res = new JSONObject();
        res.set("code", resultCode.getCode());
        res.set("message", resultCode.getMessage());
        PrintWriter out = response.getWriter();
        out.append(res.toString());
        log.warn(resultCode.getMessage());
        return false;
    }


}
