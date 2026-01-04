package com.dao;

import com.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

public class BorrowDAO {
    // 1. 查询未还的借阅记录（用于还书校验）
    public boolean isBorrowed(Long userId, Long bookId) {
        String sql = "SELECT * FROM borrow_record WHERE user_id=? AND book_id=? AND return_date IS NULL";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, bookId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. 执行还书操作（更新还书日期+计算罚款）
    public String returnBook(Long userId, Long bookId) {
        // 计算逾期天数与罚款
        String sql = "SELECT due_date FROM borrow_record WHERE user_id=? AND book_id=? AND return_date IS NULL";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, userId);
            pstmt.setLong(2, bookId);
            ResultSet rs = pstmt.executeQuery();
            if (!rs.next()) {
                return "未找到该图书的未还记录";
            }

            // 计算逾期天数
            java.util.Date dueDate = rs.getTimestamp("due_date");
            java.util.Date now = new java.util.Date();
            long diff = now.getTime() - dueDate.getTime();
            int overDays = (int) (diff / (1000 * 60 * 60 * 24));
            overDays = Math.max(overDays, 0); // 未逾期则为0

            // 计算罚款（0.5元/天，上限50元）
            double fine = overDays * 0.5;
            fine = Math.min(fine, 50.0);

            // 更新借阅记录（还书日期+罚款）
            String updateSql = "UPDATE borrow_record SET return_date=NOW(), fine_amount=? WHERE user_id=? AND book_id=? AND return_date IS NULL";
            try (PreparedStatement updatePstmt = conn.prepareStatement(updateSql)) {
                updatePstmt.setDouble(1, fine);
                updatePstmt.setLong(2, userId);
                updatePstmt.setLong(3, bookId);
                updatePstmt.executeUpdate();
            }

            // 恢复图书库存
            String stockSql = "UPDATE book SET stock=stock+1 WHERE book_id=?";
            try (PreparedStatement stockPstmt = conn.prepareStatement(stockSql)) {
                stockPstmt.setLong(1, bookId);
                stockPstmt.executeUpdate();
            }

            return overDays > 0 ? "还书成功，逾期" + overDays + "天，罚款" + fine + "元" : "还书成功，无逾期";
        } catch (Exception e) {
            e.printStackTrace();
            return "还书失败：" + e.getMessage();
        }
    }
}