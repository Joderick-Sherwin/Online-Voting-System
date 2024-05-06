package com.onlinevotingsystem.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    // JDBC URL, username, and password of MySQL server
    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/online_voting_system";
    private static final String DB_USER = "Jodes";
    private static final String DB_PASSWORD = "23052215";

    public static Connection getConnection() {
        Connection connection = null;

        try {
            // Establish a connection to the database
            connection = DriverManager.getConnection(JDBC_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database connection exceptions
        }

        return connection;
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle database connection closing exceptions
            }
        }
    }
}
