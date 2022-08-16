package com.github.ordinarykai.framework.auth.core;

import lombok.Data;

import java.util.List;

/**
 * 用户登录缓存信息
 *
 * @author kai
 * @date 2022/3/12 13:47
 */
@Data
public class AuthInfo {

    /**
     * 当前登录者ID
     */
    private Long id;

    /**
     * 当前登录者账号
     */
    private String account;

    /**
     * 当前登录者角色ID集合
     */
    private List<Long> roleIds;

    /**
     * 当前登录者权限集合
     */
    private List<String> permissions;

}
