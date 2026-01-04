package com.servlet;

import com.dao.BookDAO;
import com.model.Book;
import com.util.AdminUtil;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 管理员新增图书Servlet
 */
@WebServlet("/admin/addBook")
public class AddBookServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        
        // 1. 校验是否为管理员
        if (!AdminUtil.isAdmin(req.getSession())) {
            req.setAttribute("msg", "无管理员权限，无法执行此操作");
            req.getRequestDispatcher("bookList.jsp").forward(req, resp);
            return;
        }

        // 2. 获取前端传入的图书信息
        String isbn = req.getParameter("isbn");
        String bookName = req.getParameter("bookName");
        String author = req.getParameter("author");
        String publisher = req.getParameter("publisher");
        String publishDateStr = req.getParameter("publishDate");
        String location = req.getParameter("location");
        int stock = Integer.parseInt(req.getParameter("stock"));

        // 3. 转换日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date publishDate = null;
        try {
            publishDate = sdf.parse(publishDateStr);
        } catch (ParseException e) {
            req.setAttribute("msg", "日期格式错误，请输入yyyy-MM-dd");
            req.getRequestDispatcher("addBook.jsp").forward(req, resp);
            return;
        }

        // 4. 封装Book对象
        Book book = new Book();
        book.setIsbn(isbn);
        book.setBookName(bookName);
        book.setAuthor(author);
        book.setPublisher(publisher);
        book.setPublishDate(publishDate);
        book.setLocation(location);
        book.setStock(stock);
        book.setStatus(1); // 默认“在馆”状态

        // 5. 调用DAO新增图书
        BookDAO bookDAO = new BookDAO();
        boolean success = bookDAO.addBook(book);
        if (success) {
            req.setAttribute("msg", "图书新增成功");
        } else {
            req.setAttribute("msg", "图书新增失败，请检查ISBN是否重复");
        }
        req.getRequestDispatcher("addBook.jsp").forward(req, resp);
    }
}