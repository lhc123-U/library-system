package com.servlet;

import com.dao.BorrowDAO;
import com.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/returnBook")
public class ReturnBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            resp.sendRedirect("login.jsp");
            return;
        }

        Long bookId = Long.parseLong(req.getParameter("bookId"));
        BorrowDAO borrowDAO = new BorrowDAO();

        // 校验是否是当前用户的未还记录
        if (!borrowDAO.isBorrowed(user.getUserId(), bookId)) {
            req.setAttribute("msg", "您未借阅该图书，无法还书");
            req.getRequestDispatcher("bookList.jsp").forward(req, resp);
            return;
        }

        // 执行还书
        String result = borrowDAO.returnBook(user.getUserId(), bookId);
        req.setAttribute("msg", result);
        req.getRequestDispatcher("bookList.jsp").forward(req, resp);
    }
}