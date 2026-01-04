package com.util;

import com.model.User;
import javax.servlet.http.HttpSession;

/**
 * 管理员权限校验工具类
 */
public class AdminUtil {
    /**
     * 校验当前用户是否为管理员
     * @param session 当前会话
     * @return true-是管理员；false-非管理员
     */
    public static boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && user.getRole() == 1; // role=1代表管理员
    }
}