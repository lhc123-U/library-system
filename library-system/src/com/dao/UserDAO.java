package com.dao;

import com.model.User;
import com.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserDAO {
    // 根据手机号+密码查询用户
    public User login(String phone, String password) {
        String sql = "SELECT * FROM user WHERE phone=? AND password=?";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setUserId(rs.getLong("user_id"));
                user.setUserName(rs.getString("user_name"));
                user.setPhone(rs.getString("phone"));
                user.setRole(rs.getInt("role"));
                user.setStatus(rs.getInt("status"));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}