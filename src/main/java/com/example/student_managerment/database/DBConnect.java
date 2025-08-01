package com.example.student_managerment.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    private final String jdbcSQL = "jdbc:mysql://localhost:3306/student_manager?useSSL=false&serverTimezone=UTC";
    private final String userName = "root";
    private final String passWord = "lienminhdanh1";

    public DBConnect() {
    }

    public Connection getConnect() {
        Connection conn = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(jdbcSQL, userName, passWord);
            System.out.println("Connected to database");
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }
}
