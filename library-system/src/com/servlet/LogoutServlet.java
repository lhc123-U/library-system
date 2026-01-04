package com.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 退出登录Servlet，销毁当前用户Session
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取当前用户Session
        HttpSession session = req.getSession();
        if (session != null) {
            // 销毁Session，清除用户信息
            session.invalidate();
        }
        // 跳转至登录页
        resp.sendRedirect("login.jsp");
    }
}