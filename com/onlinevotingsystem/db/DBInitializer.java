package com.onlinevotingsystem.db;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

public class DBInitializer {
    public static void initializeDatabase() {
        try (Connection connection = DBConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Create tables (you can add more tables here)
            String createVotersTable = "CREATE TABLE IF NOT EXISTS voters (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL," +
                    "voter_id VARCHAR(20) NOT NULL," +
                    "address VARCHAR(255)," +
                    "date_of_birth DATE" +
                    ")";
            statement.execute(createVotersTable);

            String createCandidatesTable = "CREATE TABLE IF NOT EXISTS candidates (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "party VARCHAR(255) NOT NULL," +
                    "platform TEXT" +
                    ")";
            statement.execute(createCandidatesTable);

            String createPollsTable = "CREATE TABLE IF NOT EXISTS polls (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "question VARCHAR(255) NOT NULL," +
                    "option1 VARCHAR(255) NOT NULL," +
                    "option2 VARCHAR(255) NOT NULL," +
                    "votes_option1 INT DEFAULT 0," +
                    "votes_option2 INT DEFAULT 0" +
                    ")";
            statement.execute(createPollsTable);

            String createResultsTable = "CREATE TABLE IF NOT EXISTS election_results (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "election_name VARCHAR(255) NOT NULL," +
                    "winner_name VARCHAR(255) NOT NULL," +
                    "votes_winner INT NOT NULL" +
                    ")";
            statement.execute(createResultsTable);

            // You can add more tables or modify the existing ones

            System.out.println("Database tables created or already exist.");

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database initialization exceptions
        }
    }
}
