package com.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&characterEncoding=utf8";
    private static final String USER = "root";
    private static final String PWD = "123456"; // Ìæ»»ÎªÄãµÄMySQLÃÜÂë

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver"); // ÊÊÅämysql-connector-java-5.1.47
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConn() {
        try {
            return DriverManager.getConnection(URL, USER, PWD);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
