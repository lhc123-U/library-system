package com.servlet;

import com.dao.BookDAO;
import com.model.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Calendar;
import com.util.DBUtil;

@WebServlet("/borrow")
public class BorrowBookServlet extends HttpServlet {
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
        BookDAO bookDAO = new BookDAO();
        // 1. 扣减库存
        boolean reduceOk = bookDAO.reduceStock(bookId);
        if (!reduceOk) {
            req.setAttribute("msg", "图书库存不足，无法借阅");
            req.getRequestDispatcher("bookList.jsp").forward(req, resp);
            return;
        }

        // 2. 生成借阅记录（应还日期=当前+30天）
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 30);
        String dueDate = new java.sql.Date(cal.getTimeInMillis()).toString() + " 23:59:59";

        String sql = "INSERT INTO borrow_record (user_id, book_id, due_date) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, user.getUserId());
            pstmt.setLong(2, bookId);
            pstmt.setString(3, dueDate);
            pstmt.executeUpdate();
            req.setAttribute("msg", "借阅成功，应还日期：" + dueDate.split(" ")[0]);
        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("msg", "借阅失败：" + e.getMessage());
        }
        req.getRequestDispatcher("bookList.jsp").forward(req, resp);
    }
}