package com.servlet;

import com.dao.UserDAO;
import com.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        // MD5加密密码
        String md5Pwd = md5(password);

        UserDAO dao = new UserDAO();
        User user = dao.login(phone, md5Pwd);
        if (user != null && user.getStatus() == 1) {
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect("bookList.jsp"); // 登录成功跳图书列表
        } else {
            req.setAttribute("msg", "账号或密码错误，或账号已被限制");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }

    // MD5加密工具方法
    private String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(str.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
}