<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图书馆管理系统 - 登录</title>
    <style>
        .login-box {width: 400px; margin: 100px auto; padding: 30px; border: 1px solid #ccc; border-radius: 8px;}
        .login-box h2 {text-align: center; margin-bottom: 20px;}
        .form-item {margin-bottom: 15px;}
        .form-item label {display: inline-block; width: 80px; text-align: right; margin-right: 10px;}
        .form-item input {width: 250px; padding: 6px;}
        .btn {width: 100%; padding: 8px; background-color: #4CAF50; color: white; border: none; border-radius: 4px; cursor: pointer;}
        .btn:hover {background-color: #45a049;}
        .error-msg {color: red; text-align: center; margin-bottom: 15px;}
    </style>
</head>
<body>
    <div class="login-box">
        <h2>图书馆管理系统登录</h2>
        <!-- 错误提示 -->
        <div class="error-msg"><%= request.getAttribute("msg") == null ? "" : request.getAttribute("msg") %></div>
        <!-- 登录表单 -->
        <form action="login" method="post">
            <div class="form-item">
                <label for="phone">手机号：</label>
                <input type="text" id="phone" name="phone" placeholder="请输入手机号" required>
            </div>
            <div class="form-item">
                <label for="password">密码：</label>
                <input type="password" id="password" name="password" placeholder="请输入密码" required>
            </div>
            <button type="submit" class="btn">登录</button>
        </form>
    </div>
</body>
</html>