package com.campushub.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/campus_hub";

    private static final String username = System.getenv("DB_USERNAME");
    private static final String password = System.getenv("DB_PASSWORD");

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, username, password);
    }
}