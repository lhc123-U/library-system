package com.dao;

import com.model.Book;
import com.util.DBUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    // 查询所有在馆图书
    public List<Book> listAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE status=1 AND stock>0";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book();
                book.setBookId(rs.getLong("book_id"));
                book.setIsbn(rs.getString("isbn"));
                book.setBookName(rs.getString("book_name"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublishDate(rs.getDate("publish_date"));
                book.setLocation(rs.getString("location"));
                book.setStock(rs.getInt("stock"));
                book.setStatus(rs.getInt("status"));
                books.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return books;
    }

    // 扣减图书库存
    public boolean reduceStock(Long bookId) {
        String sql = "UPDATE book SET stock=stock-1 WHERE book_id=? AND stock>0";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bookId);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
 // 新增方法：添加图书（管理员功能）
    public boolean addBook(Book book) {
        String sql = "INSERT INTO book (isbn, book_name, author, publisher, publish_date, location, stock) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConn();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getIsbn());
            pstmt.setString(2, book.getBookName());
            pstmt.setString(3, book.getAuthor());
            pstmt.setString(4, book.getPublisher());
            pstmt.setDate(5, new java.sql.Date(book.getPublishDate().getTime()));
            pstmt.setString(6, book.getLocation());
            pstmt.setInt(7, book.getStock());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}