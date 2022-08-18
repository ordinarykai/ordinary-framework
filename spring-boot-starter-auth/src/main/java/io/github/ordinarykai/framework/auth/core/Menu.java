package io.github.ordinarykai.framework.auth.core;

import java.lang.annotation.*;

/**
 * 自定义接口权限注解
 *
 * @author kai
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Menu {

    /**
     * 菜单权限值
     * 格式举例：“管理员管理:/system/admin/menu -> 管理员管理:/api/system/menu/page”
     *          “一级页面名称:一级页面路由 -> 二级页面名称:二级页面路由 -> ...”
     */
    String value();

}
