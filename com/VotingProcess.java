import com.onlinevotingsystem.candidate.Candidate;
import com.onlinevotingsystem.db.*;
import com.onlinevotingsystem.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class VotingProcess {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            // Initialize the database (create tables)
            DBInitializer.initializeDatabase();

            connection = DBConnection.getConnection();
            Logger.log("Voting Process Started");

            // Display the list of candidates
            displayCandidates(connection);

            // Get voter's choice
            int voterChoice = getVoterChoice();

            // Record the vote
            recordVote(connection, voterChoice);

            Logger.log("Voting Process Completed");

        } catch (Exception e) {
            e.printStackTrace();
            // Handle database connection exceptions
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void displayCandidates(Connection connection) throws SQLException {
        System.out.println("List of Candidates:");
        String query = "SELECT * FROM candidates";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int candidateId = resultSet.getInt("id");
                String candidateName = resultSet.getString("name");
                String party = resultSet.getString("party");
                String platform = resultSet.getString("platform");

                System.out.println(candidateId + ". " + candidateName + " (" + party + ")");
                System.out.println("   Platform: " + platform);
                System.out.println();
            }
        }
    }

    private static int getVoterChoice() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the candidate number you want to vote for: ");
        return scanner.nextInt();
    }

    private static void recordVote(Connection connection, int candidateId) throws SQLException {
        // Check if the candidateId is valid (exists in the candidates table)
        if (isValidCandidate(connection, candidateId)) {
            // Insert the vote into the database
            String insertQuery = "INSERT INTO votes (candidate_id) VALUES (?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
                preparedStatement.setInt(1, candidateId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Vote recorded successfully.");
                } else {
                    System.out.println("Failed to record the vote.");
                }
            }
        } else {
            System.out.println("Invalid candidate selection. Vote not recorded.");
        }
    }

    private static boolean isValidCandidate(Connection connection, int candidateId) throws SQLException {
        // Check if the candidateId exists in the candidates table
        String query = "SELECT * FROM candidates WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, candidateId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next(); // Returns true if the candidateId exists, false otherwise
            }
        }
    }
}
