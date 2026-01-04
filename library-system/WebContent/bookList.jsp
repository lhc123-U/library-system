<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.dao.BookDAO" %>
<%@ page import="com.model.Book" %>
<%@ page import="java.util.List" %>
<%@ page import="com.model.User" %>
<%@ page import="com.dao.BorrowDAO" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.sql.PreparedStatement" %>
<%@ page import="java.sql.ResultSet" %>
<html>
<head>
    <title>图书馆管理系统 - 图书列表</title>
    <style>
        table {border-collapse: collapse; width: 80%; margin: 20px auto;}
        th, td {border: 1px solid #333; padding: 8px; text-align: center;}
        th {background-color: #f0f0f0;}
        .msg {color: green; font-size: 16px; text-align: center; margin: 10px;}
        .title {text-align: center; margin: 20px 0;}
    </style>
</head>
<body>
    <%
        // 未登录用户强制跳转登录页
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        BorrowDAO borrowDAO = new BorrowDAO();
    %>

    <h1 class="title">欢迎使用图书馆管理系统</h1>
    <h3 class="title">当前用户：<%= user.getUserName() %>（<%= user.getRole() == 1 ? "管理员" : "普通用户" %>）</h3>
    <div class="msg"><%= request.getAttribute("msg") == null ? "" : request.getAttribute("msg") %></div>

    <!-- 可借阅图书区域 -->
    <h3 style="text-align: center;">可借阅图书</h3>
    <table>
        <tr>
            <<<th>图书ID</</</th>
            <<<th>书名</</</th>
            <<<th>作者</</</th>
            <<<th>出版社</</</th>
            <<<th>库存</</</th>
            <<<th>操作</</</th>
        </tr>
        <%
            BookDAO bookDAO = new BookDAO();
            List<Book> availableBooks = bookDAO.listAvailableBooks();
            for (Book book : availableBooks) {
        %>
        <tr>
            <td><%= book.getBookId() %></td>
            <td><%= book.getBookName() %></td>
            <td><%= book.getAuthor() %></td>
            <td><%= book.getPublisher() %></td>
            <td><%= book.getStock() %></td>
            <td>
                <form action="borrow" method="post" style="display:inline;">
                    <input type="hidden" name="bookId" value="<%= book.getBookId() %>">
                    <button type="submit">借阅</button>
                </form>
            </td>
        </tr>
        <% } %>
    </table>

    <!-- 我的已借阅图书区域 -->
    <h3 style="text-align: center; margin-top: 30px;">我的已借阅图书</h3>
    <table>
        <tr>
            <<<th>图书ID</</</th>
            <<<th>书名</</</th>
            <<<th>应还日期</</</th>
            <<<th>操作</</</th>
        </tr>
        <%
            // 查询当前用户未归还的图书
            String borrowSql = "SELECT b.book_id, b.book_name, br.due_date " +
                               "FROM borrow_record br " +
                               "LEFT JOIN book b ON br.book_id = b.book_id " +
                               "WHERE br.user_id=? AND br.return_date IS NULL";
            try (Connection conn = com.util.DBUtil.getConn();
                 PreparedStatement pstmt = conn.prepareStatement(borrowSql)) {
                pstmt.setLong(1, user.getUserId());
                ResultSet rs = pstmt.executeQuery();
                while (rs.next()) {
                    Long borrowedBookId = rs.getLong("book_id");
                    String borrowedBookName = rs.getString("book_name");
                    String dueDate = rs.getString("due_date").substring(0, 10);
        %>
        <tr>
            <td><%= borrowedBookId %></td>
            <td><%= borrowedBookName %></td>
            <td><%= dueDate %></td>
            <td>
                <form action="returnBook" method="post" style="display:inline;">
                    <input type="hidden" name="bookId" value="<%= borrowedBookId %>">
                    <button type="submit">还书</button>
                </form>
            </td>
        </tr>
        <% } } catch (Exception e) { e.printStackTrace(); } %>
    </table>

    <!-- 管理员入口 + 退出登录 -->
    <div style="text-align: center; margin: 10px;">
        <% if (com.util.AdminUtil.isAdmin(session)) { %>
            <a href="admin/addBook.jsp">管理员-新增图书</a> | 
        <% } %>
        <a href="logout">退出登录</a>
    </div>
</body>
</html>