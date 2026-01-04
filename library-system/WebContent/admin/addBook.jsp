<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.util.AdminUtil" %>
<html>
<head>
    <title>管理员 - 新增图书</title>
    <style>
        .form-box {width: 600px; margin: 50px auto; padding: 30px; border: 1px solid #ccc; border-radius: 8px;}
        .form-item {margin-bottom: 15px;}
        .form-item label {display: inline-block; width: 120px; text-align: right; margin-right: 10px;}
        .form-item input {width: 300px; padding: 6px;}
        .btn {padding: 8px 20px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;}
        .btn:hover {background-color: #45a049;}
        .msg {color: green; text-align: center; margin-bottom: 15px; font-size: 16px;}
        .error-msg {color: red; text-align: center; margin-bottom: 15px; font-size: 16px;}
        .back-link {text-align: center; margin-top: 20px;}
    </style>
</head>
<body>
    <%
        // 校验是否为管理员，否则跳转图书列表页
        if (!AdminUtil.isAdmin(session)) {
            request.setAttribute("msg", "无管理员权限");
            request.getRequestDispatcher("../bookList.jsp").forward(request, response);
            return;
        }
    %>

    <div class="form-box">
        <h2 style="text-align: center; margin-bottom: 20px;">新增图书</h2>
        
        <%-- 提示信息 --%>
        <% if (request.getAttribute("msg") != null) { %>
            <div class="msg"><%= request.getAttribute("msg") %></div>
        <% } %>

        <%-- 新增图书表单 --%>
        <form action="addBook" method="post">
            <div class="form-item">
                <label for="isbn">ISBN编号：</label>
                <input type="text" id="isbn" name="isbn" placeholder="请输入ISBN" required>
            </div>
            <div class="form-item">
                <label for="bookName">图书名称：</label>
                <input type="text" id="bookName" name="bookName" placeholder="请输入书名" required>
            </div>
            <div class="form-item">
                <label for="author">作者：</label>
                <input type="text" id="author" name="author" placeholder="请输入作者" required>
            </div>
            <div class="form-item">
                <label for="publisher">出版社：</label>
                <input type="text" id="publisher" name="publisher" placeholder="请输入出版社" required>
            </div>
            <div class="form-item">
                <label for="publishDate">出版日期：</label>
                <input type="date" id="publishDate" name="publishDate" required>
            </div>
            <div class="form-item">
                <label for="location">馆藏位置：</label>
                <input type="text" id="location" name="location" placeholder="如A区1架" required>
            </div>
            <div class="form-item">
                <label for="stock">库存数量：</label>
                <input type="number" id="stock" name="stock" min="1" value="1" required>
            </div>
            <div style="text-align: center;">
                <button type="submit" class="btn">提交新增</button>
            </div>
        </form>

        <div class="back-link">
            <a href="../bookList.jsp">返回图书列表</a>
        </div>
    </div>
</body>
</html>